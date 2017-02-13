package br.com.delmano.zupchallenge.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import br.com.delmano.zupchallenge.R;
import br.com.delmano.zupchallenge.rest.api.ZupChallengeAPI;
import br.com.delmano.zupchallenge.rest.interceptor.LoggingInterceptor;
import br.com.delmano.zupchallenge.rest.model.RestError;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EBean(scope = EBean.Scope.Singleton)
public class Rest {

    private static final String CACHE_CONTROL = "Cache-Control";

    @RootContext
    protected Context context;

    @StringRes(R.string.api_url)
    protected String apiUrl;

    private ZupChallengeAPI zupChallengeAPI;

    private Converter<ResponseBody, RestError> converter;

    @AfterInject
    void afterInjection() {
        buildAppTresApi();
    }

    private void buildAppTresApi() {
        zupChallengeAPI = buildRetrofit(apiUrl).create(ZupChallengeAPI.class);
        converter = buildRetrofit(apiUrl).responseBodyConverter(RestError.class, new Annotation[0]);
    }

    @NonNull
    private Retrofit buildRetrofit(String apiUrl) {
        return new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .setDateFormat("dd MMM yyyy")
                        .create()))
                .callbackExecutor(Executors.newCachedThreadPool())
                .build();
    }

    @NonNull
    private OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new LoggingInterceptor())
                .addInterceptor(interceptor)
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .cache(new Cache(context.getCacheDir(), 10 * 1024 * 1024))
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(context.getCacheDir(), "http-cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cache;
    }

    public Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder()
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    public boolean hasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public Interceptor provideOfflineCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (!hasNetwork()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }

    public ZupChallengeAPI httpAPI() {
        return zupChallengeAPI;
    }

    public Converter<ResponseBody, RestError> errorConverter() {
        return converter;
    }
}