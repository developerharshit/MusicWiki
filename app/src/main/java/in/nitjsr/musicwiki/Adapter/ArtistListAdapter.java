package in.nitjsr.musicwiki.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import in.nitjsr.musicwiki.Modals.Artist;
import in.nitjsr.musicwiki.R;

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder> {
    Context context;
    ArrayList<Artist> dataList;

    public ArtistListAdapter(Context context, ArrayList<Artist> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_top_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int[] colors = {0xffe68a51,0xff8c6aff,0xffff5d74,0xff6b14c5,0xff6caaff};

        holder.artistName.setText(dataList.get(position).getName());
        holder.body.setBackgroundColor(colors[position%5]);

        Picasso.with(context).load(dataList.get(position).getImage())
                .placeholder(R.drawable.placeholder).fit().networkPolicy(NetworkPolicy.OFFLINE).into(holder.img, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(dataList.get(position).getImage())
                        .placeholder(R.drawable.placeholder).fit().into(holder.img);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        ImageView img;
        ConstraintLayout body;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artistName);
            img = itemView.findViewById(R.id.img);
            body = itemView.findViewById(R.id.body);
        }
    }
}
