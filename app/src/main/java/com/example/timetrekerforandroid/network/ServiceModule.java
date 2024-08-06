package com.example.timetrekerforandroid.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceModule {
    private static ServiceModule instance;

    private Gson gson;
    private ServiceModule(){}

    public static ServiceModule getInstance() {
        if(instance == null) {
            instance = new ServiceModule();
            instance.setGson(new GsonBuilder().create());
        }
        return instance;
    }
    public ApiService getService(){ return getTestService(Const.URL, ApiService.class);}

    public ApiService getDownloadService(){return getDataService(Const.URL_DOWNLOAD, ApiService.class);}

    public <T> T getDataService(String url, final Class<T> service) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
                .create(service);
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }


    public <T> T getTestService(String url, final Class<T> service) {
        OkHttpClient client = getTestHttpClient();
        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)

                .build().create(service);
    }

//
    public OkHttpClient getTestHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(8);
        final TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            @Override public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }
            @Override public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }
            @Override public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[]{}; }
        }};
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory, new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                })
                .hostnameVerifier(new HostnameVerifier() {
                    @Override public boolean verify(String hostname, SSLSession session) { return true; }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .dispatcher(dispatcher)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    Response response = chain.proceed(request);
                    return response;
                })
                .addInterceptor(logging);
        return httpBuilder.build();
    }
}
