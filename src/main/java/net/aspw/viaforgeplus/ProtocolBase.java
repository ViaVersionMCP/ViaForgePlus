package net.aspw.viaforgeplus;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.AttributeKey;
import net.aspw.viaforgeplus.api.*;
import net.raphimc.vialoader.ViaLoader;
import net.raphimc.vialoader.impl.platform.ViaBackwardsPlatformImpl;
import net.raphimc.vialoader.impl.platform.ViaLegacyPlatformImpl;
import net.raphimc.vialoader.impl.platform.ViaRewindPlatformImpl;
import net.raphimc.vialoader.impl.platform.ViaVersionPlatformImpl;
import net.raphimc.vialoader.netty.CompressionReorderEvent;

import java.util.ArrayList;
import java.util.List;

public class ProtocolBase {

    private ProtocolVersion targetVersion = ProtocolVersion.v1_8;
    public static final AttributeKey<UserConnection> LOCAL_VIA_USER = AttributeKey.valueOf("local_via_user");
    public static final AttributeKey<VFNetworkManager> VF_NETWORK_MANAGER = AttributeKey.valueOf("encryption_setup");
    private static ProtocolBase manager;
    public static List<ProtocolVersion> versions = new ArrayList<>();

    public ProtocolBase() {
    }

    public static void init(final VFPlatform platform) {
        if (manager != null) {
            return;
        }

        manager = new ProtocolBase();

        ViaLoader.init(new ViaVersionPlatformImpl(null), new ProtocolVLLoader(platform), new ProtocolVLInjector(), null, ViaBackwardsPlatformImpl::new, ViaRewindPlatformImpl::new, ViaLegacyPlatformImpl::new, null);

        versions.addAll(ProtocolVersion.getProtocols());

        versions.removeIf(i -> i == ProtocolVersion.unknown || i.olderThan(ProtocolVersion.v1_7_2));

        //ProtocolFixer.doFix();
    }

    public void inject(final Channel channel, final VFNetworkManager networkManager) {
        if (channel instanceof SocketChannel) {
            final UserConnection user = new UserConnectionImpl(channel, true);
            new ProtocolPipelineImpl(user);

            channel.attr(LOCAL_VIA_USER).set(user);
            channel.attr(VF_NETWORK_MANAGER).set(networkManager);

            channel.pipeline().addLast(new ProtocolVLLegacyPipeline(user, targetVersion));
        }
    }

    public ProtocolVersion getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersionSilent(final ProtocolVersion targetVersion) {
        this.targetVersion = targetVersion;
    }

    public void setTargetVersion(final ProtocolVersion targetVersion) {
        this.targetVersion = targetVersion;
    }

    public void reorderCompression(final Channel channel) {
        channel.pipeline().fireUserEventTriggered(CompressionReorderEvent.INSTANCE);
    }

    public static ProtocolBase getManager() {
        return manager;
    }
}
