package net.aspw.viaforgeplus.api;

import net.aspw.viaforgeplus.event.EventTarget;
import net.aspw.viaforgeplus.event.Listenable;
import net.aspw.viaforgeplus.event.MotionEvent;
import net.aspw.viaforgeplus.event.PacketEvent;
import net.aspw.viaforgeplus.network.MinecraftInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class PacketManager extends MinecraftInstance implements Listenable {

    public static float eyeHeight;
    public static float lastEyeHeight;

    @EventTarget
    public void onMotion(MotionEvent event) {
        float START_HEIGHT = 1.62f;
        float END_HEIGHT;

        lastEyeHeight = eyeHeight;

        if (ProtocolFixer.newerThanOrEqualsTo1_9() && ProtocolFixer.olderThanOrEqualsTo1_13_2())
            END_HEIGHT = 1.47f;
        else if (ProtocolFixer.newerThanOrEqualsTo1_14())
            END_HEIGHT = 1.32f;
        else END_HEIGHT = 1.54f;

        float delta;
        if (ProtocolFixer.newerThanOrEqualsTo1_9() && ProtocolFixer.olderThanOrEqualsTo1_13_2())
            delta = 0.147f;
        else if (ProtocolFixer.newerThanOrEqualsTo1_14())
            delta = 0.132f;
        else delta = 0.154f;

        if (mc.thePlayer.isSneaking())
            eyeHeight = AnimationUtils.animate(END_HEIGHT, eyeHeight, 4 * delta);
        else if (eyeHeight < START_HEIGHT)
            eyeHeight = AnimationUtils.animate(START_HEIGHT, eyeHeight, 4 * delta);
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();

        if (ProtocolFixer.newerThanOrEqualsTo1_10()) {
            if (packet instanceof C08PacketPlayerBlockPlacement) {
                ((C08PacketPlayerBlockPlacement) packet).facingX = 0.5F;
                ((C08PacketPlayerBlockPlacement) packet).facingY = 0.5F;
                ((C08PacketPlayerBlockPlacement) packet).facingZ = 0.5F;
            }
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}