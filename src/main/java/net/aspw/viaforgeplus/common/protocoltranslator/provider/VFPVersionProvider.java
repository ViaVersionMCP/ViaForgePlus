package net.aspw.viaforgeplus.common.protocoltranslator.provider;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.version.BaseVersionProvider;
import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import java.util.logging.Logger;

public class VFPVersionProvider extends BaseVersionProvider {

    @Override
    public ProtocolVersion getClosestServerProtocol(UserConnection connection) throws Exception {
        if (!IMinecraft.mc.isSingleplayer()) {
            return CommonViaForgePlus.getManager().getTargetVersion();
        }
        return super.getClosestServerProtocol(connection);
    }
    
}
