package net.aspw.viaforgeplus.common.protocoltranslator.provider;

import com.viaversion.viaversion.api.connection.UserConnection;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.provider.EncryptionProvider;

public class VFPEncryptionProvider extends EncryptionProvider {

    @Override
    public void enableDecryption(UserConnection user) {
        user.getChannel().attr(CommonViaForgePlus.VF_NETWORK_MANAGER).getAndRemove().viaForgePlus$setupPreNettyDecryption();
    }

}
