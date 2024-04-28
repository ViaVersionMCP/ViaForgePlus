package net.aspw.viaforgeplus.injection.forge.mixins.block;

import net.aspw.viaforgeplus.network.MinecraftInstance;
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
public abstract class MixinWorld implements IBlockAccess {

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    private void destroyBlock(BlockPos p_destroyBlock_1_, boolean p_destroyBlock_2_, CallbackInfoReturnable<Boolean> ci) {
        IBlockState iblockstate = MinecraftInstance.mc.theWorld.getBlockState(p_destroyBlock_1_);
        Block block = iblockstate.getBlock();

        MinecraftInstance.mc.theWorld.playAuxSFX(2001, p_destroyBlock_1_, Block.getStateId(iblockstate));

        if (block.getMaterial() == Material.air) {
            ci.setReturnValue(false);
        } else {
            if (p_destroyBlock_2_) {
                block.dropBlockAsItem(MinecraftInstance.mc.theWorld, p_destroyBlock_1_, iblockstate, 0);
            }

            ci.setReturnValue(MinecraftInstance.mc.theWorld.setBlockState(p_destroyBlock_1_, Blocks.air.getDefaultState(), 3));
        }
    }
}