package com.example.mobiledevhw2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardScore {
    public int score;
    LatLng latlng;
    String date;
    LeaderboardScore(int score, LatLng latlng, String date) {
        this.score = score;
        this.latlng = latlng;
        this.date = date;
    }

    @Override
    public String toString() {
        return "LeaderboardScore{" +
                "score=" + score +
                ", latlng=" + latlng +
                ", date='" + date + '\'' +
                '}';
    }

    public static void addScoreToLeaderboard(Activity gameActivity, int score) {
        // Get current location of user
        LatLng userLocation = getUserLocation(gameActivity);
        // Make score object
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LeaderboardScore lbScore = new LeaderboardScore(score, userLocation, date);
        // Load leaderboard scores from sharedPreference
        SharedPreferences sp = gameActivity.getSharedPreferences("Leaderboard", Context.MODE_PRIVATE);
        ArrayList<LeaderboardScore> leaderboard = getLeaderboard(sp);
        // Add score to it, trim if past 10
        leaderboard.add(lbScore);
        leaderboard.sort(Comparator.comparingInt(leaderboardScore -> -leaderboardScore.score)); // minus for descending order
        if (leaderboard.size() > 10) {
            leaderboard = new ArrayList<LeaderboardScore>(leaderboard.subList(0, 10));
        }
        // Save updated leaderboard scores to sharedPreference
        SharedPreferences.Editor editor = sp.edit();
        String leaderboardStr = new Gson().toJson(leaderboard);
        Log.d("current list: ", leaderboardStr);
        editor.putString("leaderboardJs", leaderboardStr);
        editor.commit();
    }

    public static LatLng getUserLocation(Activity gameActivity) {
        LocationManager lm = (LocationManager) gameActivity.getSystemService(Context.LOCATION_SERVICE);
        boolean haveCoarseGPS = ActivityCompat.checkSelfPermission(gameActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean haveFineGPS = ActivityCompat.checkSelfPermission(gameActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!haveCoarseGPS || !haveFineGPS) {
            Toast.makeText(gameActivity,"Couldn't get location",Toast.LENGTH_SHORT).show();
            return null;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        return latlng;
    }

    public static ArrayList<LeaderboardScore> getLeaderboard(SharedPreferences sp) {
        ArrayList<LeaderboardScore> leaderboard = new ArrayList<>();
        String leaderboardStr = sp.getString("leaderboardJs", null);
        if (leaderboardStr != null) {
            Type type = new TypeToken<List<LeaderboardScore>>(){}.getType();
            leaderboard = new Gson().fromJson(leaderboardStr, type);
        }
        return leaderboard;
    }
}
