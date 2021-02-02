package in.nitjsr.musicwiki.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

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

public class AlbumInfo extends AppCompatActivity implements View.OnClickListener {

    private String albumName,artistName;
    private Api api;
    private ImageView img,info,back;
    private TextView album,artist,content;
    private boolean isPlaylistShowing = true;
    private boolean isPlaylistAvailable = true;
    private RecyclerView recyclerView;
    private int color;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_info);

        init();
        getAlbumInfo();
    }

    private void init() {
        if(getIntent().hasExtra("album")) {
            albumName = getIntent().getStringExtra("album");
        }
        if(getIntent().hasExtra("artist")) {
            artistName = getIntent().getStringExtra("artist");
        }
        if(getIntent().hasExtra("color")) {
            color = getIntent().getIntExtra("color",0);
        }

        api = new RetrofitClient().getInstance();
        album = findViewById(R.id.albumName);
        artist = findViewById(R.id.artistName);
        img = findViewById(R.id.img);
        info = findViewById(R.id.info);
        content = findViewById(R.id.content);
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerView);
        layout = findViewById(R.id.layout);

        info.setOnClickListener(this);
        back.setOnClickListener(this);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {color,0xFF1A1A1A});
        layout.setBackground(gd);
    }

    private void getAlbumInfo() {
        Call<JsonObject> call = api.getAlbumInfo(artistName,albumName);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject data = response.body();
                if (response.isSuccessful() && data.has("album")) {
                    JsonObject albumObject = data.get("album").getAsJsonObject();
//                    Album album1 = new Album();

                    String count = prettyCount(albumObject.get("playcount").getAsLong());

                    album.setText(albumObject.get("name").getAsString());
                    artist.setText("by "+albumObject.get("artist").getAsString()+"  •  "+count+" Plays");
                    if(albumObject !=null && albumObject.has("wiki")){
                        content.setText(Html.fromHtml(albumObject.get("wiki").getAsJsonObject().get("content").getAsString()));
                    }

//                    Setting Album image
                    Picasso.with(AlbumInfo.this).load(albumObject.get("image").getAsJsonArray().get(2).getAsJsonObject().get("#text").getAsString())
                            .placeholder(R.drawable.placeholder).fit().networkPolicy(NetworkPolicy.OFFLINE).into(img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(AlbumInfo.this).load(albumObject.get("image").getAsJsonArray().get(2).getAsJsonObject().get("#text").getAsString())
                                    .placeholder(R.drawable.placeholder).fit().into(img);
                        }
                    });

//                    Creating Track List
                    int n = albumObject.get("tracks").getAsJsonObject().get("track").getAsJsonArray().size();
                    if(n>0) {
                        ArrayList<Track> tracks = new ArrayList<>();
                        for(int i=0;i<n;i++) {
                            JsonObject trackObject = albumObject.get("tracks").getAsJsonObject().get("track").getAsJsonArray().get(i).getAsJsonObject();
                            Track track = new Track();
                            track.setArtist(artistName);
                            track.setName(trackObject.get("name").getAsString());
                            track.setImg("");

                            tracks.add(track);
                        }
                        createTrackList(tracks);
                    }
                    else {
                        isPlaylistAvailable = false;
                        info.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                    }

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
                    Toast.makeText(AlbumInfo.this, "Error loading data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void createTrackList(ArrayList<Track> dataList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TrackListAdapter adapter = new TrackListAdapter(this, dataList, GenreDetail.TRACKS);
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
                recyclerView.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
            }
            else if(isPlaylistAvailable){
                isPlaylistShowing = false;
                info.setImageDrawable(getResources().getDrawable(R.drawable.ic_playlist));
                recyclerView.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            }
        }
        else if(v == back) {
            finish();
        }
    }
}