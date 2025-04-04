package net.aspw.viaforgeplus.common;

import com.viaversion.vialoader.ViaLoader;
import com.viaversion.vialoader.impl.platform.*;
import com.viaversion.vialoader.netty.CompressionReorderEvent;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.connection.ConnectionDetails;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import net.aspw.viaforgeplus.IMinecraft;
import net.aspw.viaforgeplus.ViaForgePlus;
import net.aspw.viaforgeplus.common.platform.VFPlatform;
import net.aspw.viaforgeplus.common.platform.ViaForgeConfig;
import net.aspw.viaforgeplus.common.protocoltranslator.vl.VFPVLInjector;
import net.aspw.viaforgeplus.common.protocoltranslator.vl.VFPVLLoader;
import net.aspw.viaforgeplus.common.protocoltranslator.netty.VFPNetworkManager;
import net.aspw.viaforgeplus.common.protocoltranslator.netty.ViaForgeVLLegacyPipeline;
import net.aspw.viaforgeplus.common.protocoltranslator.platform.VFPViaVersionPlatformImpl;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.aspw.viaforgeplus.vfphooks.UpdatesChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CommonViaForgePlus {

    public static final AttributeKey<UserConnection> LOCAL_VIA_USER = AttributeKey.valueOf("local_via_user");
    public static final AttributeKey<VFPNetworkManager> VF_NETWORK_MANAGER = AttributeKey.valueOf("encryption_setup");

    private static CommonViaForgePlus manager;
    private static ViaForgePlus vfp;

    private final VFPlatform platform;
    private ProtocolVersion targetVersion;

    private ViaForgeConfig config;

    public static String version = "alphaAntiLeak22";
    public static boolean isModLatest = false;

    public CommonViaForgePlus(VFPlatform platform) {
        this.platform = platform;
    }

    public static void init(final VFPlatform platform) {
        final ProtocolVersion version = ProtocolVersion.getProtocol(platform.getGameVersion());

        manager = new CommonViaForgePlus(platform);
        vfp = new ViaForgePlus();

        final File mainFolder = new File(platform.getLeadingDirectory(), "ViaForgePlus");

        ViaLoader.init(new VFPViaVersionPlatformImpl(mainFolder), new VFPVLLoader(platform), new VFPVLInjector(), null, ViaBackwardsPlatformImpl::new, ViaRewindPlatformImpl::new, ViaLegacyPlatformImpl::new, ViaAprilFoolsPlatformImpl::new);

        manager.config = new ViaForgeConfig(new File(mainFolder, "viaforgeplus.yml"), Via.getPlatform().getLogger());

        final ProtocolVersion configVersion = ProtocolVersion.getClosest(manager.config.getClientSideVersion());
        manager.setTargetVersion(configVersion != null ? configVersion : version);

        UpdatesChecker.check();
    }

    public void inject(final Channel channel, final VFPNetworkManager networkManager) {
        if (getTargetVersion().equals(getNativeVersion())) return;

        channel.attr(VF_NETWORK_MANAGER).set(networkManager);

        final UserConnection user = new UserConnectionImpl(channel, true);
        new ProtocolPipelineImpl(user);

        channel.attr(LOCAL_VIA_USER).set(user);

        channel.pipeline().addLast(new ViaForgeVLLegacyPipeline(user, targetVersion));
    }

    public void reorderCompression(final Channel channel) {
        channel.pipeline().fireUserEventTriggered(CompressionReorderEvent.INSTANCE);
    }

    public ProtocolVersion getNativeVersion() {
        return ProtocolVersion.getProtocol(platform.getGameVersion());
    }

    public ProtocolVersion getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(final ProtocolVersion targetVersion) {
        if (targetVersion == null) {
            throw new IllegalArgumentException("Target version cannot be null");
        }
        this.targetVersion = targetVersion;
        config.setClientSideVersion(targetVersion.getName());
    }

    public VFPlatform getPlatform() {
        return platform;
    }

    public ViaForgeConfig getConfig() {
        return config;
    }

    public static CommonViaForgePlus getManager() {
        return manager;
    }

    public static ViaForgePlus getVfp() {
        return vfp;
    }
}
