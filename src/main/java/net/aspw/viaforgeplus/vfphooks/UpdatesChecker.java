package net.aspw.viaforgeplus.vfphooks;

import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class UpdatesChecker {

    public static void check() {
        try {
            OldJvTLSPatcher tlsPatcher = new OldJvTLSPatcher();

            tlsPatcher.patchTLS();

            OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(tlsPatcher.sslContext.getSocketFactory(), (X509TrustManager) tlsPatcher.trustAllCerts[0])
                .build();

            Request request = new Request.Builder()
                .url("https://nattogreatapi.pages.dev/ViaForgePlus/database/latest_version.txt")
                .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.body() != null) {
                    String latestVersion = response.body().string();
                    CommonViaForgePlus.isModLatest = latestVersion.equals(CommonViaForgePlus.version);
                }
            }
        } catch (Exception ignored) {
            CommonViaForgePlus.isModLatest = false;
        }
    }
}
