package com.zencom.weathertest.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zencom.weathertest.BuildConfig;
import com.zencom.weathertest.utils.crypto.Base64;
import com.zencom.weathertest.utils.crypto.CryptoUtils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    public static final String DEFAULT_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static final String BASIC_SECURITY_HEADER = "";//"basic_security_header";
    public static final String BASIC_SECURITY_VALUE = "0y34189hf1qhfq0œ431hn";

    public static <S> S createService(Class<S> serviceClass) {

        Gson gson = new GsonBuilder().setDateFormat(DEFAULT_FORMAT_DATETIME).setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeClientBuilder(
                        new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.HEADERS)
                                .setLevel(HttpLoggingInterceptor.Level.BODY)
                        , BuildConfig.CONNECT_TIME_OUT))
                .build();

        return retrofit.create(serviceClass);
    }


    public static OkHttpClient getUnsafeClientBuilder(HttpLoggingInterceptor interceptor, int timeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        try {

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
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

            OkHttpClient.Builder clientBuilder = builder
                    .connectTimeout(BuildConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(BuildConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(BuildConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(interceptor)
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier((hostname, session) -> true);

            return clientBuilder.build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getUnsafeClientBuilder(int timeout) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        try {

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
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


            OkHttpClient.Builder clientBuilder = builder
                    .connectTimeout(BuildConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(BuildConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(BuildConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier((hostname, session) -> true);

            return clientBuilder.build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateBasicAuthToken(String phoneNumber) {

        String password = CryptoUtils.getSecret(16);//new RandomString(16, new Random()).nextString();
        String token = phoneNumber + ":" + password;
        String encoded = android.util.Base64.encodeToString(token.getBytes(), android.util.Base64.DEFAULT);
        String withoutBlanks = encoded.replaceAll("\\n", "");
        return withoutBlanks;
    }

    public static String generateBasicAuthToken(String phoneNumber, String password) {
        String token = phoneNumber + ":" + password;
        String encoded = android.util.Base64.encodeToString(token.getBytes(), android.util.Base64.DEFAULT);
        String withoutBlanks = encoded.replaceAll("\\n", "");
        return withoutBlanks;
    }

    public static String generateAESKey() {
        String key = null;
        SecretKey secretKey = null;
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(32);
            secretKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (secretKey != null) {
            key = Base64.encodeBytes(secretKey.getEncoded());
        }

        return key;
    }

    public static String generateSha1Mac(String s, String keyString) {
        byte[] bytes = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA1");
            Mac mac = null;
            mac = Mac.getInstance("HmacSHA1");
            mac.init(key);
            bytes = mac.doFinal(s.getBytes("UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        if (bytes != null) {
            return new String(bytes);
        } else return "";
    }
}
