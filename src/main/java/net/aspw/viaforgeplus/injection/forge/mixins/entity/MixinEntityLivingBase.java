package net.aspw.viaforgeplus.injection.forge.mixins.entity;

import net.aspw.viaforgeplus.api.ProtocolFixer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

    public MixinEntityLivingBase(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @ModifyConstant(method = "onLivingUpdate", constant = @Constant(doubleValue = 0.005D))
    private double onLivingUpdate(double constant) {
        if (ProtocolFixer.newerThan1_8())
            return 0.003D;
        return 0.005D;
    }
}