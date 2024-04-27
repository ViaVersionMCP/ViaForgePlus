package net.aspw.viaforgeplus.injection.forge.mixins.entity;

import com.mojang.authlib.GameProfile;
import net.aspw.viaforgeplus.ProtocolInject;
import net.aspw.viaforgeplus.api.ProtocolFixer;
import net.aspw.viaforgeplus.event.EventState;
import net.aspw.viaforgeplus.event.MotionEvent;
import net.aspw.viaforgeplus.event.PushOutEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Shadow
    public boolean serverSprintState;
    @Shadow
    @Final
    public NetHandlerPlayClient sendQueue;
    @Shadow
    public int positionUpdateTicks;
    @Shadow
    protected Minecraft mc;
    @Unique
    private boolean viaForge$prevOnGround;
    @Shadow
    private boolean serverSneakState;
    @Shadow
    private double lastReportedPosX;
    @Shadow
    private double lastReportedPosY;
    @Shadow
    private double lastReportedPosZ;
    @Shadow
    private float lastReportedYaw;
    @Shadow
    private float lastReportedPitch;
    @Unique
    private boolean lastOnGround;

    public MixinEntityPlayerSP(World p_i45074_1_, GameProfile p_i45074_2_) {
        super(p_i45074_1_, p_i45074_2_);
    }

    @Shadow
    public abstract boolean isSneaking();

    @Shadow
    protected abstract boolean isCurrentViewEntity();

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetHandlerPlayClient;addToSendQueue(Lnet/minecraft/network/Packet;)V", ordinal = 7))
    public void viaPatch(final NetHandlerPlayClient instance, final Packet<?> p_addToSendQueue_1_) {
        if (ProtocolFixer.newerThan1_8()) {
            if (this.viaForge$prevOnGround == this.onGround) {
                return;
            }
        }
        instance.addToSendQueue(p_addToSendQueue_1_);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"))
    public void viaPatch(final CallbackInfo ci) {
        this.viaForge$prevOnGround = this.onGround;
    }

    /**
     * @author As_pw
     * @reason Motion Event
     */
    @Overwrite
    public void onUpdateWalkingPlayer() {
        try {
            final MotionEvent event = new MotionEvent(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
            ProtocolInject.eventManager.callEvent(event);

            final boolean sprinting = this.isSprinting();
            final boolean sneaking = this.isSneaking();

            if (sprinting != this.serverSprintState) {
                if (sprinting)
                    this.sendQueue.addToSendQueue(new C0BPacketEntityAction((EntityPlayerSP) (Object) this, C0BPacketEntityAction.Action.START_SPRINTING));
                else
                    this.sendQueue.addToSendQueue(new C0BPacketEntityAction((EntityPlayerSP) (Object) this, C0BPacketEntityAction.Action.STOP_SPRINTING));

                this.serverSprintState = sprinting;
            }

            if (sneaking != this.serverSneakState) {
                if (sneaking)
                    this.sendQueue.addToSendQueue(new C0BPacketEntityAction((EntityPlayerSP) (Object) this, C0BPacketEntityAction.Action.START_SNEAKING));
                else
                    this.sendQueue.addToSendQueue(new C0BPacketEntityAction((EntityPlayerSP) (Object) this, C0BPacketEntityAction.Action.STOP_SNEAKING));

                this.serverSneakState = sneaking;
            }

            if (this.isCurrentViewEntity()) {
                float yaw = event.getYaw();
                float pitch = event.getPitch();

                final double xDiff = event.getX() - this.lastReportedPosX;
                final double yDiff = event.getY() - this.lastReportedPosY;
                final double zDiff = event.getZ() - this.lastReportedPosZ;
                final double yawDiff = yaw - lastReportedYaw;
                final double pitchDiff = pitch - lastReportedPitch;
                boolean moved = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff > 9.0E-4 || this.positionUpdateTicks >= 20;
                final boolean rotated = yawDiff != 0.0D || pitchDiff != 0.0D;

                if (this.ridingEntity == null) {
                    if (moved && rotated) {
                        sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(posX, getEntityBoundingBox().minY, posZ, yaw, pitch, onGround));
                    } else if (moved) {
                        sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, getEntityBoundingBox().minY, posZ, onGround));
                    } else if (rotated) {
                        sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, onGround));
                    } else {
                        sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
                    }
                } else {
                    sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(motionX, -999, motionZ, yaw, pitch, onGround));
                    moved = false;
                }

                ++this.positionUpdateTicks;

                if (moved) {
                    lastReportedPosX = posX;
                    lastReportedPosY = getEntityBoundingBox().minY;
                    lastReportedPosZ = posZ;
                    positionUpdateTicks = 0;
                }

                if (rotated) {
                    this.lastReportedYaw = yaw;
                    this.lastReportedPitch = pitch;
                }
            }

            if (this.isCurrentViewEntity())
                lastOnGround = event.getOnGround();

            event.setEventState(EventState.POST);

            ProtocolInject.eventManager.callEvent(event);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void pushOutEvent(final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final PushOutEvent event = new PushOutEvent();
        if (this.noClip) event.cancelEvent();
        ProtocolInject.eventManager.callEvent(event);

        if (event.isCancelled())
            callbackInfoReturnable.setReturnValue(false);
    }
}
