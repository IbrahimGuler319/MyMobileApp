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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// This class implemented by İbrahim Güler 200709065
public class MainActivity extends AppCompatActivity {

    private Button btnSignUp, btnLogin;
    private EditText editEmail, editPassword;
    private String txtEmail, txtPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        btnSignUp = findViewById(R.id.signupbtn);
        btnLogin = findViewById(R.id.btnLogin);
        editEmail = findViewById(R.id.loginEmail);
        editPassword = findViewById(R.id.loginPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = editEmail.getText().toString();
                txtPassword = editPassword.getText().toString();

                if (!TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtPassword)) {
                    mAuth.signInWithEmailAndPassword(txtEmail, txtPassword)
                            .addOnCompleteListener(MainActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    mUser = mAuth.getCurrentUser();

                                    if (mUser != null) {
                                        Toast.makeText(MainActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, MainMap.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(MainActivity.this, "User authentication failed!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "Email and Password cannot be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(MainActivity.this, Register.class);
                startActivity(signUp);
            }
        });
    }
}

