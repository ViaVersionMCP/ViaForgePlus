package net.aspw.viaforgeplus.mixin.impl;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.aspw.viaforgeplus.common.gui.ExtendedServerData;
import net.aspw.viaforgeplus.common.platform.VersionTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Mixin(targets = "net.minecraft.client.multiplayer.GuiConnecting$1")
public class GuiConnecting_1Mixin {

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Ljava/net/InetAddress;getByName(Ljava/lang/String;)Ljava/net/InetAddress;"))
    public InetAddress trackServerVersion(String s) throws UnknownHostException {
        final InetAddress address = InetAddress.getByName(s);
        ProtocolVersion version = ((ExtendedServerData) IMinecraft.mc.getCurrentServerData()).viaForgePlus$getVersion();
        if (version == null) {
            version = CommonViaForgePlus.getManager().getTargetVersion();
        }
        VersionTracker.storeServerProtocolVersion(address, version);
        return address;
    }

}
