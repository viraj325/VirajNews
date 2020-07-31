package com.projectbridge.virajnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import java.util.List;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.MyViewHolder> {
    private Context activity;
    private List<newsModel> feedItems;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView source, title, description, url, time;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            source = view.findViewById(R.id.source);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            url = view.findViewById(R.id.url);
            time = view.findViewById(R.id.time);
            image = view.findViewById(R.id.image);
        }
    }

    public newsAdapter(Context activity, List<newsModel> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_news_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        newsModel item = feedItems.get(position);

        holder.source.setText(item.getSource());
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.url.setText(item.getUrl());
        holder.time.setText(item.getTime());

        if (!(item.getUrlToImage().isEmpty())) {
            Glide.with(activity)
                    .load(item.getUrlToImage())
                    .transform(new CenterCrop(),new RoundedCorners(25))
                    .into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }
}