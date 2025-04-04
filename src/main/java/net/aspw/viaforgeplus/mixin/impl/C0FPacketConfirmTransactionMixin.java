package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.vfphooks.PacketFixer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.io.IOException;

@Mixin(C0FPacketConfirmTransaction.class)
public class C0FPacketConfirmTransactionMixin {

    @Shadow
    private int windowId;

    @Inject(method = "writePacketData", at = @At("HEAD"), cancellable = true)
    public void fix1_17ConfirmTransaction$packetData(PacketBuffer p_writePacketData_1_, CallbackInfo ci) {
        PacketFixer.fix1_17ConfirmTransaction$packetData(p_writePacketData_1_, this.windowId, ci);
    }
}
