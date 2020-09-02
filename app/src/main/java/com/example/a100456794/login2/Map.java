package com.example.a100456794.login2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Map extends AppCompatActivity {

    ProgressBar progressBar;
    EditText editTextUsername;
    Button resetPassword;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final TextView returnLogin = (TextView) findViewById(R.id.returnLogin);
        returnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent launchActivity = new Intent(Map.this, MainActivity.class);
                startActivity(launchActivity);
            }
        });

        progressBar = findViewById(R.id.progressBar);
        editTextUsername = findViewById(R.id.username_input);
        resetPassword = findViewById(R.id.btn_resetPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.sendPasswordResetEmail(editTextUsername.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Map.this, "Password reset link sent to email address", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Map.this, "The email address could not be found. Please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}
