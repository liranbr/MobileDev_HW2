package com.example.mobiledevhw2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;
import java.util.function.Consumer;

public class LeaderboardActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FragmentLeaderboard fragmentLeaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        fragmentLeaderboard = (FragmentLeaderboard) getSupportFragmentManager().findFragmentById(R.id.leaderboard_FCV_Leaderboard);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.leaderboard_FCV_Map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Objects.requireNonNull(LeaderboardScore.getUserLocation(fragmentLeaderboard.requireActivity())), 4f));
        Consumer<LeaderboardScore> consumer = score -> {
            googleMap.addMarker(new MarkerOptions().position(score.latlng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(score.latlng, 6f));
        };
        this.fragmentLeaderboard.setOnClickConsumer(consumer);
    }
}
