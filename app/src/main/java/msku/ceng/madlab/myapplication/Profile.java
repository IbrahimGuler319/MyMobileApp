package msku.ceng.madlab.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import msku.ceng.madlab.myapplication.R;


public class Profile extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private TextView textViewUsername, textViewEmail, textViewPassword;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPassword = findViewById(R.id.textViewPassword);

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
                        Log.e("ProfileActivity", "Failed to retrieve user data", e);
                    });
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.action_profile) {
                // Bu sayfa zaten Profile, başka bir aktiviteye geçmeye gerek yok.
                return true;
            } else if (itemId == R.id.action_message) {
                startActivity(new Intent(Profile.this, Message.class));
                return true;
            } else if (itemId == R.id.action_advert) {
                startActivity(new Intent(Profile.this, Adverts.class));
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