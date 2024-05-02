package net.aspw.viaforgeplus.api;

import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.Protocol1_16_4To1_17;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import net.aspw.viaforgeplus.ProtocolBase;
import net.aspw.viaforgeplus.network.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

public class ProtocolFixer {
    private static final Minecraft mc = MinecraftInstance.mc;

    public static void sendConditionalSwing(final MovingObjectPosition mop) {
        if (mop != null && mop.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
            mc.thePlayer.swingItem();
        }
    }

    public static void sendFixedAttack(final EntityPlayer entityIn, final Entity target) {
        if (newerThan1_8()) {
            mc.playerController.attackEntity(entityIn, target);
            mc.thePlayer.swingItem();
        } else {
            mc.thePlayer.swingItem();
            mc.playerController.attackEntity(entityIn, target);
        }
    }

    public static void transactionFix1_17() {
        final Protocol1_16_4To1_17 protocol = Via.getManager().getProtocolManager().getProtocol(Protocol1_16_4To1_17.class);
        assert protocol != null;
        protocol.registerClientbound(ClientboundPackets1_17.PING, ClientboundPackets1_16_2.WINDOW_CONFIRMATION, wrapper -> {}, true);
        protocol.registerServerbound(ServerboundPackets1_16_2.WINDOW_CONFIRMATION, ServerboundPackets1_17.PONG, wrapper -> {}, true);
    }

    public static boolean newerThanOrEqualsTo1_8() {
        return ProtocolBase.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_8) && !MinecraftInstance.mc.isIntegratedServerRunning() || MinecraftInstance.mc.isIntegratedServerRunning();
    }

    public static boolean newerThan1_8() {
        return ProtocolBase.getManager().getTargetVersion().newerThan(ProtocolVersion.v1_8) && !MinecraftInstance.mc.isIntegratedServerRunning();
    }

    public static boolean newerThanOrEqualsTo1_9() {
        return ProtocolBase.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_9) && !MinecraftInstance.mc.isIntegratedServerRunning();
    }

    public static boolean newerThanOrEqualsTo1_13() {
        return ProtocolBase.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13) && !MinecraftInstance.mc.isIntegratedServerRunning();
    }

    public static boolean olderThanOrEqualsTo1_13_2() {
        return ProtocolBase.getManager().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_13_2) && !MinecraftInstance.mc.isIntegratedServerRunning();
    }

    public static boolean newerThanOrEqualsTo1_14() {
        return ProtocolBase.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_14) && !MinecraftInstance.mc.isIntegratedServerRunning();
    }

    public static boolean newerThanOrEqualsTo1_16() {
        return ProtocolBase.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_14) && !MinecraftInstance.mc.isIntegratedServerRunning();
    }

    public static boolean newerThanOrEqualsTo1_17() {
        return ProtocolBase.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_17) && !MinecraftInstance.mc.isIntegratedServerRunning();
    }
}