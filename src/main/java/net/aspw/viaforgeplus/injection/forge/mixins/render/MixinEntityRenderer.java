package net.aspw.viaforgeplus.injection.forge.mixins.render;

import net.aspw.viaforgeplus.api.AnimationUtils;
import net.aspw.viaforgeplus.network.MinecraftInstance;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Unique
    private float height;
    @Unique
    private float previousHeight;

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getEyeHeight()F"))
    public float modifyEyeHeight(Entity entity, float partialTicks) {
        return previousHeight + (height - previousHeight) * partialTicks;
    }

    @Inject(method = "updateRenderer", at = @At("HEAD"))
    private void interpolateHeight(CallbackInfo ci) {
        Entity entity = MinecraftInstance.mc.getRenderViewEntity();
        float eyeHeight = entity.getEyeHeight();
        previousHeight = height;
        if (eyeHeight < height)
            height = eyeHeight;
        else
            height += (eyeHeight - height) * 0.5f;
        AnimationUtils.setAnimatedEyeHeight(height);
    }
}