package net.aspw.viaforgeplus.mixin.impl;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.aspw.viaforgeplus.vfphooks.MotionFixes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin implements IBlockAccess {

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    private void handleDestroyBlockSound(BlockPos p_destroyBlock_1_, boolean p_destroyBlock_2_, CallbackInfoReturnable<Boolean> cir) {
        MotionFixes.handleBlockDestroySound(p_destroyBlock_1_, p_destroyBlock_2_, cir);
    }
}
