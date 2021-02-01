package in.nitjsr.musicwiki.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import in.nitjsr.musicwiki.Adapter.GenreDetailTabAdapter;
import in.nitjsr.musicwiki.Fragments.Albums;
import in.nitjsr.musicwiki.Fragments.Artists;
import in.nitjsr.musicwiki.Fragments.Tracks;
import in.nitjsr.musicwiki.R;

public class GenreDetail extends AppCompatActivity {

    private String genreName="";
    private GenreDetailTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_detail);

        init();
        setPagerAdapter();
    }

    private void init() {
        if(getIntent().hasExtra("genre")) {
            genreName = getIntent().getStringExtra("genre");
        }
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        title = findViewById(R.id.title);
        title.setText(genreName);
    }

    private void setPagerAdapter() {
        adapter = new GenreDetailTabAdapter(getSupportFragmentManager(),0);
        adapter.addFragment(new Artists(), "Artists");
        adapter.addFragment(new Albums(),"Albums");
        adapter.addFragment(new Tracks(), "Tracks");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}