package sample.com.cleartaxtweets.api;

import android.util.Base64;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sample.com.cleartaxtweets.model.AccessToken;

/**
 * Created by ladia on 21/07/16.
 */
public class TwitterServiceProvider {

    public static final String API_BASE_URL = ApiConstants.TWITTER_SEARCH_URL;
    private static TwitterApiService mApiService;

    public static TwitterApiService getService(boolean isConfigured) {
        OkHttpClient.Builder okHttpClient;
        if(isConfigured) {
            okHttpClient = getConfiguredClient();
        } else {
            okHttpClient = getAuthClient();
        }
        okHttpClient.readTimeout(60, TimeUnit.SECONDS);
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS);
        final Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build());
        mApiService = builder.build().create(TwitterApiService.class);
        return mApiService;
    }

    public static OkHttpClient.Builder getAuthClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        String credentials = ApiConstants.BEARER_TOKEN_CREDENTIALS;
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", basic)
                        .header("Accept", "application/json")
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        return okHttpClient;
    }

    private static OkHttpClient.Builder getConfiguredClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        final String token = ClearTaxApplication.getInstance().getAccessToken();
        final String tokenType = ClearTaxApplication.getInstance().getTokenType();
        if (token != null && tokenType!=null) {
            okHttpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization",
                                    tokenType + " " + token)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }
        return okHttpClient;
    }
}
