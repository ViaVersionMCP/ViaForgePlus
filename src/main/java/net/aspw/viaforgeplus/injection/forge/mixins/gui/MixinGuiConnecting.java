package net.aspw.viaforgeplus.injection.forge.mixins.gui;

import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentTranslation;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(GuiConnecting.class)
public abstract class MixinGuiConnecting extends GuiScreen {

    @Shadow
    @Final
    private static Logger logger;
    @Shadow
    @Final
    private static AtomicInteger CONNECTION_ID;
    @Shadow
    private NetworkManager networkManager;
    @Shadow
    private boolean cancel;
    @Shadow
    @Final
    private GuiScreen previousGuiScreen;

    /**
     * @author As_pw
     * @reason Connection Fix
     */
    @Overwrite
    private void connect(final String ip, final int port) {
        logger.info("Authenticating to " + ip + ", " + port);

        new Thread(() -> {
            InetAddress inetaddress = null;

            try {
                if (cancel) {
                    return;
                }

                inetaddress = InetAddress.getByName(ip);
                networkManager = NetworkManager.createNetworkManagerAndConnect(inetaddress, port, mc.gameSettings.isUsingNativeTransport());
                networkManager.setNetHandler(new NetHandlerLoginClient(networkManager, mc, previousGuiScreen));
                networkManager.sendPacket(new C00Handshake(47, ip, port, EnumConnectionState.LOGIN, true));
                networkManager.sendPacket(new C00PacketLoginStart(mc.getSession().getProfile()));
            } catch (UnknownHostException unknownhostexception) {
                if (cancel)
                    return;

                logger.error("Couldn't connect to server", unknownhostexception);
                mc.displayGuiScreen(new GuiDisconnected(previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", "Unknown host")));
            } catch (Exception exception) {
                if (cancel) {
                    return;
                }

                logger.error("Couldn't connect to server", exception);
                String s = exception.toString();

                if (inetaddress != null) {
                    String s1 = inetaddress + ":" + port;
                    s = s.replaceAll(s1, "");
                }

                mc.displayGuiScreen(new GuiDisconnected(previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", s)));
            }
        }, "Server Connector #" + CONNECTION_ID.incrementAndGet()).start();
    }
}