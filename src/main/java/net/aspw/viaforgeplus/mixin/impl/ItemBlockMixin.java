package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.vfphooks.MotionFixes;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBlock.class)
public class ItemBlockMixin extends Item {

    @Shadow
    @Final
    public Block block;

    @Inject(method = "onItemUse", at = @At("HEAD"), cancellable = true)
    private void handlePlaceBlockSound(ItemStack p_onItemUse_1_, EntityPlayer p_onItemUse_2_, World p_onItemUse_3_, BlockPos p_onItemUse_4_, EnumFacing p_onItemUse_5_, float p_onItemUse_6_, float p_onItemUse_7_, float p_onItemUse_8_, CallbackInfoReturnable<Boolean> cir) {
        MotionFixes.handleBlockPlaceSound(block, this.getMetadata(p_onItemUse_1_.getMetadata()), p_onItemUse_1_, p_onItemUse_2_, p_onItemUse_3_, p_onItemUse_4_, p_onItemUse_5_, p_onItemUse_6_, p_onItemUse_7_, p_onItemUse_8_, cir);
    }
}
