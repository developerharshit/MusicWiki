package in.nitjsr.musicwiki.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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

import java.util.ArrayList;

import in.nitjsr.musicwiki.Activities.AlbumInfo;
import in.nitjsr.musicwiki.Activities.GenreDetail;
import in.nitjsr.musicwiki.Modals.Album;
import in.nitjsr.musicwiki.R;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Album> dataList;
    private final int flag;

    public AlbumListAdapter(Context context, ArrayList<Album> dataList, int flag) {
        this.context = context;
        this.dataList = dataList;
        this.flag = flag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_top_album, parent, false);

        if(flag == GenreDetail.TRACKS) {
            view = inflater.inflate(R.layout.item_top_tracks, parent, false);
        }
        return new AlbumListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int[] colors = {0xffe68a51,0xff8c6aff,0xffff5d74,0xff6b14c5,0xff6caaff};

        holder.albumName.setText(dataList.get(position).getName());
        holder.artistName.setText(dataList.get(position).getArtist());
        holder.body.setBackgroundColor(colors[position%5]);

        if(!TextUtils.isEmpty(dataList.get(position).getImg())) {
            Picasso.with(context).load(dataList.get(position).getImg())
                    .placeholder(R.drawable.placeholder).fit().networkPolicy(NetworkPolicy.OFFLINE).into(holder.img, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(dataList.get(position).getImg())
                            .placeholder(R.drawable.placeholder).fit().into(holder.img);
                }
            });
        }

        if(flag != 25) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context,AlbumInfo.class);
                intent.putExtra("artist",dataList.get(position).getArtist());
                intent.putExtra("album",dataList.get(position).getName());
                intent.putExtra("color",colors[position%5]);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView albumName,artistName;
        ImageView img;
        ConstraintLayout body;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.name);
            artistName = itemView.findViewById(R.id.artistName);
            img = itemView.findViewById(R.id.img);
            body = itemView.findViewById(R.id.body);
        }
    }
}
