package net.aspw.viaforgeplus.common.protocoltranslator.provider;

import com.viaversion.viaversion.api.connection.UserConnection;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.provider.OldAuthProvider;

public class VFPOldAuthProvider extends OldAuthProvider {

    @Override
    public void sendAuthRequest(UserConnection user, String serverId) throws Throwable {
        final CommonViaForgePlus common = CommonViaForgePlus.getManager();
        if (!common.getConfig().isVerifySessionInOldVersions()) {
            return;
        }
        common.getPlatform().joinServer(serverId);
    }

}
