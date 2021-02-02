package in.nitjsr.musicwiki.Api;

import com.google.gson.JsonObject;

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

    @GET("?method=tag.gettoptracks")
    Call<JsonObject> getTopTracks(@Query("tag") String tag);

    @GET("?method=tag.getinfo")
    Call<JsonObject> getGenreInfo(@Query("tag") String tag);

    @GET("?method=album.getinfo")
    Call<JsonObject> getAlbumInfo(
            @Query("artist") String artist,
            @Query("album") String album
    );

    @GET("?method=artist.getinfo")
    Call<JsonObject> getArtistInfo(@Query("artist") String artist);

    @GET("?method=artist.gettopalbums")
    Call<JsonObject> getArtistAlbums(@Query("artist") String artist);

    @GET("?method=artist.gettoptracks")
    Call<JsonObject> getArtistTracks(@Query("artist") String artist);

}
