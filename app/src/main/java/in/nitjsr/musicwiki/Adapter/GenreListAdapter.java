package in.nitjsr.musicwiki.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.nitjsr.musicwiki.Activities.GenreDetail;
import in.nitjsr.musicwiki.R;

public class GenreListAdapter extends RecyclerView.Adapter {
    private final Context context;
    private final ArrayList<String> data;
    private final int flag;
    private static final int FOOTER_VIEW = 1;
    private Callback callback;

    public GenreListAdapter(Context context, ArrayList<String> data, int flag) {
        this.context = context;
        this.data = data;
        this.flag = flag;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(viewType == FOOTER_VIEW) {
            view = inflater.inflate(R.layout.item_genre_footer, parent, false);
            return new FooterViewHolder(view);
        }
        view = inflater.inflate(R.layout.item_genre, parent, false);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.itemView.setOnClickListener(v -> {
                if(callback != null) {
                    callback.onClickShowMore();
                }
            });
        }
        else {
            NormalViewHolder viewHolder = (NormalViewHolder)holder;
            viewHolder.genre.setText(data.get(position));
            if (flag == 0) {
                viewHolder.genre.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            viewHolder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, GenreDetail.class);
                intent.putExtra("genre", data.get(position));
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if(data.size()>10 || flag==0) {
            return data.size();
        }
        return data.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == data.size()) {
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);

        }
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView genre;
        public NormalViewHolder(View itemView) {
            super(itemView);
            genre = itemView.findViewById(R.id.genre_name);
        }
    }

    public interface Callback {
        void onClickShowMore();
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }
}
