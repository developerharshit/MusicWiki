package in.nitjsr.musicwiki.Api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("?method=chart.gettoptags")
    Call<JsonObject> getGenreList();

    @GET("?method=tag.gettopalbums")
    Call<JsonObject> getTopAlbums(@Query("tag") String tag);

    @GET("?method=tag.gettopartists")
    Call<JsonObject> getTopArtists(@Query("tag") String tag);
}
