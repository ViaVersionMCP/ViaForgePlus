package net.aspw.viaforgeplus.injection.forge.mixins.gui;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.ProtocolBase;
import net.aspw.viaforgeplus.api.ProtocolSelector;
import net.aspw.viaforgeplus.network.APIConnecter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends GuiScreen {

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        buttonList.add(new GuiButton(1151, 4, height - 24, 68, 20, "Protocol"));
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void drawScreen(CallbackInfo callbackInfo) {
        final ProtocolVersion version = ProtocolBase.getManager().getTargetVersion();
        if (APIConnecter.INSTANCE.isLatest())
            this.fontRendererObj.drawStringWithShadow("§7ViaForgePlus is Latest!", 6f, height - 46, 0xffffff);
        else
            this.fontRendererObj.drawStringWithShadow("§7ViaForgePlus Update is Available!", 6f, height - 46, 0xffffff);
        this.fontRendererObj.drawStringWithShadow("§7Current Protocol: §d" + version.getName(), 6f, height - 35, 0xffffff);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        if (button.id == 1151) {
            mc.displayGuiScreen(new ProtocolSelector(this));
        }
    }
}