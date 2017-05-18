package xyz.romakononovich.article;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class RvAdaptepSearchItem extends RecyclerView.Adapter<RvAdaptepSearchItem.ItemViewHolder> {
    List<Article> articleList;
    Context context;


    public RvAdaptepSearchItem(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context=context;
    }

    @Override
    public RvAdaptepSearchItem.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ItemViewHolder pvh = new ItemViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RvAdaptepSearchItem.ItemViewHolder holder, int position) {
        holder.tvTitle.setText(articleList.get(position).getTitle());
        holder.tvAuthor.setText(articleList.get(position).getAuthor());
        holder.tvDate.setText(articleList.get(position).getPublishedAt());
        holder.tvDecription.setText(articleList.get(position).getDescription());
        Picasso.with(context)
                .load(articleList.get(position).getUrlToImage())
                .placeholder(R.drawable.item_picture)
                .into(holder.imageView);
        Log.d("IMG",articleList.get(position).getUrlToImage());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvAuthor;
        private ImageView imageView;
        private TextView tvDate;
        private TextView tvDecription;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvAuthor = (TextView) itemView.findViewById(R.id.author);
            tvDate = (TextView) itemView.findViewById(R.id.date);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            tvDecription = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
