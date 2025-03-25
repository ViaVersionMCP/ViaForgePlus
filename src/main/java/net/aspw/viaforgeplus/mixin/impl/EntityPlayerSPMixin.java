package net.aspw.viaforgeplus.mixin.impl;

import com.mojang.authlib.GameProfile;
import net.aspw.viaforgeplus.vfphooks.MotionFixes;
import net.aspw.viaforgeplus.vfphooks.VersionDiffPatches;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer {

    public EntityPlayerSPMixin(final World p_i45074_1_, final GameProfile p_i45074_2_) {
        super(p_i45074_1_, p_i45074_2_);
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void handleUpdateEvent(CallbackInfo ci) {
        MotionFixes.playerSizeHook();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    private void handleMotionEvent(CallbackInfo ci) {
        MotionFixes.handleEyeYHeight();
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void handlePushEvent(final CallbackInfoReturnable<Boolean> cir) {
        VersionDiffPatches.pushOutHook(cir);
    }
}
