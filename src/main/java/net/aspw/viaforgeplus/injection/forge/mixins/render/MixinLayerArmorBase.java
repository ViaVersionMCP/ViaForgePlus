package net.aspw.viaforgeplus.injection.forge.mixins.render;

import net.aspw.viaforgeplus.api.McUpdatesHandler;
import net.aspw.viaforgeplus.api.ProtocolFixer;
import net.aspw.viaforgeplus.network.MinecraftInstance;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LayerArmorBase.class})
public class MixinLayerArmorBase {
    @Inject(method = {"doRenderLayer"}, at = {@At("HEAD")}, cancellable = true)
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo ci) {
        if (ProtocolFixer.newerThanOrEqualsTo1_13() && McUpdatesHandler.shouldAnimation() && entitylivingbaseIn == MinecraftInstance.mc.thePlayer)
            ci.cancel();
    }
}