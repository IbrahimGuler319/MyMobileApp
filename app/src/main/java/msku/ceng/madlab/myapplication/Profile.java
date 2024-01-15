package msku.ceng.madlab.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private Button btnEditProfile;
    private BottomNavigationView bottomNavigation;
    private EditText editUsername, editName, editSurname, editAge;
    private RadioGroup handPrefer, footPrefer;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private ArrayList<String> userSports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnEditProfile = findViewById(R.id.editProfileButton);
        editUsername = findViewById(R.id.editTextUsername);
        editName = findViewById(R.id.editTextName);
        editSurname = findViewById(R.id.editTextSurname);
        editAge = findViewById(R.id.editTextAge);
        handPrefer = findViewById(R.id.radioGroupHand);
        footPrefer = findViewById(R.id.radioGroupFoot);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("Username");
                            String name = documentSnapshot.getString("Name");
                            String surname = documentSnapshot.getString("Surname");
                            String age = documentSnapshot.getString("Age");
                            String handPreference = documentSnapshot.getString("HandPreference");
                            String footPreference = documentSnapshot.getString("FootPreference");
                            userSports = (ArrayList<String>) documentSnapshot.get("Sports");
                            editUsername.setText(username);
                            editName.setText(name);
                            editSurname.setText(surname);
                            editAge.setText(age);
                            setRadioGroupSelection(handPrefer, handPreference);
                            setRadioGroupSelection(footPrefer, footPreference);
                            if (userSports != null) {
                                setCheckBoxes(userSports);
                            }

                            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateUserData();
                                    Toast.makeText(getApplicationContext(), "Your update is successful.", Toast.LENGTH_SHORT).show();

                                }
                            });
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
    private void setCheckBoxes(ArrayList<String> userSports) {
        // XML'deki checkbox ID'leri
        int[] checkBoxIds = {
                R.id.checkBoxFootball,
                R.id.checkBoxBasketball,
                R.id.checkBoxVolleyball,
                R.id.checkBoxGolf,
                R.id.checkBoxBadminton,
                R.id.checkBoxTennis
        };

        for (int i = 0; i < checkBoxIds.length; i++) {
            CheckBox checkBox = findViewById(checkBoxIds[i]);
            // Kullanıcının "Sports" dizisinde bulunan sporlara göre checkbox'ları işaretle
            if (userSports.contains(checkBox.getText().toString())) {
                checkBox.setChecked(true);
            }
        }
    }
    private void setRadioGroupSelection(RadioGroup radioGroup, String preference) {
        if (preference != null) {
            switch (preference) {
                case "Left":
                    radioGroup.check(radioGroup.getId() == R.id.radioGroupHand ? R.id.radioButtonLeftHand : R.id.radioButtonLeftFoot);
                    break;
                case "Right":
                    radioGroup.check(radioGroup.getId() == R.id.radioGroupHand ? R.id.radioButtonRightHand : R.id.radioButtonRightFoot);
                    break;
                case "Both":
                    radioGroup.check(radioGroup.getId() == R.id.radioGroupHand ? R.id.radioButtonBothHand : R.id.radioButtonBothFoot);
                    break;
                // Varsayılan olarak hiçbir şey işaretlenmemiş olacak
            }
        }
    }
    private void updateUserData() {
        // Kullanıcının girdiği yeni verileri al
        String newUsername = editUsername.getText().toString();
        String newName = editName.getText().toString();
        String newSurname = editSurname.getText().toString();
        String newAge = editAge.getText().toString();
        int selectedHandId = handPrefer.getCheckedRadioButtonId();
        RadioButton selectedHandPrefer = findViewById(selectedHandId);
        String stringHand = selectedHandPrefer.getText().toString();
        int selectedFootId = footPrefer.getCheckedRadioButtonId();
        RadioButton selectedFootPrefer = findViewById(selectedFootId);
        String stringFoot = selectedFootPrefer.getText().toString();
        CheckBox checkbox1 = findViewById(R.id.checkBoxFootball);
        CheckBox checkbox2 = findViewById(R.id.checkBoxBasketball);
        CheckBox checkbox3 = findViewById(R.id.checkBoxVolleyball);
        CheckBox checkbox4 = findViewById(R.id.checkBoxGolf);
        CheckBox checkbox5 = findViewById(R.id.checkBoxBadminton);
        CheckBox checkbox6 = findViewById(R.id.checkBoxTennis);

        CheckBox[] checkboxes = {checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6};
        ArrayList<String> newSelectedSports = new ArrayList<>();

        for (CheckBox checkbox : checkboxes) {
            if (checkbox.isChecked()) {
                newSelectedSports.add(checkbox.getText().toString());
            }
        }

        // Yeni verileri Firestore'a güncelle
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("Username", newUsername);
        userUpdates.put("Name", newName);
        userUpdates.put("Surname", newSurname);
        userUpdates.put("Age", newAge);
        userUpdates.put("HandPreference", stringHand);
        userUpdates.put("FootPreference", stringFoot);
        userUpdates.put("Sports", newSelectedSports);
        db.collection("users")
                .document(currentUser.getUid())
                .update(userUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Başarılı güncelleme
                        } else {
                            // Güncelleme hatası
                        }
                    }
                });
    }
}
