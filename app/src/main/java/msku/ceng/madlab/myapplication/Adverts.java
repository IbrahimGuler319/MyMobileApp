package msku.ceng.madlab.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Adverts extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private TextView advertsMenu;
    private Button givingAdvert, lookingAdvert, yourAdverts, referencedAdverts;
    private LinearLayout topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverts);
        advertsMenu = findViewById(R.id.advertsMenu);
        givingAdvert = findViewById(R.id.givingAdvert);
        lookingAdvert = findViewById(R.id.lookingAdvert);
        yourAdverts = findViewById(R.id.givingAdvert);
        referencedAdverts = findViewById(R.id.referencedAdverts);
        topBar = findViewById(R.id.topBar);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.action_profile) {
                startActivity(new Intent(Adverts.this, Profile.class));
                return true;
            } else if (itemId == R.id.action_message) {
                startActivity(new Intent(Adverts.this, Message.class));
                return true;
            } else if (itemId == R.id.action_advert) {
                // Bu sayfa zaten Adverts, başka bir aktiviteye geçmeye gerek yok.
                return true;
            } else if (itemId == R.id.action_exit) {
                // Handle exit action
                finish();
                return true;
            } else {
                return false;
            }
        });
    }
}