package net.aspw.viaforgeplus.common.protocoltranslator.platform;

import com.viaversion.vialoader.impl.platform.ViaVersionPlatformImpl;
import net.aspw.viaforgeplus.common.platform.VFPlatform;
import java.io.File;

public final class VFPViaVersionPlatformImpl extends ViaVersionPlatformImpl {

    public VFPViaVersionPlatformImpl(final File rootFolder) {
        super(rootFolder);
    }

    @Override
    public String getPlatformName() {
        return "ViaForgePlus";
    }

    @Override
    public String getPlatformVersion() {
        return VFPlatform.VERSION;
    }

}
