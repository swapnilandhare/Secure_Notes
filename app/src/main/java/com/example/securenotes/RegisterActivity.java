package com.example.securenotes;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    EditText etRegisterEmail;
    EditText etRegisterPassword;
    TextView tvLoginHere;
    Button btRegister;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressBar progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        progressBar2 =findViewById(R.id.progressBar2);
        etRegisterEmail=findViewById(R.id.etRegisterEmail);
        etRegisterPassword=findViewById(R.id.etRegisterPassword);
        btRegister=findViewById(R.id.btRegister);
        tvLoginHere=findViewById(R.id.tvLoginHere);
        tvLoginHere.setOnClickListener(view -> {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
        btRegister.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btRegister.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            createUser();
        });
    }

    private void createUser() {
        String email=etRegisterEmail.getText().toString();
        String password=etRegisterPassword.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            etRegisterEmail.setError("Email cannot be empty");
            etRegisterEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(password))
        {
            etRegisterPassword.setError("Password cannot be empty");
            etRegisterPassword.requestFocus();
        }
        else
        {
            btRegister.setVisibility(View.INVISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            } else {
                                btRegister.setVisibility(View.VISIBLE);
                                progressBar2.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Registration failed"+task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                etRegisterEmail.setText("");
                                etRegisterPassword.setText("");
                                etRegisterEmail.requestFocus();
                            }
                        }
                    });
        }
    }
}