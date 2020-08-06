package com.dcodes.xet;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class Xet {

    private OnXetCompletedListener onXetCompletedListener;
    private static final String URL_IS_INVALID = "Detail url is invalid.";
    private static final String NULL_RESPONSE = "Server response returns null.";
    private String errorMessage = "";
    private boolean useAlternativeAPI = false;

    public static Xet newInstance() {
        return new Xet();
    }

    private Xet() {

    }

    public interface OnXetCompletedListener {
        void onSuccess(XetModel model);

        void onError(String errorMessage);
    }

    private interface XetRequest {
        @FormUrlEncoded
        @POST("api.php")
        Call<UrlModel> getRawBody(@Field("a") String a, @Field("b") String b);

    }

    private Retrofit getClient(String link) {
        return new Retrofit.Builder().baseUrl(link).addConverterFactory(GsonConverterFactory.create()).client(getUnsafeOkHttpClient().build()).build();
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private XetRequest getXetRequest(String api_link) {
        return getClient(api_link).create(XetRequest.class);
    }

    public void setOnXetCompletedListener(OnXetCompletedListener onXetCompletedListener) {
        this.onXetCompletedListener = onXetCompletedListener;
    }

    private String resolve(String s) {
        return new String(Base64.decode(s, Base64.DEFAULT));
    }

    @SuppressWarnings("unused")
    public void useAlternativeAPI() {
        this.useAlternativeAPI = true;
    }

    private boolean urlIsNotValid(String url) {
        if (url == null) return true;
        try {
            new URL(url);
            return false;
        } catch (MalformedURLException ignored) {
            return true;
        }
    }

    public void execute(String detailUrl) {
        if (onXetCompletedListener == null) {
            Log.e("IllegalAccessException", "set setOnXetCompletedListener first before execute");
            return;
        }

        if (urlIsNotValid(detailUrl)) {
            onXetCompletedListener.onError(URL_IS_INVALID);
            return;
        }

        if (detailUrl.contains("xnxx.com") && !useAlternativeAPI) {
            detailUrl = detailUrl.replace(resolve("aHR0cHM6Ly93d3cueG54eC5jb20v"), resolve("aHR0cHM6Ly93d3cueG54eC5lcy8="));
            String finalDetailUrl = detailUrl;
            new AsyncXxxLoader(new AsyncXxxLoader.OnXxxCompleteListener() {
                @Override
                public void onSuccess(XetModel xetModel) {
                    if (onXetCompletedListener != null) onXetCompletedListener.onSuccess(xetModel);
                }

                @Override
                public void onError(String message) {
                    errorMessage = message;
                    startAlternativeAPI(finalDetailUrl);
                }
            }).execute(detailUrl);

        } else {
            startAlternativeAPI(detailUrl);
        }
    }

    private void startAlternativeAPI(String url) {
        XetRequest xetRequest = getXetRequest(resolve("aHR0cHM6Ly9teWNpbmVtYS54eXoveG54eHVwZGF0ZS8="));
        xetRequest.getRawBody(resolve("eG54eHBk"), url).enqueue(new Callback<UrlModel>() {
            @Override
            public void onResponse(@NonNull Call<UrlModel> call, @NonNull Response<UrlModel> response) {
                UrlModel body = response.body();
                if (body == null) {
                    if (onXetCompletedListener != null)
                        onXetCompletedListener.onError(NULL_RESPONSE);
                } else {
                    if (body.getSuccess())
                        onXetCompletedListener.onSuccess(new XetModel(body.getSdUrl(), body.getHdUrl()));
                    else onXetCompletedListener.onError(body.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UrlModel> call, @NonNull Throwable t) {
                if (onXetCompletedListener != null) {
                    String error = !errorMessage.isEmpty() ? "Error ID : 1\n" + errorMessage + "\n\n" : "";
                    error = error + "Error ID : 2\nerror while getting hd and sd links\n" + t.getMessage();
                    onXetCompletedListener.onError(error);

                }
            }
        });
    }

}
