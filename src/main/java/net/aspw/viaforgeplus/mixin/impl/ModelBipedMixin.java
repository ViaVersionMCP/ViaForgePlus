package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.vfphooks.Interpolation;
import net.aspw.viaforgeplus.vfphooks.SwimmingAnimation;
import net.aspw.viaforgeplus.vfphooks.VersionDiffPatches;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public abstract class ModelBipedMixin {

    @Shadow
    public ModelRenderer bipedRightArm;

    @Shadow
    public ModelRenderer bipedLeftArm;

    @Shadow
    public boolean isSneak;

    @Shadow
    public ModelRenderer bipedHead;

    @Shadow
    public ModelRenderer bipedHeadwear;

    @Inject(method = "setRotationAngles", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelBiped;swingProgress:F"))
    private void swimmingAnimation(float p_setRotationAngles1, float p_setRotationAngles2, float p_setRotationAngles3, float p_setRotationAngles4, float p_setRotationAngles5, float p_setRotationAngles6, Entity p_setRotationAngles7, CallbackInfo ci) {
        if (!(p_setRotationAngles7 instanceof EntityPlayer) || !p_setRotationAngles7.equals(IMinecraft.mc.thePlayer)) return;

        SwimmingAnimation.handleSwimmingAnimation(bipedRightArm, bipedLeftArm, bipedHead, bipedHeadwear, p_setRotationAngles1, p_setRotationAngles2, p_setRotationAngles3, p_setRotationAngles4, p_setRotationAngles5, p_setRotationAngles6, p_setRotationAngles7, ci);
    }
}
