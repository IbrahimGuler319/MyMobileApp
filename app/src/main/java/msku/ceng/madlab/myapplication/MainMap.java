package msku.ceng.madlab.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import msku.ceng.madlab.myapplication.R;

public class MainMap extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_home) {
                // Handle the Home button click (optional: check if it's not already in MainMap)
                startActivity(new Intent(MainMap.this, MainMap.class));
                return true;
            } else if (itemId == R.id.action_profile) {
                startActivity(new Intent(MainMap.this, Profile.class));
                return true;
            } else if (itemId == R.id.action_message) {
                startActivity(new Intent(MainMap.this, Message.class));
                return true;
            } else if (itemId == R.id.action_advert) {
                startActivity(new Intent(MainMap.this, Adverts.class));
                return true;
            } else {
                return false;
            }
        });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng location = new LatLng(55.6761, 12.5683);
        googleMap.addMarker(new MarkerOptions().position(location).title("Copenhagen"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
    }
}
