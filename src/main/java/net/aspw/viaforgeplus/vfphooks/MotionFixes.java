package net.aspw.viaforgeplus.vfphooks;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.logging.Level;

public class MotionFixes {

    public static float eyeHeight;
    public static float lastEyeHeight;

    private static boolean forceSneaking = false;

    public static void handleEyeYHeight() {
        float startHeight = 1.62f;
        float endHeight = IMinecraft.mc.isSingleplayer() ? 1.54f : VersionDiffPatches.shouldSwimOrCrawl() ? 0.45f : CommonViaForgePlus.getManager().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_8) ? 1.54f : CommonViaForgePlus.getManager().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_13_2) ? 1.47f : 1.32f;
        float delta = IMinecraft.mc.isSingleplayer() ? 0.154f : VersionDiffPatches.shouldSwimOrCrawl() ? 0.085f : CommonViaForgePlus.getManager().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_8) ? 0.154f : CommonViaForgePlus.getManager().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_13_2) ? 0.147f : 0.132f;

        lastEyeHeight = eyeHeight;

        Float target = VersionDiffPatches.shouldSwimOrCrawl() || IMinecraft.mc.thePlayer.isSneaking() ? Float.valueOf(endHeight) : eyeHeight < startHeight ? startHeight : null;

        if (target != null)
            eyeHeight = Interpolation.lerp(target, eyeHeight, 4f * delta);
    }

    public static void playerSizeHook() {
        if (VersionDiffPatches.shouldSwimOrCrawl() && IMinecraft.mc.thePlayer.isInWater()) {
            double d3 = IMinecraft.mc.thePlayer.getLookVec().yCoord;
            double d4 = 0.025D;
            double motionBoost = d3 <= 0.0D || IMinecraft.mc.thePlayer.worldObj.getBlockState(new BlockPos(IMinecraft.mc.thePlayer.posX, IMinecraft.mc.thePlayer.posY + 1.0D - 0.64D, IMinecraft.mc.thePlayer.posZ)).getBlock().getMaterial() == Material.water ? ((d3 - IMinecraft.mc.thePlayer.motionY) * d4) + 0.018 : 0.018;
            IMinecraft.mc.thePlayer.motionX *= 1.09F;
            IMinecraft.mc.thePlayer.motionZ *= 1.09F;
            IMinecraft.mc.thePlayer.motionY += motionBoost;
        }

        float newHeight = IMinecraft.mc.isSingleplayer() ? 1.8f : VersionDiffPatches.shouldSwimOrCrawl() ? 0.6f : IMinecraft.mc.thePlayer.isSneaking() && CommonViaForgePlus.getManager().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_8) ? 1.8f : IMinecraft.mc.thePlayer.isSneaking() && CommonViaForgePlus.getManager().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_13_2) ? 1.65f : IMinecraft.mc.thePlayer.isSneaking() ? 1.5f : 1.8f;

        double d0 = IMinecraft.mc.thePlayer.width / 2.0;
        AxisAlignedBB box = IMinecraft.mc.thePlayer.getEntityBoundingBox();
        AxisAlignedBB fixedBB = new AxisAlignedBB(IMinecraft.mc.thePlayer.posX - d0, box.minY, IMinecraft.mc.thePlayer.posZ - d0, IMinecraft.mc.thePlayer.posX + d0, box.minY + IMinecraft.mc.thePlayer.height, IMinecraft.mc.thePlayer.posZ + d0);
        AxisAlignedBB sneakBB = new AxisAlignedBB(box.minX, box.minY + 0.9, box.minZ, box.minX + 0.6, box.minY + 1.8, box.minZ + 0.6);

        IMinecraft.mc.thePlayer.setEntityBoundingBox(fixedBB);
        IMinecraft.mc.thePlayer.height = newHeight;

        if (!IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_9) && IMinecraft.mc.thePlayer.onGround && !IMinecraft.mc.theWorld.getCollisionBoxes(sneakBB).isEmpty()) {
            IMinecraft.mc.gameSettings.keyBindSneak.pressed = true;
            forceSneaking = true;
        } else if (IMinecraft.mc.theWorld.getCollisionBoxes(sneakBB).isEmpty() && forceSneaking) {
            if (!GameSettings.isKeyDown(IMinecraft.mc.gameSettings.keyBindSneak))
                IMinecraft.mc.gameSettings.keyBindSneak.pressed = false;
            forceSneaking = false;
        }
    }

    public static String handleHandshake(String constant) {
        return !IMinecraft.mc.isSingleplayer() ? "" : constant;
    }

    public static double handlePosition() {
        return !IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThan(ProtocolVersion.v1_8) ? 0.003 : 0.005;
    }

    public static float handleLadder() {
        return !IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThan(ProtocolVersion.v1_8) ? 0.1875f : 0.125f;
    }

    public static void handleLilyPad(BlockPos p_getCollisionBoundingBox_2_, CallbackInfoReturnable<AxisAlignedBB> cir) {
        if (!IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThan(ProtocolVersion.v1_8))
            cir.setReturnValue(new AxisAlignedBB((double) p_getCollisionBoundingBox_2_.getX() + 0.0625D, (double) p_getCollisionBoundingBox_2_.getY() + 0.0D, (double) p_getCollisionBoundingBox_2_.getZ() + 0.0625D, (double) p_getCollisionBoundingBox_2_.getX() + 0.9375D, (double) p_getCollisionBoundingBox_2_.getY() + 0.09375D, (double) p_getCollisionBoundingBox_2_.getZ() + 0.9375D));
    }

    public static float handleHitBoxSize() {
        return !IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThan(ProtocolVersion.v1_8) ? 0f : 0.1f;
    }

    public static void handleBlockDestroySound(BlockPos p_destroyBlock_1_, boolean p_destroyBlock_2_, CallbackInfoReturnable<Boolean> cir) {
        if (!IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
            IBlockState iblockstate = IMinecraft.mc.theWorld.getBlockState(p_destroyBlock_1_);
            Block block = iblockstate.getBlock();

            IMinecraft.mc.theWorld.playAuxSFX(2001, p_destroyBlock_1_, Block.getStateId(iblockstate));

            if (block.getMaterial() == Material.air) {
                cir.setReturnValue(false);
            } else {
                if (p_destroyBlock_2_) {
                    block.dropBlockAsItem(IMinecraft.mc.theWorld, p_destroyBlock_1_, iblockstate, 0);
                }

                cir.setReturnValue(IMinecraft.mc.theWorld.setBlockState(p_destroyBlock_1_, Blocks.air.getDefaultState(), 3));
            }
        }
    }

    public static void handleBlockPlaceSound(Block shadowBlock, Integer p1metaData, ItemStack p_onItemUse_1_, EntityPlayer p_onItemUse_2_, World p_onItemUse_3_, BlockPos p_onItemUse_4_, EnumFacing p_onItemUse_5_, float p_onItemUse_6_, float p_onItemUse_7_, float p_onItemUse_8_, CallbackInfoReturnable<Boolean> cir) {
        IBlockState iblockstate = p_onItemUse_3_.getBlockState(p_onItemUse_4_);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(p_onItemUse_3_, p_onItemUse_4_)) {
            p_onItemUse_4_ = p_onItemUse_4_.offset(p_onItemUse_5_);
        }

        if (p_onItemUse_1_.stackSize == 0) {
            cir.setReturnValue(false);
        } else if (!p_onItemUse_2_.canPlayerEdit(p_onItemUse_4_, p_onItemUse_5_, p_onItemUse_1_)) {
            cir.setReturnValue(false);
        } else if (p_onItemUse_3_.canBlockBePlaced(shadowBlock, p_onItemUse_4_, false, p_onItemUse_5_, null, p_onItemUse_1_)) {
            int i = p1metaData;
            IBlockState iblockstate1 = shadowBlock.onBlockPlaced(p_onItemUse_3_, p_onItemUse_4_, p_onItemUse_5_, p_onItemUse_6_, p_onItemUse_7_, p_onItemUse_8_, i, p_onItemUse_2_);

            if (p_onItemUse_3_.setBlockState(p_onItemUse_4_, iblockstate1, 3)) {
                iblockstate1 = p_onItemUse_3_.getBlockState(p_onItemUse_4_);

                if (iblockstate1.getBlock() == shadowBlock) {
                    ItemBlock.setTileEntityNBT(p_onItemUse_3_, p_onItemUse_2_, p_onItemUse_4_, p_onItemUse_1_);
                    shadowBlock.onBlockPlacedBy(p_onItemUse_3_, p_onItemUse_4_, iblockstate1, p_onItemUse_2_, p_onItemUse_1_);
                }

                if (!IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
                    IMinecraft.mc.theWorld.playSoundAtPos(p_onItemUse_4_.add(0.5, 0.5, 0.5), shadowBlock.stepSound.getPlaceSound(), (shadowBlock.stepSound.getVolume() + 1.0F) / 2.0F, shadowBlock.stepSound.getFrequency() * 0.8F, false);
                } else {
                    p_onItemUse_3_.playSoundEffect((float) p_onItemUse_4_.getX() + 0.5F, (float) p_onItemUse_4_.getY() + 0.5F, (float) p_onItemUse_4_.getZ() + 0.5F, shadowBlock.stepSound.getPlaceSound(), (shadowBlock.stepSound.getVolume() + 1.0F) / 2.0F, shadowBlock.stepSound.getFrequency() * 0.8F);
                }

                --p_onItemUse_1_.stackSize;
            }

            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }
    }
}
