package msku.ceng.madlab.myapplication;// LookingAdvertsActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
// This class implemented by Baran İşci 200709054
public class LookingAdvertsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private ListView listViewAdverts;
    private AdvertAdapter advertAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looking_adverts);

        listViewAdverts = findViewById(R.id.listViewAdverts);
        db = FirebaseFirestore.getInstance();

        fetchAdverts();


        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.action_profile) {
                startActivity(new Intent(LookingAdvertsActivity.this, Profile.class));
                return true;
            } else if (itemId == R.id.action_home) {
                startActivity(new Intent(LookingAdvertsActivity.this, MainMap.class));
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

    private void fetchAdverts() {
        // Firebase Firestore sorgusunu gerçekleştir
        db.collection("adverts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<HashMap<String, Object>> advertsList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        HashMap<String, Object> advert = new HashMap<>(document.getData());
                        advertsList.add(advert);
                    }


                    advertAdapter = new AdvertAdapter(this, R.layout.item_advert, advertsList);
                    listViewAdverts.setAdapter(advertAdapter);
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(this, "Error fetching adverts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
