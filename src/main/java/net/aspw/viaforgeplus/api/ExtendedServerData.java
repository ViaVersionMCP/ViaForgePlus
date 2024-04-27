package net.aspw.viaforgeplus.api;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

public interface ExtendedServerData {

    ProtocolVersion viaForge$getVersion();

    void viaForge$setVersion(final ProtocolVersion version);
}