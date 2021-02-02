package in.nitjsr.musicwiki.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.nitjsr.musicwiki.Adapter.AlbumListAdapter;
import in.nitjsr.musicwiki.Adapter.GenreListAdapter;
import in.nitjsr.musicwiki.Adapter.TrackListAdapter;
import in.nitjsr.musicwiki.Api.Api;
import in.nitjsr.musicwiki.Api.RetrofitClient;
import in.nitjsr.musicwiki.Modals.Album;
import in.nitjsr.musicwiki.Modals.Track;
import in.nitjsr.musicwiki.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistInfo extends AppCompatActivity implements View.OnClickListener {

    private String artistName;
    private Api api;
    private ImageView img,info,back;
    private TextView artist,content,playCount,followers;
    private boolean isPlaylistShowing = true;
    private boolean isPlaylistAvailable = true;
    private int color;
    private RelativeLayout layout;
    private LinearLayout rv_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_info);

        init();
        getArtistInfo();
        getArtistTracks();
        getArtistAlbums();
    }

    private void init() {
        if(getIntent().hasExtra("artist")) {
            artistName = getIntent().getStringExtra("artist");
        }
        if(getIntent().hasExtra("color")) {
            color = getIntent().getIntExtra("color",0);
        }

        api = new RetrofitClient().getInstance();
        artist = findViewById(R.id.artistName);
        img = findViewById(R.id.img);
        info = findViewById(R.id.info);
        content = findViewById(R.id.content);
        back = findViewById(R.id.back);
        layout = findViewById(R.id.layout);
        playCount = findViewById(R.id.playCount);
        followers = findViewById(R.id.followers);
        rv_container = findViewById(R.id.rv_container);

        info.setOnClickListener(this);
        back.setOnClickListener(this);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {color,0xFF1A1A1A});
        layout.setBackground(gd);
    }

    private void getArtistInfo() {
        Call<JsonObject> call = api.getArtistInfo(artistName);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject data = response.body();
                if (response.isSuccessful() && data.has("artist")) {
                    JsonObject albumObject = data.get("artist").getAsJsonObject();
//                    Album album1 = new Album();

                    String count = prettyCount(albumObject.get("stats").getAsJsonObject().get("playcount").getAsLong());
                    String listeners = prettyCount(albumObject.get("stats").getAsJsonObject().get("listeners").getAsLong());

                    artist.setText(albumObject.get("name").getAsString());
                    playCount.setText(count+" plays");
                    followers.setText(listeners+" followers");
                    if(albumObject !=null && albumObject.has("bio")){
                        content.setText(Html.fromHtml(albumObject.get("bio").getAsJsonObject().get("content").getAsString()));
                    }

//                    Setting Artist image
                    Picasso.with(ArtistInfo.this).load(albumObject.get("image").getAsJsonArray().get(2).getAsJsonObject().get("#text").getAsString())
                            .placeholder(R.drawable.placeholder).fit().networkPolicy(NetworkPolicy.OFFLINE).into(img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ArtistInfo.this).load(albumObject.get("image").getAsJsonArray().get(2).getAsJsonObject().get("#text").getAsString())
                                    .placeholder(R.drawable.placeholder).fit().into(img);
                        }
                    });

//                    Creating Genre List
                    int m = albumObject.get("tags").getAsJsonObject().get("tag").getAsJsonArray().size();
                    ArrayList<String> genres = new ArrayList<>();
                    for(int i=0;i<m;i++) {
                        JsonObject genreObject = albumObject.get("tags").getAsJsonObject().get("tag").getAsJsonArray().get(i).getAsJsonObject();
                        String track = genreObject.get("name").getAsString();

                        genres.add(track);
                    }
                    createGenreList(genres);

                } else {
                    Toast.makeText(ArtistInfo.this, "Error loading data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getArtistTracks() {
        Call<JsonObject> call = api.getArtistTracks(artistName);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject data = response.body();
                if (response.isSuccessful() && data.has("toptracks")) {
                    int n = data.get("toptracks").getAsJsonObject().get("track").getAsJsonArray().size();
                    ArrayList<Album> albums = new ArrayList<>();
                    for(int i=0;i<n;i++) {
                        JsonObject albumObject = data.get("toptracks").getAsJsonObject().get("track").getAsJsonArray().get(i).getAsJsonObject();
                        Album album = new Album();
                        album.setName(albumObject.get("name").getAsString());
                        album.setArtist(albumObject.get("artist").getAsJsonObject().get("name").getAsString());
                        album.setImg(albumObject.get("image").getAsJsonArray().get(2).getAsJsonObject().get("#text").getAsString());
                        albums.add(album);
                    }
                    createTrackList(albums);
                } else {
                    Toast.makeText(ArtistInfo.this, "Error loading data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getArtistAlbums() {
        Call<JsonObject> call = api.getArtistAlbums(artistName);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject data = response.body();
                if (response.isSuccessful() && data.has("topalbums")) {
                    int n = data.get("topalbums").getAsJsonObject().get("album").getAsJsonArray().size();
                    ArrayList<Album> albums = new ArrayList<>();
                    for(int i=0;i<n;i++) {
                        JsonObject albumObject = data.get("topalbums").getAsJsonObject().get("album").getAsJsonArray().get(i).getAsJsonObject();
                        Album album = new Album();
                        album.setName(albumObject.get("name").getAsString());
                        album.setArtist(albumObject.get("artist").getAsJsonObject().get("name").getAsString());
                        album.setImg(albumObject.get("image").getAsJsonArray().get(2).getAsJsonObject().get("#text").getAsString());
                        albums.add(album);
                    }
                    createAlbumList(albums);
                } else {
                    Toast.makeText(ArtistInfo.this, "Error loading data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void createTrackList(ArrayList<Album> dataList) {
        RecyclerView recyclerView = findViewById(R.id.tracks_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        AlbumListAdapter adapter = new AlbumListAdapter(this, dataList, 25);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void createAlbumList(ArrayList<Album> dataList) {
        RecyclerView recyclerView = findViewById(R.id.albums_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        AlbumListAdapter adapter = new AlbumListAdapter(this, dataList, GenreDetail.ALBUMS);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void createGenreList(ArrayList<String> genres) {
        RecyclerView recyclerView = findViewById(R.id.genreList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        GenreListAdapter adapter = new GenreListAdapter(this, genres,0);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private String prettyCount(long numValue) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == info) {
            if(!isPlaylistShowing && isPlaylistAvailable) {
                isPlaylistShowing = true;
                info.setImageDrawable(getResources().getDrawable(R.drawable.ic_info));
                rv_container.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
            }
            else if(isPlaylistAvailable){
                isPlaylistShowing = false;
                info.setImageDrawable(getResources().getDrawable(R.drawable.ic_playlist));
                rv_container.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            }
        }
        else if(v == back) {
            finish();
        }
    }
}