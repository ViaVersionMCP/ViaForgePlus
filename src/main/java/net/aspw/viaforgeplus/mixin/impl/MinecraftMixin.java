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

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void injectMod(CallbackInfo callbackInfo) {
        ViaForgePlus vfp = new ViaForgePlus();
        vfp.initVFPlatform();
    }

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    private void fixAttackOrder(CallbackInfo ci) {
        VersionDiffPatches.fixedAttackOrder(ci);
    }

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"), cancellable = true)
    private void blockBreakUnderThan1_7Hook(boolean leftClick, CallbackInfo ci) {
        VersionDiffPatches.blockBreakUnderThan1_7Hook(leftClick, ci);
    }
}
