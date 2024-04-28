package net.aspw.viaforgeplus.injection.forge.mixins.client;

import net.aspw.viaforgeplus.ProtocolInject;
import net.aspw.viaforgeplus.api.ProtocolFixer;
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
public abstract class MixinMinecraft {

    @Shadow
    public MovingObjectPosition objectMouseOver;
    @Shadow
    public WorldClient theWorld;
    @Shadow
    public EntityPlayerSP thePlayer;
    @Shadow
    public EffectRenderer effectRenderer;
    @Shadow
    public PlayerControllerMP playerController;
    @Shadow
    public int leftClickCounter;

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void startGame(CallbackInfo callbackInfo) {
        ProtocolInject.INSTANCE.init();
    }

    /**
     * @author As_pw
     * @reason Attack Order Packet Fix
     */
    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    private void clickMouse(CallbackInfo ci) {
        if (this.leftClickCounter <= 0) {
            if (this.objectMouseOver != null && Objects.requireNonNull(this.objectMouseOver.typeOfHit) != MovingObjectPosition.MovingObjectType.ENTITY) {
                this.thePlayer.swingItem();
            }

            if (this.objectMouseOver != null) {
                switch (this.objectMouseOver.typeOfHit) {
                    case ENTITY:
                        ProtocolFixer.sendFixedAttack(this.thePlayer, this.objectMouseOver.entityHit);
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

        ci.cancel();
    }

    @Redirect(
            method = "clickMouse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;swingItem()V")
    )
    private void fixAttackOrder_VanillaSwing() {
        ProtocolFixer.sendConditionalSwing(this.objectMouseOver);
    }

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"), cancellable = true)
    private void sendClickBlockToController(boolean leftClick, CallbackInfo ci) {
        if (!leftClick)
            this.leftClickCounter = 0;

        if (this.leftClickCounter <= 0) {
            if (leftClick && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockPos = this.objectMouseOver.getBlockPos();

                if (this.thePlayer.isUsingItem() && ProtocolFixer.newerThanOrEqualsTo1_8())
                    return;

                if (this.theWorld.getBlockState(blockPos).getBlock().getMaterial() != Material.air && this.playerController.onPlayerDamageBlock(blockPos, this.objectMouseOver.sideHit)) {
                    this.effectRenderer.addBlockHitEffects(blockPos, this.objectMouseOver.sideHit);
                    this.thePlayer.swingItem();
                }
            } else {
                this.playerController.resetBlockRemoving();
            }
        }

        ci.cancel();
    }
}