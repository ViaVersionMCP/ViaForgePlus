package net.aspw.viaforgeplus.mixin.impl;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.common.gui.ExtendedServerData;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerData.class)
public class ServerDataMixin implements ExtendedServerData {

    @Unique
    private ProtocolVersion viaForgePlus$version;

    @Inject(method = "getNBTCompound", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;setString(Ljava/lang/String;Ljava/lang/String;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    public void saveVersion(CallbackInfoReturnable<NBTTagCompound> cir, NBTTagCompound nbttagcompound) {
        if (viaForgePlus$version != null) {
            nbttagcompound.setString("viaForgePlus$version", viaForgePlus$version.getName());
        }
    }

    @Inject(method = "getServerDataFromNBTCompound", at = @At(value = "TAIL"))
    private static void getVersion(NBTTagCompound nbtCompound, CallbackInfoReturnable<ServerData> cir) {
        if (nbtCompound.hasKey("viaForgePlus$version")) {
            ((ExtendedServerData) cir.getReturnValue()).viaForgePlus$setVersion(ProtocolVersion.getClosest(nbtCompound.getString("viaForgePlus$version")));
        }
    }

    @Inject(method = "copyFrom", at = @At("HEAD"))
    public void track(ServerData serverDataIn, CallbackInfo ci) {
        if (serverDataIn instanceof ExtendedServerData) {
            viaForgePlus$version = ((ExtendedServerData) serverDataIn).viaForgePlus$getVersion();
        }
    }

    @Override
    public ProtocolVersion viaForgePlus$getVersion() {
        return viaForgePlus$version;
    }

    @Override
    public void viaForgePlus$setVersion(ProtocolVersion version) {
        viaForgePlus$version = version;
    }
}
