package com.example.newsapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewHolder> {
    private Context context;
    private ArrayList<New> news;

    public NewsListAdapter(Context context, ArrayList<New> hotels) {
        this.context = context;
        this.news = hotels;
    }

    @NonNull
    @Override
    public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("xxxxxxxxxxxxxxxx",""+parent.toString()+"   "+viewType);
        return new NewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewHolder holder, int position) {

        New current_new = news.get(position);
        holder.new_title.setText(current_new.getNewTitle());
        holder.new_topic.setText(current_new.getNewTopic());
        holder.new_date.setText(current_new.getNewDate());
        holder.new_time.setText(current_new.getNewTime());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class NewHolder extends RecyclerView.ViewHolder{

        TextView new_title;
        TextView new_topic;
        TextView new_date;
        TextView new_time;

        public NewHolder(@NonNull View itemView) {
            super(itemView);
            new_title = itemView.findViewById(R.id.new_title_tv);
            new_topic = itemView.findViewById(R.id.new_topic_tv);
            new_date = itemView.findViewById(R.id.new_date_tv);
            new_time = itemView.findViewById(R.id.new_time_tv);
        }
    }
}