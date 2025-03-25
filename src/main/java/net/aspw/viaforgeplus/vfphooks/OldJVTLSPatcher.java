package net.aspw.viaforgeplus.vfphooks;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class OldJvTLSPatcher {

    public final TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }
    };

    public final SSLContext sslContext;

    public OldJvTLSPatcher() throws Exception {
        sslContext = SSLContext.getInstance("TLS");
    }

    public void patchTLS() throws Exception {
        sslContext.init(null, trustAllCerts, new SecureRandom());
    }
}
