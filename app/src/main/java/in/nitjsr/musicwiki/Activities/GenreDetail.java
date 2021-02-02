package in.nitjsr.musicwiki.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.nitjsr.musicwiki.Adapter.GenreDetailTabAdapter;
import in.nitjsr.musicwiki.Api.Api;
import in.nitjsr.musicwiki.Api.RetrofitClient;
import in.nitjsr.musicwiki.Fragments.Albums;
import in.nitjsr.musicwiki.Fragments.Artists;
import in.nitjsr.musicwiki.Fragments.Tracks;
import in.nitjsr.musicwiki.R;
import in.nitjsr.musicwiki.Utils.InfoDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreDetail extends AppCompatActivity implements View.OnClickListener {

    private String genreName="";
    private GenreDetailTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView title;
    public static final int TRACKS = 15;
    public static final int ALBUMS = 11;
    private Api api;
    private String info;
    private ImageView infoBtn,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_detail);

        init();
        setPagerAdapter();
        getGenreInfo();
    }

    private void init() {
        if(getIntent().hasExtra("genre")) {
            genreName = getIntent().getStringExtra("genre");
        }
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        title = findViewById(R.id.title);
        title.setText(genreName);
        api = new RetrofitClient().getInstance();
        infoBtn = findViewById(R.id.info);
        back = findViewById(R.id.back);

        infoBtn.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void setPagerAdapter() {
        adapter = new GenreDetailTabAdapter(getSupportFragmentManager(),0);
        adapter.addFragment(new Artists(), "Artists");
        adapter.addFragment(new Albums(),"Albums");
        adapter.addFragment(new Tracks(), "Tracks");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getGenreInfo() {
        Call<JsonObject> call = api.getGenreInfo(genreName);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject data = response.body();
                if (response.isSuccessful() && data.has("tag")) {
                    info = data.get("tag").getAsJsonObject().get("wiki").getAsJsonObject().get("content").getAsString();

                } else {
                    Toast.makeText(GenreDetail.this, "Error loading data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == infoBtn){
            InfoDialog dialog = new InfoDialog(genreName,info);
            dialog.show(getSupportFragmentManager(),"info");
        }
        else if (v == back) {
            finish();
        }
    }
}