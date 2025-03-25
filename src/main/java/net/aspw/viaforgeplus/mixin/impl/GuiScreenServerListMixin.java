package net.aspw.viaforgeplus.mixin.impl;

import com.viaversion.viaversion.util.Pair;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.aspw.viaforgeplus.common.platform.ViaForgeConfig;
import net.aspw.viaforgeplus.screen.GuiProtocolSelectorScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenServerList.class)
public class GuiScreenServerListMixin extends GuiScreen {

    @Inject(method = "initGui", at = @At("RETURN"))
    public void hookViaForgePlusButton(CallbackInfo ci) {
        buttonList.add(new GuiButton(1_000_000_000, 4, this.height - 24, 88, 20, "ViaForgePlus"));
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    public void handleViaForgePlusButtonClicking(GuiButton p_actionPerformed_1_, CallbackInfo ci) {
        if (p_actionPerformed_1_.id == 1_000_000_000) {
            mc.displayGuiScreen(new GuiProtocolSelectorScreen(this));
        }
    }
    
}
