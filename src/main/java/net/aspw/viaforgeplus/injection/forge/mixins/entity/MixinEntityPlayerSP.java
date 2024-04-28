package net.aspw.viaforgeplus.injection.forge.mixins.entity;

import com.mojang.authlib.GameProfile;
import net.aspw.viaforgeplus.ProtocolInject;
import net.aspw.viaforgeplus.event.EventState;
import net.aspw.viaforgeplus.event.MotionEvent;
import net.aspw.viaforgeplus.event.PushOutEvent;
import net.aspw.viaforgeplus.event.UpdateEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    public MixinEntityPlayerSP(World p_i45074_1_, GameProfile p_i45074_2_) {
        super(p_i45074_1_, p_i45074_2_);
    }

    @Shadow
    public abstract boolean isSneaking();

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    private void onUpdateWalkingPlayer(CallbackInfo ci) {
        final MotionEvent event = new MotionEvent();
        ProtocolInject.eventManager.callEvent(event);
        event.setEventState(EventState.POST);
        ProtocolInject.eventManager.callEvent(event);
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void onLivingUpdate(CallbackInfo ci) {
        ProtocolInject.eventManager.callEvent(new UpdateEvent());
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