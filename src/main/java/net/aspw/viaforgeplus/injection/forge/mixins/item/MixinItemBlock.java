package net.aspw.viaforgeplus.injection.forge.mixins.item;

import net.aspw.viaforgeplus.api.ProtocolFixer;
import net.aspw.viaforgeplus.network.MinecraftInstance;
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
public class MixinItemBlock extends Item {

    @Shadow
    @Final
    public Block block;

    @Inject(method = "onItemUse", at = @At("HEAD"), cancellable = true)
    private void onItemUse(ItemStack p_onItemUse_1_, EntityPlayer p_onItemUse_2_, World p_onItemUse_3_, BlockPos p_onItemUse_4_, EnumFacing p_onItemUse_5_, float p_onItemUse_6_, float p_onItemUse_7_, float p_onItemUse_8_, CallbackInfoReturnable<Boolean> ci) {
        IBlockState iblockstate = p_onItemUse_3_.getBlockState(p_onItemUse_4_);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(p_onItemUse_3_, p_onItemUse_4_)) {
            p_onItemUse_4_ = p_onItemUse_4_.offset(p_onItemUse_5_);
        }

        if (p_onItemUse_1_.stackSize == 0) {
            ci.setReturnValue(false);
        } else if (!p_onItemUse_2_.canPlayerEdit(p_onItemUse_4_, p_onItemUse_5_, p_onItemUse_1_)) {
            ci.setReturnValue(false);
        } else if (p_onItemUse_3_.canBlockBePlaced(this.block, p_onItemUse_4_, false, p_onItemUse_5_, null, p_onItemUse_1_)) {
            int i = this.getMetadata(p_onItemUse_1_.getMetadata());
            IBlockState iblockstate1 = this.block.onBlockPlaced(p_onItemUse_3_, p_onItemUse_4_, p_onItemUse_5_, p_onItemUse_6_, p_onItemUse_7_, p_onItemUse_8_, i, p_onItemUse_2_);

            if (p_onItemUse_3_.setBlockState(p_onItemUse_4_, iblockstate1, 3)) {
                iblockstate1 = p_onItemUse_3_.getBlockState(p_onItemUse_4_);

                if (iblockstate1.getBlock() == this.block) {
                    ItemBlock.setTileEntityNBT(p_onItemUse_3_, p_onItemUse_2_, p_onItemUse_4_, p_onItemUse_1_);
                    this.block.onBlockPlacedBy(p_onItemUse_3_, p_onItemUse_4_, iblockstate1, p_onItemUse_2_, p_onItemUse_1_);
                }

                if (ProtocolFixer.newerThan1_8()) {
                    MinecraftInstance.mc.theWorld.playSoundAtPos(p_onItemUse_4_.add(0.5, 0.5, 0.5), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F, false);
                } else {
                    p_onItemUse_3_.playSoundEffect((float) p_onItemUse_4_.getX() + 0.5F, (float) p_onItemUse_4_.getY() + 0.5F, (float) p_onItemUse_4_.getZ() + 0.5F, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
                }

                --p_onItemUse_1_.stackSize;
            }

            ci.setReturnValue(true);
        } else {
            ci.setReturnValue(false);
        }
    }
}