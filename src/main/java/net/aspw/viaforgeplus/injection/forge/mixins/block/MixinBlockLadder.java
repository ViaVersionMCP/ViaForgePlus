package net.aspw.viaforgeplus.injection.forge.mixins.block;

import net.aspw.viaforgeplus.api.ProtocolFixer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BlockLadder.class)
public abstract class MixinBlockLadder extends Block {

    public MixinBlockLadder(Material p_i46399_1_, MapColor p_i46399_2_) {
        super(p_i46399_1_, p_i46399_2_);
    }

    @ModifyConstant(method = "setBlockBoundsBasedOnState", constant = @Constant(floatValue = 0.125F))
    private float setBlockBoundsBasedOnState(float constant) {
        if (ProtocolFixer.newerThan1_8())
            return 0.1875F;
        return 0.125F;
    }
}