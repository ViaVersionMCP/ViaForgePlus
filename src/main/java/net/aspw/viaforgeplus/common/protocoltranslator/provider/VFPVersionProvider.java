package net.aspw.viaforgeplus.common.protocoltranslator.provider;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.version.BaseVersionProvider;
import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;

public class VFPVersionProvider extends BaseVersionProvider {

    @Override
    public ProtocolVersion getClosestServerProtocol(UserConnection connection) throws Exception {
        if (connection.isClientSide() && !IMinecraft.mc.isSingleplayer()) {
            return connection.getChannel().attr(CommonViaForgePlus.VF_NETWORK_MANAGER).get().viaForgePlus$getTrackedVersion();
        }
        return super.getClosestServerProtocol(connection);
    }
    
}
