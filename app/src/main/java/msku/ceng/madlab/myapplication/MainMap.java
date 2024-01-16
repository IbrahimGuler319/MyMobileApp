package msku.ceng.madlab.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;

import msku.ceng.madlab.myapplication.R;

public class MainMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private BottomNavigationView bottomNavigation;
    private FirebaseFirestore db;
    private HashMap<String, double[]> cities = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        db = FirebaseFirestore.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        cities.put("Eskisehir", new double[]{39.766193, 30.526714});
        cities.put("Istanbul", new double[]{41.015137, 28.979530});
        cities.put("Ankara", new double[]{39.925533, 32.866287});
        cities.put("Izmir", new double[]{38.423733, 27.142826});
        cities.put("Mugla", new double[]{37.22, 28.37});
        cities.put("Kocaeli", new double[]{40.766666, 29.916668});
        cities.put("Antalya", new double[]{36.884804, 30.704044});

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.action_profile) {
                startActivity(new Intent(MainMap.this, Profile.class));
                return true;
            } else if (itemId == R.id.action_home) {

                return true;
            } else if (itemId == R.id.action_advert) {
                startActivity(new Intent(MainMap.this, Adverts.class));
                return true;
            } else if (itemId == R.id.action_exit) {
                // Handle exit action
                finish();
                return true;
            } else {
                return false;
            }
        });
        bottomNavigation.setSelectedItemId(R.id.action_home);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot user : task.getResult()){
                            String username = user.getString("Username");
                            String city = user.getString("Location");
                            Log.d("MainMap", "username: " + username);
                            Log.d("MainMap", "location: " + city);

                            if (cities.containsKey(city)){
                                double[] coordinates = cities.get(city);
                                LatLng location = new LatLng(coordinates[0], coordinates[1]);
                                googleMap.addMarker(new MarkerOptions().position(location).title(username));
                            } else {
                                Log.d("MainMap", "City not found in the 'cities' HashMap for user: " + username);
                            }
                        }
                        LatLng defaultLocation = new LatLng(41.015137, 28.979530);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 6));
                    } else {
                        Log.e("MainMap", "Error getting documents: ", task.getException());
                    }
                });
    }
}