package net.aspw.viaforgeplus.vfphooks;

import com.viaversion.viabackwards.protocol.v1_11to1_10.Protocol1_11To1_10;
import com.viaversion.viarewind.protocol.v1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ServerboundPackets1_9;
import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnderEye;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

public class VersionDiffPatches {

    public static void blockBreakUnderThan1_7Hook(boolean leftClick, CallbackInfo ci) {
        ci.cancel();

        if (!leftClick)
            IMinecraft.mc.leftClickCounter = 0;

        if (IMinecraft.mc.leftClickCounter <= 0) {
            if (leftClick && IMinecraft.mc.objectMouseOver != null && IMinecraft.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockPos = IMinecraft.mc.objectMouseOver.getBlockPos();

                if (IMinecraft.mc.thePlayer.isUsingItem() && (CommonViaForgePlus.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_8) || IMinecraft.mc.isSingleplayer())) return;

                if (IMinecraft.mc.theWorld.getBlockState(blockPos).getBlock().getMaterial() != Material.air && IMinecraft.mc.playerController.onPlayerDamageBlock(blockPos, IMinecraft.mc.objectMouseOver.sideHit)) {
                    IMinecraft.mc.effectRenderer.addBlockHitEffects(blockPos, IMinecraft.mc.objectMouseOver.sideHit);
                    IMinecraft.mc.thePlayer.swingItem();
                }
            } else {
                IMinecraft.mc.playerController.resetBlockRemoving();
            }
        }
    }

    public static void fixedAttackOrder(CallbackInfo ci) {
        ci.cancel();

        if (IMinecraft.mc.leftClickCounter <= 0) {
            if (IMinecraft.mc.objectMouseOver != null && Objects.requireNonNull(IMinecraft.mc.objectMouseOver.typeOfHit) != MovingObjectPosition.MovingObjectType.ENTITY) {
                IMinecraft.mc.thePlayer.swingItem();
            }

            if (IMinecraft.mc.objectMouseOver != null) {
                switch (IMinecraft.mc.objectMouseOver.typeOfHit) {
                    case ENTITY:
                        sendFixedAttack(IMinecraft.mc.thePlayer, IMinecraft.mc.objectMouseOver.entityHit);
                        break;

                    case BLOCK:
                        BlockPos blockpos = IMinecraft.mc.objectMouseOver.getBlockPos();

                        if (IMinecraft.mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                            IMinecraft.mc.playerController.clickBlock(blockpos, IMinecraft.mc.objectMouseOver.sideHit);
                            break;
                        }

                    case MISS:
                    default:
                        if (IMinecraft.mc.playerController.isNotCreative()) {
                            IMinecraft.mc.leftClickCounter = 10;
                        }
                }
            }
        }
    }

    private static void sendFixedAttack(final EntityPlayer entityIn, final Entity target) {
        if (!IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
            IMinecraft.mc.playerController.attackEntity(entityIn, target);
            IMinecraft.mc.thePlayer.swingItem();
        } else {
            IMinecraft.mc.thePlayer.swingItem();
            IMinecraft.mc.playerController.attackEntity(entityIn, target);
        }
    }

    public static void handlePacket(final Packet<?> packet, final CallbackInfo ci) {
        if (!IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThan(ProtocolVersion.v1_8) && packet instanceof C0APacketAnimation) {
            ci.cancel();
            PacketWrapper fixedC0A = PacketWrapper.create(ServerboundPackets1_9.SWING, Via.getManager().getConnectionManager().getConnections().iterator().next());
            fixedC0A.write(Types.VAR_INT, 0);
            fixedC0A.sendToServer(Protocol1_9To1_8.class);
        }

        if (!IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_16) && (packet instanceof C08PacketPlayerBlockPlacement && IMinecraft.mc.theWorld.getBlockState(((C08PacketPlayerBlockPlacement) packet).getPosition()).getBlock() instanceof BlockAir && (((C08PacketPlayerBlockPlacement) packet).getStack().getItem() instanceof ItemSnowball || ((C08PacketPlayerBlockPlacement) packet).getStack().getItem() instanceof ItemEnderPearl || ((C08PacketPlayerBlockPlacement) packet).getStack().getItem() instanceof ItemEnderEye || ((C08PacketPlayerBlockPlacement) packet).getStack().getItem() instanceof ItemExpBottle || (((C08PacketPlayerBlockPlacement) packet).getStack().getMetadata() & 0x4000) != 0) || packet instanceof C0EPacketClickWindow && (((C0EPacketClickWindow) packet).getMode() == 4 || ((C0EPacketClickWindow) packet).getSlotId() == -999) || packet instanceof C07PacketPlayerDigging && IMinecraft.mc.thePlayer.getHeldItem() != null && (((C07PacketPlayerDigging) packet).getStatus() == C07PacketPlayerDigging.Action.DROP_ITEM || ((C07PacketPlayerDigging) packet).getStatus() == C07PacketPlayerDigging.Action.DROP_ALL_ITEMS))) {
            PacketWrapper swingPacket = PacketWrapper.create(ServerboundPackets1_9.SWING, Via.getManager().getConnectionManager().getConnections().iterator().next());
            swingPacket.write(Types.VAR_INT, 0);
            swingPacket.sendToServer(Protocol1_9To1_8.class);
        }

        if (!IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_11) && packet instanceof C08PacketPlayerBlockPlacement && ((C08PacketPlayerBlockPlacement) packet).getPlacedBlockDirection() != 255) {
            ci.cancel();
            PacketWrapper fixedC08 = PacketWrapper.create(ServerboundPackets1_9.USE_ITEM_ON, Via.getManager().getConnectionManager().getConnections().iterator().next());
            fixedC08.write(Types.BLOCK_POSITION1_8, new BlockPosition(((C08PacketPlayerBlockPlacement) packet).getPosition().getX(), ((C08PacketPlayerBlockPlacement) packet).getPosition().getY(), ((C08PacketPlayerBlockPlacement) packet).getPosition().getZ()));
            fixedC08.write(Types.VAR_INT, ((C08PacketPlayerBlockPlacement) packet).getPlacedBlockDirection());
            fixedC08.write(Types.VAR_INT, 0);
            fixedC08.write(Types.FLOAT, ((C08PacketPlayerBlockPlacement) packet).facingX);
            fixedC08.write(Types.FLOAT, ((C08PacketPlayerBlockPlacement) packet).facingY);
            fixedC08.write(Types.FLOAT, ((C08PacketPlayerBlockPlacement) packet).facingZ);
            fixedC08.sendToServer(Protocol1_11To1_10.class);
        }
    }

    public static void pushOutHook(CallbackInfoReturnable<Boolean> cir) {
        if (shouldSwimOrCrawl() || !IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13) && IMinecraft.mc.thePlayer.isSneaking())
            cir.setReturnValue(false);
    }

    private static boolean isUnderWater() {
        final World world = IMinecraft.mc.thePlayer.getEntityWorld();
        double eyeBlock = IMinecraft.mc.thePlayer.posY + (double) IMinecraft.mc.thePlayer.getEyeHeight() - 0.25;
        BlockPos blockPos = new BlockPos(IMinecraft.mc.thePlayer.posX, eyeBlock, IMinecraft.mc.thePlayer.posZ);

        return world.getBlockState(blockPos).getBlock().getMaterial() == Material.water && !(IMinecraft.mc.thePlayer.ridingEntity instanceof EntityBoat);
    }

    private static boolean canSwim() {
        return !IMinecraft.mc.thePlayer.isSneaking() && IMinecraft.mc.thePlayer.isInWater() && IMinecraft.mc.thePlayer.isSprinting() && isUnderWater();
    }

    public static boolean shouldSwimOrCrawl() {
        AxisAlignedBB box = IMinecraft.mc.thePlayer.getEntityBoundingBox();
        AxisAlignedBB crawlBB = new AxisAlignedBB(box.minX, box.minY + 0.9, box.minZ, box.minX + 0.6, box.minY + 1.5, box.minZ + 0.6);

        return !IMinecraft.mc.isSingleplayer() && CommonViaForgePlus.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13) && (canSwim() || !IMinecraft.mc.theWorld.getCollisionBoxes(crawlBB).isEmpty());
    }
}
