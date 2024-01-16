package msku.ceng.madlab.myapplication;// LookingAdvertsActivity.java

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LookingAdvertsActivity extends AppCompatActivity {
    private ListView listViewAdverts;
    private AdvertAdapter advertAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looking_adverts);

        listViewAdverts = findViewById(R.id.listViewAdverts);
        db = FirebaseFirestore.getInstance();

        // Firebase'den verileri çek ve AdvertAdapter'a ilet
        fetchAdverts();
    }

    private void fetchAdverts() {
        // Firebase Firestore sorgusunu gerçekleştir
        db.collection("adverts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<HashMap<String, Object>> advertsList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Her bir ilanın verilerini al ve liste ye ekle
                        HashMap<String, Object> advert = new HashMap<>(document.getData());
                        advertsList.add(advert);
                    }

                    // AdvertAdapter'ı oluştur ve ListView'a bağla
                    advertAdapter = new AdvertAdapter(this, R.layout.item_advert, advertsList);
                    listViewAdverts.setAdapter(advertAdapter);
                })
                .addOnFailureListener(e -> {
                    // Hata durumunda kullanıcıyı bilgilendir
                    Toast.makeText(this, "Error fetching adverts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
