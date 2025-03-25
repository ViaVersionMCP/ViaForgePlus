package net.aspw.viaforgeplus.mixin.impl;

import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.types.BulkChunkType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.ByteArrayOutputStream;

@Mixin(value = BulkChunkType.class, remap = false)
public class BulkChunkTypeMixin {

    @Redirect(method = "write(Lio/netty/buffer/ByteBuf;[Lcom/viaversion/viaversion/api/minecraft/chunks/Chunk;)V", at = @At(value = "INVOKE", target = "Ljava/io/ByteArrayOutputStream;writeBytes([B)V"))
    private void write(ByteArrayOutputStream instance, byte[] b) {
        instance.write(b, 0, b.length);
    }

}
