package net.aspw.viaforgeplus.mixin.impl;

import net.aspw.viaforgeplus.vfphooks.MotionFixes;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @ModifyConstant(method = "getCollisionBorderSize", constant = @Constant(floatValue = 0.1F))
    private float handleHitBoxSize(float constant) {
        return MotionFixes.handleHitBoxSize();
    }
}
