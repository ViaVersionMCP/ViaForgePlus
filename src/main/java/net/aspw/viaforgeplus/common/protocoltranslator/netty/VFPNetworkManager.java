package net.aspw.viaforgeplus.common.protocoltranslator.netty;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

public interface VFPNetworkManager {
    void viaForgePlus$setupPreNettyDecryption();

    ProtocolVersion viaForgePlus$getTrackedVersion();

    void viaForgePlus$setTrackedVersion(final ProtocolVersion version);

}
