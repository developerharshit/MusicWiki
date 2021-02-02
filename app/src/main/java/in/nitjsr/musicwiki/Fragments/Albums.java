package in.nitjsr.musicwiki.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.nitjsr.musicwiki.Activities.GenreDetail;
import in.nitjsr.musicwiki.Activities.MainActivity;
import in.nitjsr.musicwiki.Adapter.AlbumListAdapter;
import in.nitjsr.musicwiki.Adapter.GenreListAdapter;
import in.nitjsr.musicwiki.Api.Api;
import in.nitjsr.musicwiki.Api.RetrofitClient;
import in.nitjsr.musicwiki.Modals.Album;
import in.nitjsr.musicwiki.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Albums extends Fragment {

    private String genreName="";
    private Api api;
    private ArrayList<Album> dataList;
    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        init(view);
        getTopAlbums();
        return view;
    }

    private void init(View view) {
        if(getActivity().getIntent().hasExtra("genre")) {
            genreName = getActivity().getIntent().getStringExtra("genre");
        }
        api = new RetrofitClient().getInstance();
        dataList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void getTopAlbums() {
        Call<JsonObject> call = api.getTopAlbums(genreName);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject data = response.body();
                if (response.isSuccessful() && data.has("albums")) {
                    int n = data.get("albums").getAsJsonObject().get("album").getAsJsonArray().size();
                    for(int i=0;i<n;i++) {
                        JsonObject albumObject = data.get("albums").getAsJsonObject().get("album").getAsJsonArray().get(i).getAsJsonObject();
                        Album album = new Album();
                        album.setName(albumObject.get("name").getAsString());
                        album.setArtist(albumObject.get("artist").getAsJsonObject().get("name").getAsString());
                        album.setImg(albumObject.get("image").getAsJsonArray().get(2).getAsJsonObject().get("#text").getAsString());

                        dataList.add(album);
                    }
                    createAlbumList();

                } else {
                    Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }
    private void createAlbumList() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        AlbumListAdapter adapter = new AlbumListAdapter(getContext(), dataList, GenreDetail.ALBUMS);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}