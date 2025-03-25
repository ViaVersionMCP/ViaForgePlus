package net.aspw.viaforgeplus.common.platform;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class VersionTracker {

    public static final Map<InetAddress, ProtocolVersion> SERVER_PROTOCOL_VERSIONS = new HashMap<>();

    public static void storeServerProtocolVersion(InetAddress address, ProtocolVersion version) {
        SERVER_PROTOCOL_VERSIONS.put(address, version);
        CommonViaForgePlus.getManager().setTargetVersionSilent(version);
    }

    public static ProtocolVersion getServerProtocolVersion(InetAddress address) {
        return SERVER_PROTOCOL_VERSIONS.remove(address);
    }

}
