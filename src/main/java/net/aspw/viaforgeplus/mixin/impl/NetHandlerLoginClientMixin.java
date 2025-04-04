package net.aspw.viaforgeplus.mixin.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.viaversion.viaversion.api.connection.UserConnection;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.aspw.viaforgeplus.common.protocoltranslator.netty.VFPNetworkManager;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.NetworkManager;
import net.raphimc.vialegacy.api.LegacyProtocolVersion;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.ProtocolMetadataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("DataFlowIssue")
@Mixin(NetHandlerLoginClient.class)
public class NetHandlerLoginClientMixin {

    @Shadow @Final private NetworkManager networkManager;

    @Redirect(method = "handleEncryptionRequest", at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/minecraft/MinecraftSessionService;joinServer(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Ljava/lang/String;)V"))
    public void onlyJoinServerIfPremium(MinecraftSessionService instance, GameProfile profile, String authenticationToken, String serverId) throws AuthenticationException {
        if (CommonViaForgePlus.getManager().getTargetVersion().olderThanOrEqualTo(LegacyProtocolVersion.r1_6_4)) {
            final UserConnection user = networkManager.channel().attr(CommonViaForgePlus.LOCAL_VIA_USER).get();
            if (user != null && user.has(ProtocolMetadataStorage.class) && !user.get(ProtocolMetadataStorage.class).authenticate) {
                return;
            }
        }
        instance.joinServer(profile, authenticationToken, serverId);
    }

}
