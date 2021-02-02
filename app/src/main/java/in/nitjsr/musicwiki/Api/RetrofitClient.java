package in.nitjsr.musicwiki.Api;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final String BASE_URL = "https://ws.audioscrobbler.com/2.0/?";
    private final String apiKey = "c08516dddb22e336aa34ce8e95590a29";
    private Api api;

    public RetrofitClient() {
        setRetrofit();
    }

    public Api getInstance() {
        return api;
    }

    private void setRetrofit() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    HttpUrl url = request.url()
                            .newBuilder().addQueryParameter("api_key", apiKey)
                            .addQueryParameter("format", "json")
                            .build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        api = retrofit.create(Api.class);
    }
}
