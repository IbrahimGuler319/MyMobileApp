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
                db = FirebaseFirestore.getInstance();
                txtEmail = editEmail.getText().toString();
                txtUsername = editUsername.getText().toString();
                txtPassword = editPassword.getText().toString();
                txtPassword2 = editPassword2.getText().toString();

                Map<String, Object> user = new HashMap<>();
                user.put("Username", txtUsername);
                user.put("Email", txtEmail);
                user.put("Password", txtPassword);

                db.collection("user")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(Register.this, "Successful", Toast.LENGTH_SHORT).show();
                                Intent login = new Intent(Register.this, MainMap.class);
                                startActivity(login);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                if (!TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtUsername) &&
                        !TextUtils.isEmpty(txtPassword) && !TextUtils.isEmpty(txtPassword2)) {
                    mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(Register.this, "Email and Password cannot be empty.", Toast.LENGTH_SHORT).show();
                }
                Intent register = new Intent(Register.this, MainActivity.class);
                startActivity(register);
            }
        });
    }
}
