package net.aspw.viaforgeplus.injection.forge.mixins.block;

import net.aspw.viaforgeplus.api.ProtocolFixer;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockLilyPad.class)
public abstract class MixinBlockLilyPad extends BlockBush {

    @Inject(method = "getCollisionBoundingBox", at = @At("HEAD"), cancellable = true)
    private void getCollisionBoundingBox(World p_getCollisionBoundingBox_1_, BlockPos p_getCollisionBoundingBox_2_, IBlockState p_getCollisionBoundingBox_3_, CallbackInfoReturnable<AxisAlignedBB> ci) {
        if (ProtocolFixer.newerThan1_8())
            ci.setReturnValue(new AxisAlignedBB((double) p_getCollisionBoundingBox_2_.getX() + 0.0625D, (double) p_getCollisionBoundingBox_2_.getY() + 0.0D, (double) p_getCollisionBoundingBox_2_.getZ() + 0.0625D, (double) p_getCollisionBoundingBox_2_.getX() + 0.9375D, (double) p_getCollisionBoundingBox_2_.getY() + 0.09375D, (double) p_getCollisionBoundingBox_2_.getZ() + 0.9375D));
        ci.setReturnValue(new AxisAlignedBB((double) p_getCollisionBoundingBox_2_.getX() + 0.0D, (double) p_getCollisionBoundingBox_2_.getY() + 0.0D, (double) p_getCollisionBoundingBox_2_.getZ() + 0.0D, (double) p_getCollisionBoundingBox_2_.getX() + 1.0D, (double) p_getCollisionBoundingBox_2_.getY() + 0.015625D, (double) p_getCollisionBoundingBox_2_.getZ() + 1.0D));
    }
}