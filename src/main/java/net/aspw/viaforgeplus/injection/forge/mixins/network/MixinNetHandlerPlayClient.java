package net.aspw.viaforgeplus.injection.forge.mixins.network;

import net.aspw.viaforgeplus.api.ProtocolFixer;
import net.aspw.viaforgeplus.network.MinecraftInstance;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetHandlerPlayClient.class, priority = 1001)
public class MixinNetHandlerPlayClient {

    /**
     * @author FlorianMichael
     * @reason 1.17+ Transaction fix
     */
    @Inject(method = "handleConfirmTransaction", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift = At.Shift.AFTER), cancellable=true)
    private void handleConfirmTransaction(S32PacketConfirmTransaction packetIn, CallbackInfo ci) {
        if (ProtocolFixer.newerThanOrEqualsTo1_17()) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(new C0FPacketConfirmTransaction(packetIn.getWindowId(), (short) 0, false));
            ci.cancel();
        }
    }
}