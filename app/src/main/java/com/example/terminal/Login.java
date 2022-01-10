package com.example.terminal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText login_email, login_password;
    FirebaseAuth fAuth;
    TextView loginBtn;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.LoginBtn);
        progressBar2 = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = login_email.getText().toString().trim();
                String password = login_password.getText().toString().trim();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                    login_email.setError("Email required");
                    login_password.setError("Create password");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    login_email.setError("Email required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    login_password.setError("Create password");
                    return;
                }
                if (password.length() < 6){
                    login_password.setError("Minimum 6 characters");
                    return;
                }

                //login user

                progressBar2.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Login.this, "Logged in", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    progressBar2.setVisibility(View.INVISIBLE);
                                    finish();
                                }
                                else{
                                    Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar2.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
            }
        });


    }
}