package net.aspw.viaforgeplus.mixin.impl;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.minecraft.client.gui.GuiOverlayDebug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiOverlayDebug.class)
public class GuiOverlayDebugMixin {

    @Inject(method = "getDebugInfoRight", at = @At(value = "TAIL"))
    public void addProtocolVersionToList(CallbackInfoReturnable<List<String>> cir) {
        final CommonViaForgePlus common = CommonViaForgePlus.getManager();
        final ProtocolVersion version = CommonViaForgePlus.getManager().getTargetVersion();

        if (common.getConfig().isShowProtocolVersionInF3()) {
            cir.getReturnValue().add("");
            String renderVersion = IMinecraft.mc.isSingleplayer() ? "Disabled" : version.getName();
            cir.getReturnValue().add("ViaForgePlus: " + renderVersion);
        }
    }

}
