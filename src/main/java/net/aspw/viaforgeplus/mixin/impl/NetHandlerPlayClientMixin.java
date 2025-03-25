package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {

    @Inject(method = "handleJoinGame", at = @At("RETURN"))
    public void sendConnectionDetails(CallbackInfo ci) {
        if (CommonViaForgePlus.getManager().getTargetVersion().equals(CommonViaForgePlus.getManager().getNativeVersion())) return;

        CommonViaForgePlus.getManager().sendConnectionDetails(IMinecraft.mc.thePlayer.sendQueue.getNetworkManager().channel());
    }
}
