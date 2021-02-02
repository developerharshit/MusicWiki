package in.nitjsr.musicwiki.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.nitjsr.musicwiki.Adapter.GenreListAdapter;
import in.nitjsr.musicwiki.Api.Api;
import in.nitjsr.musicwiki.Api.RetrofitClient;
import in.nitjsr.musicwiki.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GenreListAdapter.Callback, View.OnClickListener {

    private RecyclerView recyclerView;
    private Api api;
    private ProgressDialog progressDialog;
    private ArrayList<String> mainDataList,dummyDataList;
    private GenreListAdapter adapter;
    private ImageView close, search;
    private AutoCompleteTextView searchBox;
    private RelativeLayout layout;
    private boolean isSearchShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        getGenreList();
    }

    private void init() {
        api = new RetrofitClient().getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        recyclerView = findViewById(R.id.genreList);
        mainDataList = new ArrayList<>();
        dummyDataList = new ArrayList<>();
        close = findViewById(R.id.close);
        searchBox = findViewById(R.id.search_box);
        search = findViewById(R.id.search);
        layout = findViewById(R.id.search_cont);

        search.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    private void getGenreList() {
        progressDialog.show();
        Call<JsonObject> call = api.getGenreList();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject data = response.body();
                if (response.isSuccessful() && data.has("tags")) {
                    progressDialog.dismiss();
                    for (int i = 0; i < 50; i++) {
                        String name = data.get("tags").getAsJsonObject().get("tag").getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString();
                        mainDataList.add(name);
                    }
                    createGenreList();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createGenreList() {
        GridLayoutManager gd = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gd);

        for(int i = 0; i<10; ++i) {
            dummyDataList.add(mainDataList.get(i));
        }

        adapter = new GenreListAdapter(this, dummyDataList,1);
        adapter.setCallback(this);

        gd.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position == dummyDataList.size()) return 3;
                return 1;
            }
        });
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickShowMore() {
        for(int i = 10; i<mainDataList.size(); ++i) {
            dummyDataList.add(mainDataList.get(i));
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        if(v == search){
            layout.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>
                    (this,android.R.layout.select_dialog_item,mainDataList);
            searchBox.setThreshold(1);
            searchBox.setAdapter(adapter);

            searchBox.setOnItemClickListener((parent, view, position, id) -> {
                String text = searchBox.getText().toString();
                layout.setVisibility(View.GONE);
                searchBox.setText("");
                Intent intent = new Intent(this, GenreDetail.class);
                intent.putExtra("genre", text);
                this.startActivity(intent);
            });
        }
        else if(v == close) {
            layout.setVisibility(View.GONE);
        }
    }
}