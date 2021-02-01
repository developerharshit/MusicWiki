package in.nitjsr.musicwiki.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.nitjsr.musicwiki.Adapter.AlbumListAdapter;
import in.nitjsr.musicwiki.Adapter.ArtistListAdapter;
import in.nitjsr.musicwiki.Api.Api;
import in.nitjsr.musicwiki.Api.RetrofitClient;
import in.nitjsr.musicwiki.Modals.Album;
import in.nitjsr.musicwiki.Modals.Artist;
import in.nitjsr.musicwiki.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Artists extends Fragment {

    private String genreName="";
    private Api api;
    private ProgressDialog progressDialog;
    private ArrayList<Artist> dataList;
    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        init(view);
        getTopArtists();
        return view;
    }

    private void init(View view) {
        if(getActivity().getIntent().hasExtra("genre")) {
            genreName = getActivity().getIntent().getStringExtra("genre");
        }
        api = new RetrofitClient().getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        dataList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void getTopArtists() {
        progressDialog.show();
        Call<JsonObject> call = api.getTopArtists(genreName);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject data = response.body();
                if (response.isSuccessful() && data.has("topartists")) {
                    progressDialog.dismiss();
                    for(int i=0;i<50;i++) {
                        JsonObject albumObject = data.get("topartists").getAsJsonObject().get("artist").getAsJsonArray().get(i).getAsJsonObject();
                        Artist artist = new Artist();
                        artist.setName(albumObject.get("name").getAsString());
                        artist.setImage(albumObject.get("image").getAsJsonArray().get(2).getAsJsonObject().get("#text").getAsString());

                        dataList.add(artist);

                    }
                    createAlbumList();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
    private void createAlbumList() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        ArtistListAdapter adapter = new ArtistListAdapter(getContext(), dataList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}