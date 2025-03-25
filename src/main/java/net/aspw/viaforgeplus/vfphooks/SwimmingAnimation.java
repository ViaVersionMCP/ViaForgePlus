package net.aspw.viaforgeplus.vfphooks;

import net.aspw.viaforgeplus.IMinecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.logging.Logger;

public class SwimmingAnimation {

    @Unique
    private static float viaForgePlus$prevSwing = 0.0F;

    @Unique
    private static float viaForgePlus$prevRotateAngle = 0.0F;

    @Unique
    private static float viaForgePlus$prevTranslateZ = 0.0F;

    @Unique
    private static float viaForgePlus$prevHeadAngleX = 0.0F;

    @Unique
    private static boolean armFixed = false;

    @Unique
    private static long lastUpdateTime = System.nanoTime();

    public static void handleSwimmingAnimation(ModelRenderer bipedRightArm, ModelRenderer bipedLeftArm, ModelRenderer bipedHead, ModelRenderer bipedHeadwear, float p_setRotationAngles1, float p_setRotationAngles2, float p_setRotationAngles3, float p_setRotationAngles4, float p_setRotationAngles5, float p_setRotationAngles6, Entity p_setRotationAngles7, CallbackInfo callbackInfo) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0F;
        lastUpdateTime = currentTime;

        boolean isSwimming = VersionDiffPatches.shouldSwimOrCrawl();

        float targetRotate = isSwimming ? 10.0F : 0.0F;
        float targetTranslateZ = isSwimming ? -0.16F : 0.0F;

        float targetSwing = isSwimming ? IMinecraft.mc.thePlayer.limbSwing / 3.0F : 0.0F;
        float targetHeadAngleX = isSwimming ? -0.95F : 0.0F;

        float interpolationSpeed = 3.0F * deltaTime;
        float swingInterpolationSpeed = 7.0F * deltaTime;

        viaForgePlus$prevRotateAngle = Interpolation.lerp(viaForgePlus$prevRotateAngle, targetRotate, interpolationSpeed);
        viaForgePlus$prevTranslateZ = Interpolation.lerp(viaForgePlus$prevTranslateZ, targetTranslateZ, interpolationSpeed);
        if (!armFixed && isSwimming) {
            IMinecraft.mc.thePlayer.limbSwing = 0f;
            armFixed = true;
        } else viaForgePlus$prevSwing = Interpolation.lerp(viaForgePlus$prevSwing, targetSwing, swingInterpolationSpeed);
        viaForgePlus$prevHeadAngleX = Interpolation.lerp(viaForgePlus$prevHeadAngleX, targetHeadAngleX, interpolationSpeed);

        if (Math.abs(viaForgePlus$prevRotateAngle - targetHeadAngleX) > 0.001F && IMinecraft.mc.gameSettings.thirdPersonView != 0)
            GlStateManager.rotate(viaForgePlus$prevRotateAngle, 1F, 0.0F, 0.0F);

        if (Math.abs(viaForgePlus$prevTranslateZ - targetHeadAngleX) > 0.001F && IMinecraft.mc.gameSettings.thirdPersonView != 0)
            GlStateManager.translate(0.0F, 0.0F, viaForgePlus$prevTranslateZ);

        if (viaForgePlus$prevHeadAngleX != 0f && isSwimming && IMinecraft.mc.gameSettings.thirdPersonView != 0) {
            bipedHead.rotateAngleX = viaForgePlus$prevHeadAngleX;
            bipedHeadwear.rotateAngleX = viaForgePlus$prevHeadAngleX;
        }

        if (viaForgePlus$prevSwing != 0f && Math.abs(viaForgePlus$prevSwing - targetSwing) > 0.035F) {
            if (IMinecraft.mc.gameSettings.thirdPersonView != 0) {
                bipedLeftArm.rotateAngleX = viaForgePlus$prevSwing;
                bipedRightArm.rotateAngleX = viaForgePlus$prevSwing;
                bipedLeftArm.rotateAngleY = viaForgePlus$prevSwing;
                bipedRightArm.rotateAngleY = -viaForgePlus$prevSwing;
            }
        } else if (armFixed && !isSwimming) {
            IMinecraft.mc.thePlayer.limbSwing = 1.5f;
            armFixed = false;
        }
    }
}
