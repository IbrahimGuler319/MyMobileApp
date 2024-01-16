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
// Thiss class implemented by İbrahim Güler 200709065
public class Adverts extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private TextView advertsMenu;
    private Button givingAdvert, lookingAdvert;
    private LinearLayout topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adverts);
        advertsMenu = findViewById(R.id.advertsMenu);
        givingAdvert = findViewById(R.id.givingAdvert);
        lookingAdvert = findViewById(R.id.lookingAdvert);
        topBar = findViewById(R.id.topBar);

        givingAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Adverts.this, GivingAdvertActivity.class));
            }
        });
        lookingAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Adverts.this, LookingAdvertsActivity.class));
            }
        });
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.action_profile) {
                startActivity(new Intent(Adverts.this, Profile.class));
                return true;
            } else if (itemId == R.id.action_home) {
                startActivity(new Intent(Adverts.this, MainMap.class));
                return true;
            } else if (itemId == R.id.action_advert) {
                return true;
            } else if (itemId == R.id.action_exit) {
                // Handle exit action
                finish();
                return true;
            } else {
                return false;
            }
        });
        bottomNavigation.setSelectedItemId(R.id.action_advert);
    }
}