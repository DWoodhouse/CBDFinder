package com.example.a100456794.login2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SignUp extends Activity implements View.OnClickListener {

    private Button buttonRegister;
    private TextInputEditText editTextUsername;
    private TextInputEditText editTextFirstName;
    private TextInputEditText editTextLastName;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextPasswordConfirm;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.btn_signup);
        final TextView login = (TextView) findViewById(R.id.alreadyregistered);

        //Takes values from xml and puts them into fields here
        editTextUsername = (TextInputEditText) findViewById(R.id.username_input);
        editTextFirstName = (TextInputEditText) findViewById(R.id.firstname_input);
        editTextLastName = (TextInputEditText) findViewById(R.id.lastname_input);
        editTextPassword = (TextInputEditText) findViewById(R.id.password_input);
        editTextPasswordConfirm = (TextInputEditText) findViewById(R.id.confirmpassword_input);


        buttonRegister.setOnClickListener(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent launchActivity1 = new Intent(SignUp.this, MainActivity.class);
                startActivity(launchActivity1);
            }
        });
    }

    private void registerUser() {

        final String username = editTextUsername.getText().toString().trim();
        final String firstName = editTextFirstName.getText().toString().trim();
        final String lastName = editTextLastName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordConfirm = editTextPasswordConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(passwordConfirm)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "The passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        } else {

            progressDialog.setMessage("Registering User...");
            progressDialog.show();
            firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.hide();
                                Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent launchActivity1 = new Intent(SignUp.this, MainActivity.class);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String userId = user.getUid();
                                DatabaseReference mRef = database.getReference().child("Users").child(userId);
                                mRef.child("username").setValue(username);
                                mRef.child("firstName").setValue(firstName);
                                mRef.child("lastName").setValue(lastName);

                                startActivity(launchActivity1);
                            } else {
                                progressDialog.hide();
                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                Toast.makeText(SignUp.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            registerUser();
        }
    }
}
