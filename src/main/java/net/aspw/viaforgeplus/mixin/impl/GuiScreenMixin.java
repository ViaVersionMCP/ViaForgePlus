package net.aspw.viaforgeplus.mixin.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin {

    @Shadow
    public Minecraft mc;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    protected List<GuiButton> buttonList;

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    protected void injectActionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        this.viaForgePlus$actionPerformed(button);
    }

    @Unique
    protected void viaForgePlus$actionPerformed(GuiButton button) {
    }
}
