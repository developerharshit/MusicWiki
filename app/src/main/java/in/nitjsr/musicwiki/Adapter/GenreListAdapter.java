package in.nitjsr.musicwiki.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.nitjsr.musicwiki.Activities.GenreDetail;
import in.nitjsr.musicwiki.R;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<String> data;

    public GenreListAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.genre.setText(data.get(position));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GenreDetail.class);
            intent.putExtra("genre", data.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView genre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            genre = itemView.findViewById(R.id.genre_name);
        }
    }
}
