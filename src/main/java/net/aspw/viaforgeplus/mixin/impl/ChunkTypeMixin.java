package net.aspw.viaforgeplus.mixin.impl;

import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.ChunkType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.ByteArrayOutputStream;

@Mixin(value = ChunkType.class, remap = false)
public class ChunkTypeMixin {

    @Redirect(method = "serialize", at = @At(value = "INVOKE", target = "Ljava/io/ByteArrayOutputStream;writeBytes([B)V"))
    private static void serialize(ByteArrayOutputStream instance, byte[] b) {
        instance.write(b, 0, b.length);
    }

}
