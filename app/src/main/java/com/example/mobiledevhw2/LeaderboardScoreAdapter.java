package com.example.mobiledevhw2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.function.Consumer;

public class LeaderboardScoreAdapter extends RecyclerView.Adapter<LeaderboardScoreAdapter.ViewHolder> {
    ArrayList<LeaderboardScore> scoreArrayList;
    Consumer<LeaderboardScore> consumer;

    public LeaderboardScoreAdapter(ArrayList<LeaderboardScore> scoreArrayList) {
        this.scoreArrayList = scoreArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.set(scoreArrayList.get(position).score, scoreArrayList.get(position).date);
        holder.itemView.setOnClickListener(view -> consumer.accept(this.scoreArrayList.get(position)));
    }

    public void setConsumer(Consumer<LeaderboardScore> consumer) {
        this.consumer = consumer;
    }

    @Override
    public int getItemCount() {
        return scoreArrayList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView scoreView;
        TextView dateView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.scoreView = itemView.findViewById(R.id.lbItem_TXT_score);
            this.dateView = itemView.findViewById(R.id.lbItem_TXT_date);
        }

        public void set(int score, String dateText) {
            String s = "" + score;
            scoreView.setText(s);
            dateView.setText(dateText);
        }
    }
}
