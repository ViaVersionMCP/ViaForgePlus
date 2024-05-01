package net.aspw.viaforgeplus.injection.forge.mixins.packets;

import net.aspw.viaforgeplus.api.ProtocolFixer;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(S32PacketConfirmTransaction.class)
public class MixinS32PacketConfirmTransaction {

	@Shadow
	private short actionNumber;

	@Shadow
	private boolean field_148893_c;

	@Shadow
	private int windowId;

	/**
     * @author FlorianMichael
     * @reason 1.17+ transaction fix
     */

	@Overwrite
	public void readPacketData(PacketBuffer buf) {
	    if (ProtocolFixer.newerThanOrEqualsTo1_17()) {
	        this.windowId = buf.readInt();
	    } else {
	        this.windowId = buf.readUnsignedByte();
	        this.actionNumber = buf.readShort();
	        this.field_148893_c = buf.readBoolean();
	    }
	}
}