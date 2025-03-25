package net.aspw.viaforgeplus.mixin.impl;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.ViaForgePlus;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.aspw.viaforgeplus.vfphooks.VersionDiffPatches;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Objects;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    public int leftClickCounter;

    @Shadow
    public MovingObjectPosition objectMouseOver;

    @Shadow
    public EntityPlayerSP thePlayer;

    @Shadow
    public WorldClient theWorld;

    @Shadow
    public PlayerControllerMP playerController;

    @Shadow
    public EffectRenderer effectRenderer;

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void injectMod(CallbackInfo callbackInfo) {
        ViaForgePlus vfp = new ViaForgePlus();
        vfp.initVFPlatform();
    }

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    private void fixAttackOrder(CallbackInfo ci) {
        ci.cancel();

        if (this.leftClickCounter <= 0) {
            if (this.objectMouseOver != null && Objects.requireNonNull(this.objectMouseOver.typeOfHit) != MovingObjectPosition.MovingObjectType.ENTITY) {
                this.thePlayer.swingItem();
            }

            if (this.objectMouseOver != null) {
                switch (this.objectMouseOver.typeOfHit) {
                    case ENTITY:
                        VersionDiffPatches.sendFixedAttack(this.thePlayer, this.objectMouseOver.entityHit);
                        break;

                    case BLOCK:
                        BlockPos blockpos = this.objectMouseOver.getBlockPos();

                        if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                            this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
                            break;
                        }

                    case MISS:
                    default:
                        if (this.playerController.isNotCreative()) {
                            this.leftClickCounter = 10;
                        }
                }
            }
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"), cancellable = true)
    private void blockBreakUnderThan1_7Hook(boolean leftClick, CallbackInfo ci) {
        ci.cancel();

        if (!leftClick)
            this.leftClickCounter = 0;

        if (this.leftClickCounter <= 0) {
            if (leftClick && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockPos = this.objectMouseOver.getBlockPos();

                if (this.thePlayer.isUsingItem() && CommonViaForgePlus.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_8)) return;

                if (this.theWorld.getBlockState(blockPos).getBlock().getMaterial() != Material.air && this.playerController.onPlayerDamageBlock(blockPos, this.objectMouseOver.sideHit)) {
                    this.effectRenderer.addBlockHitEffects(blockPos, this.objectMouseOver.sideHit);
                    this.thePlayer.swingItem();
                }
            } else {
                this.playerController.resetBlockRemoving();
            }
        }
    }
}
