package in.nitjsr.musicwiki.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private Api api;
    private ProgressDialog progressDialog;
    private ArrayList<String> dataList;

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
        dataList = new ArrayList<>();
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
                        dataList.add(name);
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
            }
        });
    }

    private void createGenreList() {
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        GenreListAdapter adapter = new GenreListAdapter(this, dataList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

}