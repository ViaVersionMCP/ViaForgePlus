package net.aspw.viaforgeplus.common.platform;

import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider.GameProfileFetcher;

import java.io.File;

public interface VFPlatform {

    String VERSION = "4.1.0";

    int getGameVersion();

    File getLeadingDirectory();

    void joinServer(final String serverId) throws Throwable;

    GameProfileFetcher getGameProfileFetcher();

}
