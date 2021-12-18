package com.example.securenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        setContentView(R.layout.activity_main);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // if you are redirecting from a fragment then use getActivity() as the context.
                if(user == null)
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                else
                    startActivity(new Intent(MainActivity.this,ShowActivity.class));
            }
        };
        Handler h = new Handler();
        // The Runnable will be executed after the given delay time
        h.postDelayed(r, 1500); // will be delayed for 1.5 seconds
    }
}