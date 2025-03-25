package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.aspw.viaforgeplus.common.protocoltranslator.netty.VFPNetworkManager;
import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.NetworkManager$5")
public class NetworkManager_5Mixin {

    @Unique
    @Final
    @Mutable
    NetworkManager val$networkmanager;

    @Inject(method = "initChannel", at = @At(value = "TAIL"), remap = false)
    private void hookViaPipeline(Channel channel, CallbackInfo ci) {
        CommonViaForgePlus.getManager().inject(channel, (VFPNetworkManager) val$networkmanager);
    }
    
}
