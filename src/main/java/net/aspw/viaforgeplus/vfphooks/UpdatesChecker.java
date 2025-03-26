package net.aspw.viaforgeplus.vfphooks;

import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.X509TrustManager;

public class UpdatesChecker {

    public static void check() {
        try {
            OldJVTLSPatcher tlsPatcher = new OldJVTLSPatcher();

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
