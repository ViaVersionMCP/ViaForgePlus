package net.aspw.viaforgeplus;

import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.aspw.viaforgeplus.common.platform.VFPlatform;
import net.aspw.viaforgeplus.common.protocoltranslator.fetcher.VFPGameProfileFetcher;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.Mod;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider.GameProfileFetcher;

import java.io.File;

@Mod(modid = "viaforgeplus", name = "ViaForgePlus", acceptableRemoteVersions = "*", clientSideOnly=true, useMetadata=true)
public class ViaForgePlus implements VFPlatform {

    public void initVFPlatform() {
        CommonViaForgePlus.init(this);
    }

    @Override
    public int getGameVersion() {
        return RealmsSharedConstants.NETWORK_PROTOCOL_VERSION;
    }

    @Override
    public File getLeadingDirectory() {
        return IMinecraft.mc.mcDataDir;
    }

    @Override
    public void joinServer(String serverId) throws Throwable {
        final Session session = IMinecraft.mc.getSession();

        IMinecraft.mc.getSessionService().joinServer(session.getProfile(), session.getToken(), serverId);
    }

    @Override
    public GameProfileFetcher getGameProfileFetcher() {
        return new VFPGameProfileFetcher();
    }
}
