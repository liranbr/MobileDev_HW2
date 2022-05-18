package com.example.mobiledevhw2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FragmentLeaderboard extends Fragment {
    LeaderboardScoreAdapter adapter;

    public FragmentLeaderboard() {
        super(R.layout.fragment_leaderboard);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        assert view != null;
        RecyclerView lbList = view.findViewById(R.id.fragment_LST_leaderboard);
        SharedPreferences sp = view.getContext().getSharedPreferences("Leaderboard", Context.MODE_PRIVATE);
        ArrayList<LeaderboardScore> scoreArrayList = LeaderboardScore.getLeaderboard(sp);
        adapter = new LeaderboardScoreAdapter(scoreArrayList);

        lbList.setAdapter(adapter);

        return view;
    }

    public void setOnClickConsumer(Consumer<LeaderboardScore> consumer) {
        adapter.setConsumer(consumer);
    }
}
