package net.aspw.viaforgeplus.mixin.impl;

import com.viaversion.vialoader.netty.VLLegacyPipeline;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.aspw.viaforgeplus.common.protocoltranslator.netty.VFPNetworkManager;
import io.netty.channel.Channel;
import net.aspw.viaforgeplus.vfphooks.PacketFixer;
import net.minecraft.network.NettyEncryptingDecoder;
import net.minecraft.network.NettyEncryptingEncoder;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.util.CryptManager;
import net.raphimc.vialegacy.api.LegacyProtocolVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

@Mixin(NetworkManager.class)
public class NetworkManagerMixin implements VFPNetworkManager {

    @Shadow private Channel channel;

    @Shadow private boolean isEncrypted;
    @Unique
    private Cipher viaForgePlus$decryptionCipher;

    @Inject(method = "setCompressionTreshold", at = @At("RETURN"))
    public void reorderPipeline(int p_setCompressionTreshold_1_, CallbackInfo ci) {
        CommonViaForgePlus.getManager().reorderCompression(channel);
    }

    @Inject(method = "enableEncryption", at = @At("HEAD"), cancellable = true)
    private void storeEncryptionCiphers(SecretKey key, CallbackInfo ci) {
        if (CommonViaForgePlus.getManager().getTargetVersion().olderThanOrEqualTo(LegacyProtocolVersion.r1_6_4)) {
            ci.cancel();
            this.viaForgePlus$decryptionCipher = CryptManager.createNetCipherInstance(2, key);
            this.isEncrypted = true;
            this.channel.pipeline().addBefore(VLLegacyPipeline.VIALEGACY_PRE_NETTY_LENGTH_REMOVER_NAME, "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
        }
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;isChannelOpen()Z"), cancellable = true)
    private void handlePacket(final Packet<?> packet, final CallbackInfo ci) {
        PacketFixer.handlePacket(packet, ci);
    }

    @Override
    public void viaForgePlus$setupPreNettyDecryption() {
        this.channel.pipeline().addBefore(VLLegacyPipeline.VIALEGACY_PRE_NETTY_LENGTH_REMOVER_NAME, "decrypt", new NettyEncryptingDecoder(this.viaForgePlus$decryptionCipher));
    }
}
