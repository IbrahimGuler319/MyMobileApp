package msku.ceng.madlab.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import msku.ceng.madlab.myapplication.MainActivity;
import msku.ceng.madlab.myapplication.MainMap;
import msku.ceng.madlab.myapplication.R;

public class Register extends AppCompatActivity {

    private Button btnRegister;
    private EditText editEmail, editUsername, editPassword, editPassword2;
    private String txtEmail, txtUsername, txtPassword, txtPassword2;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.Registerbtn);
        editUsername = findViewById(R.id.editTextUsername);
        editEmail = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editTextPassword);
        editPassword2 = findViewById(R.id.editTextCPassword);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = editEmail.getText().toString();
                txtUsername = editUsername.getText().toString();
                txtPassword = editPassword.getText().toString();
                txtPassword2 = editPassword2.getText().toString();

                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtUsername) ||
                        TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtPassword2)) {
                    Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!txtPassword.equals(txtPassword2)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Firestore'a kullanıcı bilgilerini ekleme
                    db = FirebaseFirestore.getInstance();
                    Map<String, Object> user = new HashMap<>();
                    user.put("Username", txtUsername);
                    user.put("Email", txtEmail);
                    user.put("Password", txtPassword);

                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    // Firestore'a başarıyla eklendikten sonra Firebase Authentication'a kayıt yap
                                    mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(Register.this, "Authentication failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "Failed to register. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
