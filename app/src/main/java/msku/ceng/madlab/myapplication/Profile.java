package msku.ceng.madlab.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    private static final String TAG = "Profile";
    private TextView textViewUsername, textViewEmail, textViewPassword;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPassword = findViewById(R.id.textViewPassword);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_home) {
                    // Handle the Home button click (optional: check if it's not already in Profile)
                    return true;
                } else if (menuItem.getItemId() == R.id.action_message) {
                    startActivity(new Intent(Profile.this, Message.class));
                    finish(); // Bu aktiviteyi kapatın, isteğe bağlı
                    return true;
                } else if (menuItem.getItemId() == R.id.action_advert) {
                    startActivity(new Intent(Profile.this, Adverts.class));
                    finish(); // Bu aktiviteyi kapatın, isteğe bağlı
                    return true;
                }
                return false;
            }
        });

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("Username");
                            String email = documentSnapshot.getString("Email");
                            String password = documentSnapshot.getString("Password");

                            textViewUsername.setText("Username: " + username);
                            textViewEmail.setText("Email: " + email);
                            textViewPassword.setText("Password: " + password);
                        } else {
                            Toast.makeText(Profile.this, "User data does not exist", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Profile.this, "Failed to retrieve user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Failed to retrieve user data", e);
                    });
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
