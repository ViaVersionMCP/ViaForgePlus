package net.aspw.viaforgeplus.injection.forge.mixins.packets;

import net.aspw.viaforgeplus.network.MinecraftInstance;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(C00Handshake.class)
public class MixinC00Handshake {

    @ModifyConstant(method = "writePacketData", constant = @Constant(stringValue = "\u0000FML\u0000"))
    private String connectionFixes(String constant) {
        return !MinecraftInstance.mc.isIntegratedServerRunning() ? "" : "\u0000FML\u0000";
    }
}