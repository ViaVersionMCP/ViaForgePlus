package net.aspw.viaforgeplus.mixin.impl;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.aspw.viaforgeplus.common.gui.ExtendedServerData;
import net.aspw.viaforgeplus.common.platform.VersionTracker;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetAddress;

@Mixin(OldServerPinger.class)
public class OldServerPingerMixin {

    @Unique
    private ServerData viaForgePlus$serverData;

    @Inject(method = "ping", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;createNetworkManagerAndConnect(Ljava/net/InetAddress;IZ)Lnet/minecraft/network/NetworkManager;"))
    public void trackServerData(ServerData server, CallbackInfo ci) {
        viaForgePlus$serverData = server;
    }

    @Redirect(method = "ping", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkManager;createNetworkManagerAndConnect(Ljava/net/InetAddress;IZ)Lnet/minecraft/network/NetworkManager;"))
    public NetworkManager trackVersion(InetAddress address, int i, boolean b) {
        try {
            if (viaForgePlus$serverData != null) {
                ProtocolVersion version = null;

                if (viaForgePlus$serverData instanceof ExtendedServerData) {
                    version = ((ExtendedServerData) viaForgePlus$serverData).viaForgePlus$getVersion();
                }

                if (version == null) {
                    version = CommonViaForgePlus.getManager().getTargetVersion();
                }

                if (address != null) {
                    VersionTracker.storeServerProtocolVersion(address, version);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            viaForgePlus$serverData = null;
        }

        return NetworkManager.createNetworkManagerAndConnect(address, i, b);
    }
}
