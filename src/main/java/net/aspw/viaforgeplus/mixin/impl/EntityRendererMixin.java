package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.vfphooks.Interpolation;
import net.aspw.viaforgeplus.vfphooks.MotionFixes;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Unique
    private float viaForgePlus$height;
    @Unique
    private float viaForgePlus$previousHeight;

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getEyeHeight()F"))
    public float modifyEyeHeight(Entity entity, float partialTicks) {
        return Interpolation.lerp(viaForgePlus$previousHeight, viaForgePlus$height, partialTicks);
    }

    @Inject(method = "updateRenderer", at = @At("HEAD"))
    private void interpolateHeight(CallbackInfo ci) {
        float eyeHeight = MotionFixes.eyeHeight;
        viaForgePlus$previousHeight = viaForgePlus$height;
        viaForgePlus$height = eyeHeight < viaForgePlus$height ? eyeHeight : viaForgePlus$height + ((eyeHeight - viaForgePlus$height) * 0.5f);
        Interpolation.animatedEyeHeight = viaForgePlus$height;
    }
}
