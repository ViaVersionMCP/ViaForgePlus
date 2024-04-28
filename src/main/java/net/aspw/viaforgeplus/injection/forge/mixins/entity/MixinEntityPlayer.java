package net.aspw.viaforgeplus.injection.forge.mixins.entity;

import net.aspw.viaforgeplus.api.McUpdatesHandler;
import net.aspw.viaforgeplus.api.PacketManager;
import net.aspw.viaforgeplus.api.ProtocolFixer;
import net.aspw.viaforgeplus.network.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends MixinEntityLivingBase {

    public MixinEntityPlayer(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @Shadow
    public abstract boolean isPlayerSleeping();

    private final ItemStack[] mainInventory = new ItemStack[36];
    private final ItemStack[] armorInventory = new ItemStack[4];

    /**
     * @author As_pw
     * @reason Eye Height Fix
     */
    @Overwrite
    public float getEyeHeight() {
        final Minecraft mc = MinecraftInstance.mc;
        if (ProtocolFixer.newerThanOrEqualsTo1_13() && McUpdatesHandler.doingEyeRot)
            return McUpdatesHandler.lastEyeHeight + (McUpdatesHandler.eyeHeight - McUpdatesHandler.lastEyeHeight) * mc.timer.renderPartialTicks;
        if (this.isPlayerSleeping())
            return 0.2F;
        return PacketManager.lastEyeHeight + (PacketManager.eyeHeight - PacketManager.lastEyeHeight) * mc.timer.renderPartialTicks;
    }

    /**
     * @author As_pw
     * @reason 1.16+ Item Drop Fix
     */
    @Inject(method = "dropItem", at = @At("HEAD"))
    private void dropItem(ItemStack p_dropItem_1_, boolean p_dropItem_2_, boolean p_dropItem_3_, CallbackInfoReturnable<EntityItem> cir) {
        for (int i = 0; i < this.mainInventory.length; ++i) {
            if (ProtocolFixer.newerThanOrEqualsTo1_16())
                MinecraftInstance.mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
            if (this.mainInventory[i] != null) {
                this.mainInventory[i] = null;
            }
        }

        for (int j = 0; j < this.armorInventory.length; ++j) {
            if (ProtocolFixer.newerThanOrEqualsTo1_16())
                MinecraftInstance.mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
            if (this.armorInventory[j] != null) {
                this.armorInventory[j] = null;
            }
        }
    }
}