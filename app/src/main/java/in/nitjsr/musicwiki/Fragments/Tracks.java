package in.nitjsr.musicwiki.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.nitjsr.musicwiki.Activities.GenreDetail;
import in.nitjsr.musicwiki.Adapter.AlbumListAdapter;
import in.nitjsr.musicwiki.Adapter.TrackListAdapter;
import in.nitjsr.musicwiki.Api.Api;
import in.nitjsr.musicwiki.Api.RetrofitClient;
import in.nitjsr.musicwiki.Modals.Album;
import in.nitjsr.musicwiki.Modals.Track;
import in.nitjsr.musicwiki.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tracks extends Fragment {

    private String genreName = "";
    private Api api;
    private ArrayList<Track> dataList;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        init(view);
        getTopTracks();
        return view;
    }

    private void init(View view) {
        if (getActivity().getIntent().hasExtra("genre")) {
            genreName = getActivity().getIntent().getStringExtra("genre");
        }
        api = new RetrofitClient().getInstance();
        dataList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void getTopTracks() {
        Call<JsonObject> call = api.getTopTracks(genreName);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject data = response.body();
                if (response.isSuccessful() && data.has("tracks")) {
                    int n = data.get("tracks").getAsJsonObject().get("track").getAsJsonArray().size();
                    for (int i = 0; i < n; i++) {
                        JsonObject trackObject = data.get("tracks").getAsJsonObject().get("track").getAsJsonArray().get(i).getAsJsonObject();
                        Track track = new Track();
                        track.setName(trackObject.get("name").getAsString());
                        track.setArtist(trackObject.get("artist").getAsJsonObject().get("name").getAsString());
                        track.setImg(trackObject.get("image").getAsJsonArray().get(2).getAsJsonObject().get("#text").getAsString());

                        dataList.add(track);

                    }
                    createTrackList();

                } else {
                    Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    private void createTrackList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TrackListAdapter adapter = new TrackListAdapter(getContext(), dataList, GenreDetail.TRACKS);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}