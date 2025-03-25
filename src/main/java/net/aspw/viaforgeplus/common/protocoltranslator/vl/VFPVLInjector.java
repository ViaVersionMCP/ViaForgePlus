package net.aspw.viaforgeplus.common.protocoltranslator.vl;

import com.viaversion.vialoader.impl.viaversion.VLInjector;
import com.viaversion.vialoader.netty.VLLegacyPipeline;

public class VFPVLInjector extends VLInjector {

    @Override
    public String getDecoderName() {
        return VLLegacyPipeline.VIA_DECODER_NAME;
    }

    @Override
    public String getEncoderName() {
        return VLLegacyPipeline.VIA_ENCODER_NAME;
    }
    
}
