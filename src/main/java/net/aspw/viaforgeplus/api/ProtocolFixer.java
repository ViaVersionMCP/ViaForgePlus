package net.aspw.viaforgeplus.api;

import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import com.viaversion.viabackwards.protocol.protocol1_15_2to1_16.Protocol1_15_2To1_16;
import com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.Protocol1_16_4To1_17;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.Protocol1_18_2To1_19;
import com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.Protocol1_19_1To1_19_3;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import com.viaversion.viaversion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import com.viaversion.viaversion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ClientboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ClientboundPackets1_19_1;
import com.viaversion.viaversion.protocols.protocol1_19_3to1_19_1.ClientboundPackets1_19_3;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ClientboundPackets1_19;
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

    /**
     * @author As_pw, toidicakhia
     */
    public static void doFix() {
        // Transaction Fixes
        final Protocol1_16_4To1_17 transaction1_17 = Via.getManager().getProtocolManager().getProtocol(Protocol1_16_4To1_17.class);
        assert transaction1_17 != null;
        transaction1_17.registerClientbound(ClientboundPackets1_17.PING, ClientboundPackets1_16_2.WINDOW_CONFIRMATION, wrapper -> {
        }, true);
        transaction1_17.registerServerbound(ServerboundPackets1_16_2.WINDOW_CONFIRMATION, ServerboundPackets1_17.PONG, wrapper -> {
        }, true);

        // Connection Fixes
        final Protocol1_12_2To1_13 connection1_13 = Via.getManager().getProtocolManager().getProtocol(Protocol1_12_2To1_13.class);
        assert connection1_13 != null;
        connection1_13.registerClientbound(ClientboundPackets1_13.PLAYER_INFO, ClientboundPackets1_12_1.PLAYER_INFO, wrapper -> {
        }, true);

        final Protocol1_15_2To1_16 connection1_16 = Via.getManager().getProtocolManager().getProtocol(Protocol1_15_2To1_16.class);
        assert connection1_16 != null;
        connection1_16.registerClientbound(ClientboundPackets1_16.PLAYER_INFO, ClientboundPackets1_15.PLAYER_INFO, wrapper -> {
        }, true);

        final Protocol1_18_2To1_19 connection1_19 = Via.getManager().getProtocolManager().getProtocol(Protocol1_18_2To1_19.class);
        assert connection1_19 != null;
        connection1_19.registerClientbound(ClientboundPackets1_19.PLAYER_INFO, ClientboundPackets1_18.PLAYER_INFO, wrapper -> {
        }, true);

        final Protocol1_19_1To1_19_3 connection1_19_3 = Via.getManager().getProtocolManager().getProtocol(Protocol1_19_1To1_19_3.class);
        assert connection1_19_3 != null;
        connection1_19_3.registerClientbound(ClientboundPackets1_19_3.PLAYER_INFO_UPDATE, ClientboundPackets1_19_1.PLAYER_INFO, wrapper -> {
        }, true);
        connection1_19_3.registerClientbound(ClientboundPackets1_19_3.PLAYER_INFO_REMOVE, ClientboundPackets1_19_1.PLAYER_INFO, wrapper -> {
        }, true);
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