package com.example.securenotes;

import static android.view.View.INVISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText etLoginEmail;
    EditText etLoginPassword;
    Button btLogin;
    TextView tvRegisterHere;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        progressBar = findViewById(R.id.progressBar);
        etLoginEmail=findViewById(R.id.etLogInEmail);
        etLoginPassword=findViewById(R.id.etLoginPassword);
        btLogin=findViewById(R.id.btLogin);
        tvRegisterHere=findViewById(R.id.tvRegisterHere);
        tvRegisterHere.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
        btLogin.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btLogin.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            loginUser();
        });
    }

    private void loginUser() {
        String email=etLoginEmail.getText().toString();
        String password=etLoginPassword.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(password))
        {
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        }
        else
        {
            btLogin.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this,"Log In Successful",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginActivity.this,ShowActivity.class));
                            } else {
                                btLogin.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Log In failed"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                etLoginEmail.setText("");
                                etLoginPassword.setText("");
                                etLoginEmail.requestFocus();
                            }
                        }
                    });
        }
    }
}