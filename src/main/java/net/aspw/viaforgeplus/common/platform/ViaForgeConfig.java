package net.aspw.viaforgeplus.common.platform;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.util.Config;
import com.viaversion.viaversion.util.Pair;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ViaForgeConfig extends Config {

    public static final String CLIENT_SIDE_VERSION = "client-side-version";
    public static final String VERIFY_SESSION_IN_OLD_VERSIONS = "verify-session-in-old-versions";
    public static final String ALLOW_BETACRAFT_AUTHENTICATION = "allow-betacraft-authentication";
    public static final String SHOW_PROTOCOL_VERSION_IN_F3 = "show-protocol-version-in-f3";

    public ViaForgeConfig(File configFile, Logger logger) {
        super(configFile, logger);
        reload();
    }

    @Override
    public URL getDefaultConfigURL() {
        return getClass().getClassLoader().getResource("assets/viaforgeplus/config.yml");
    }

    @Override
    protected void handleConfig(Map<String, Object> config) {
    }

    @Override
    public List<String> getUnsupportedOptions() {
        return Collections.emptyList();
    }

    @Override
    public void set(String path, Object value) {
        super.set(path, value);
        save();
    }

    public String getClientSideVersion() {
        if (getInt(CLIENT_SIDE_VERSION, -1) != -1) {
            return ProtocolVersion.getProtocol(getInt(CLIENT_SIDE_VERSION, -1)).getName();
        }
        return getString(CLIENT_SIDE_VERSION, "");
    }

    public void setClientSideVersion(final String version) {
        set(CLIENT_SIDE_VERSION, version);
    }

    public boolean isVerifySessionInOldVersions() {
        return getBoolean(VERIFY_SESSION_IN_OLD_VERSIONS, true);
    }

    public boolean isAllowBetacraftAuthentication() {
        return getBoolean(ALLOW_BETACRAFT_AUTHENTICATION, true);
    }

    public boolean isShowProtocolVersionInF3() {
        return getBoolean(SHOW_PROTOCOL_VERSION_IN_F3, true);
    }
}
