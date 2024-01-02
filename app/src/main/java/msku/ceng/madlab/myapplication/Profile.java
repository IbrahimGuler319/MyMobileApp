package msku.ceng.madlab.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import msku.ceng.madlab.myapplication.R;

// ... (diğer importlar)

public class Profile extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView ageTextView;
    private TextView genderTextView;
    private TextView handTextView;
    private TextView footTextView;
    private TextView emailTextView;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // TextView'lere erişim tanımlamalarını yap
        usernameTextView = findViewById(R.id.usernameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        genderTextView = findViewById(R.id.genderTextView);
        handTextView = findViewById(R.id.handTextView);
        footTextView = findViewById(R.id.footTextView);
        emailTextView = findViewById(R.id.emailTextView);

        // Firestore ve kullanıcı referanslarını al
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Kullanıcı verilerini almak için Firestore'dan belirli bir belgeyi al
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Kullanıcı verilerini belgeden al
                            String username = documentSnapshot.getString("Username");
                            String email = currentUser.getEmail();

                            // TextView'lere verileri yerleştir
                            usernameTextView.setText("Username: " + username);
                            emailTextView.setText("Email: " + email);
                        } else {
                            // Belge bulunamadı
                            Toast.makeText(Profile.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Hata durumunda kullanıcıya bildir
                        Toast.makeText(Profile.this, "Failed to retrieve document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
