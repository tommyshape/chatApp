package com.example.terminal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {

    EditText user_name, user_email, user_password;
    TextView registerBtn, loginHereBtn;
    ProgressBar progressBar;
    static FirebaseAuth fAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String userID;
    static DocumentReference user_name_reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        registerBtn = findViewById(R.id.RegisterBtn);
        loginHereBtn = findViewById(R.id.LoginHereBtn);
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = user_email.getText().toString().trim();
                String password = user_password.getText().toString().trim();
                final String name = user_name.getText().toString().trim();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                    user_email.setError("Email required");
                    user_password.setError("Create password");
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    user_email.setError("Email required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    user_password.setError("Create password");
                    return;
                }
                if (password.length() < 6){
                    user_password.setError("Minimum 6 characters");
                    return;
                }
                if (TextUtils.isEmpty(name)){
                    user_name.setError("Enter your name");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //registering the user in firebase

                fAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    userID = fAuth.getCurrentUser().getUid();
                                    user_name_reference = db.collection("Terminal-user-credentials").document(userID);
                                    user_name_reference.set(new RegisterSign(name, email,userID))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Register.this, "name and email saved to database", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                                else{
                                    Toast.makeText(Register.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });

            }
        });

        loginHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}