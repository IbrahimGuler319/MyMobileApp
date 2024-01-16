package msku.ceng.madlab.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import msku.ceng.madlab.myapplication.MainActivity;
import msku.ceng.madlab.myapplication.MainMap;
import msku.ceng.madlab.myapplication.R;
// This class implemented by İbrahim Güler 200709065
public class Register extends AppCompatActivity {

    private Button btnRegister;
    private EditText editEmail, editUsername, editName, editSurname, editAge, editPassword, editPassword2;
    private RadioGroup handPrefer, footPrefer;
    private String txtEmail, txtUsername, txtName, txtSurname, txtAge, txtPassword, txtPassword2, selectedHand, selectedFoot;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<String> selectedSports; // Yeni eklenen satır

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.Registerbtn);
        editUsername = findViewById(R.id.editTextUsername);
        editName = findViewById(R.id.editTextName);
        editSurname = findViewById(R.id.editTextSurname);
        editAge = findViewById(R.id.editTextAge);
        editEmail = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editTextPassword);
        editPassword2 = findViewById(R.id.editTextCPassword);
        handPrefer = findViewById(R.id.radioGroupHand);
        footPrefer = findViewById(R.id.radioGroupFoot);
        selectedSports = new ArrayList<>(); // Yeni eklenen satır
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = editEmail.getText().toString();
                txtUsername = editUsername.getText().toString();
                txtName = editName.getText().toString();
                txtSurname = editSurname.getText().toString();
                txtAge = editAge.getText().toString();
                txtPassword = editPassword.getText().toString();
                txtPassword2 = editPassword2.getText().toString();
                selectedSports.clear();

                CheckBox checkBoxFootball = findViewById(R.id.checkBoxFootball);
                if (checkBoxFootball.isChecked()) {
                    selectedSports.add(checkBoxFootball.getText().toString());
                }

                CheckBox checkBoxBasketball = findViewById(R.id.checkBoxBasketball);
                if (checkBoxBasketball.isChecked()) {
                    selectedSports.add(checkBoxBasketball.getText().toString());
                }

                CheckBox checkBoxVolleyball = findViewById(R.id.checkBoxVolleyball);
                if (checkBoxVolleyball.isChecked()) {
                    selectedSports.add(checkBoxVolleyball.getText().toString());
                }

                CheckBox checkBoxGolf = findViewById(R.id.checkBoxGolf);
                if (checkBoxGolf.isChecked()) {
                    selectedSports.add(checkBoxGolf.getText().toString());
                }

                CheckBox checkBoxBadminton = findViewById(R.id.checkBoxBadminton);
                if (checkBoxBadminton.isChecked()) {
                    selectedSports.add(checkBoxBadminton.getText().toString());
                }

                CheckBox checkBoxTennis = findViewById(R.id.checkBoxTennis);
                if (checkBoxTennis.isChecked()) {
                    selectedSports.add(checkBoxTennis.getText().toString());
                }

                if (TextUtils.isEmpty(txtEmail) && TextUtils.isEmpty(txtUsername) &&
                        TextUtils.isEmpty(txtPassword) && TextUtils.isEmpty(txtPassword2)) {
                    Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!txtPassword.equals(txtPassword2)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        if (currentUser != null) {

                                            int selectedHandId = handPrefer.getCheckedRadioButtonId();
                                            if (selectedHandId != -1) {
                                                RadioButton selectedHandRadioButton = findViewById(selectedHandId);
                                                selectedHand = selectedHandRadioButton.getText().toString();
                                            }


                                            int selectedFootId = footPrefer.getCheckedRadioButtonId();
                                            if (selectedFootId != -1) {
                                                RadioButton selectedFootRadioButton = findViewById(selectedFootId);
                                                selectedFoot = selectedFootRadioButton.getText().toString();
                                            }


                                            db = FirebaseFirestore.getInstance();
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("Username", txtUsername);
                                            user.put("Email", txtEmail);
                                            user.put("Name", txtName);
                                            user.put("Surname", txtSurname);
                                            user.put("Location", txtAge);
                                            user.put("Password", txtPassword);
                                            user.put("HandPreference", selectedHand);
                                            user.put("FootPreference", selectedFoot);
                                            user.put("Sports", selectedSports);

                                            db.collection("users")
                                                    .document(currentUser.getUid())
                                                    .set(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(Register.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(Register.this, "Failed to register. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(Register.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}