package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.vfphooks.MotionFixes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BlockLadder.class)
public abstract class BlockLadderMixin extends Block {

    public BlockLadderMixin(final Material p_i46399_1_, final MapColor p_i46399_2_) {
        super(p_i46399_1_, p_i46399_2_);
    }

    @ModifyConstant(method = "setBlockBoundsBasedOnState", constant = @Constant(floatValue = 0.125F))
    private float handleLadder(float constant) {
        return MotionFixes.handleLadder();
    }
}
