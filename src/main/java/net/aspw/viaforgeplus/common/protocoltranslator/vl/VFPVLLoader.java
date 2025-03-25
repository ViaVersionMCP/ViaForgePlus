package net.aspw.viaforgeplus.common.protocoltranslator.vl;

import com.viaversion.vialoader.impl.viaversion.VLLoader;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.protocols.v1_8to1_9.provider.MovementTransmitterProvider;
import net.aspw.viaforgeplus.common.platform.VFPlatform;
import net.aspw.viaforgeplus.common.protocoltranslator.provider.*;
import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.provider.ClassicMPPassProvider;
import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.provider.OldAuthProvider;
import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.provider.EncryptionProvider;
import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider.GameProfileFetcher;

public class VFPVLLoader extends VLLoader {

    private final VFPlatform platform;

    public VFPVLLoader(VFPlatform platform) {
        this.platform = platform;
    }

    @Override
    public void load() {
        super.load();

        final ViaProviders providers = Via.getManager().getProviders();

        providers.use(VersionProvider.class, new VFPVersionProvider());
        providers.use(MovementTransmitterProvider.class, new VFPMovementTransmitterProvider());
        providers.use(OldAuthProvider.class, new VFPOldAuthProvider());
        providers.use(GameProfileFetcher.class, platform.getGameProfileFetcher());
        providers.use(EncryptionProvider.class, new VFPEncryptionProvider());
        providers.use(ClassicMPPassProvider.class, new VFPClassicMPPassProvider());
    }
    
}
