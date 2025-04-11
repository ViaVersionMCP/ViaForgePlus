package net.aspw.viaforgeplus.mixin.impl;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.aspw.viaforgeplus.screen.GuiProtocolSelectorScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.realms.RealmsBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDownloadTerrain.class)
public abstract class GuiDownloadTerrainMixin extends GuiScreenMixin {

    @Inject(method = "initGui", at = @At(value = "RETURN"))
    private void injectDisconnectButton(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(1_000_000_000, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel")));
    }

    @Override
    protected void viaForgePlus$actionPerformed(GuiButton p_actionPerformed_1_) {
        if (p_actionPerformed_1_.id == 1_000_000_000) {
            boolean flag = this.mc.isIntegratedServerRunning();
            boolean flag1 = this.mc.isConnectedToRealms();
            p_actionPerformed_1_.enabled = false;

            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);

            if (flag) {
                this.mc.displayGuiScreen(new GuiMainMenu());
            } else if (flag1) {
                RealmsBridge realmsbridge = new RealmsBridge();
                realmsbridge.switchToRealms(new GuiMainMenu());
            } else {
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
        }
    }

    @Override
    protected void viaForgePlus$updateScreen(CallbackInfo ci) {
        if (CommonViaForgePlus.getManager().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_12_2))
            ci.cancel();
    }
}
