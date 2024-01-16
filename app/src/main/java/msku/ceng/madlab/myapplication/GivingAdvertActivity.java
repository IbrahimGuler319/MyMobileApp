package msku.ceng.madlab.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
// This class implemented by Baran İşci 200709054
public class GivingAdvertActivity extends AppCompatActivity {

    private EditText editTextAdvertContent, editTextTime, editTextAddress;
    private Button btnSubmit;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giving_advert);

        editTextAdvertContent = findViewById(R.id.editTextAdvertContent);
        editTextTime = findViewById(R.id.editTextTime);
        editTextAddress = findViewById(R.id.editTextAddress);
        btnSubmit = findViewById(R.id.btnSubmit);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAdvert();
            }
        });
    }

    private void submitAdvert() {
        String content = editTextAdvertContent.getText().toString();
        String time = editTextTime.getText().toString();
        String address = editTextAddress.getText().toString();

        if (!content.isEmpty() && !time.isEmpty() && !address.isEmpty() && currentUser != null) {

            String advertId = db.collection("adverts").document().getId();


            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {

                            String username = documentSnapshot.getString("Username");


                            ArrayList<String> comments = new ArrayList<>();
                            comments.add("Comments \n");

                            Map<String, Object> advertData = new HashMap<>();
                            advertData.put("ID", advertId);
                            advertData.put("Owner", username);
                            advertData.put("Content", content);
                            advertData.put("Time", time);
                            advertData.put("Address", address);
                            advertData.put("Comments", comments);


                            db.collection("adverts")
                                    .document(advertId)  // Oluşturulan random ID'yi belge kimliği olarak kullan
                                    .set(advertData)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(GivingAdvertActivity.this, "İlan başarıyla verildi!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(GivingAdvertActivity.this, "İlan verme başarısız oldu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(GivingAdvertActivity.this, "Kullanıcı bilgileri alınamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Lütfen tüm bilgileri girin", Toast.LENGTH_SHORT).show();
        }
    }

}
