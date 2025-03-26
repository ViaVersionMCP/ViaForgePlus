package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.vfphooks.Interpolation;
import net.aspw.viaforgeplus.vfphooks.MotionFixes;
import net.aspw.viaforgeplus.vfphooks.VersionDiffPatches;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBaseMixin {

    public EntityPlayerMixin(final World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @Inject(method = "getEyeHeight", at = @At("HEAD"), cancellable = true)
    public void hookEyeHeight(CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof EntityPlayerSP)
            cir.setReturnValue(Interpolation.lerp(MotionFixes.lastEyeHeight, MotionFixes.eyeHeight, IMinecraft.mc.timer.renderPartialTicks));
    }
}
