package com.example.securenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.HashMap;

public class NotesActivity extends AppCompatActivity {
    private EditText etTopic,etSubject;
    private FirebaseDatabase db=FirebaseDatabase.getInstance();
    private String topic,subject;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser user=mAuth.getCurrentUser();
    private DatabaseReference root=db.getReference().child(user.getUid());
    private HashMap<String,String> userMap=new HashMap<>();
    private ProgressBar progressBar;
    public final String key=user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        etTopic =findViewById(R.id.etTopic);
        etSubject=findViewById(R.id.etSubject);

        progressBar=findViewById(R.id.progressadd);


    }


    private void add() {
        progressBar.setVisibility(View.VISIBLE);
        if(etTopic.getText().toString().isEmpty() && etSubject.getText().toString().isEmpty())
        {
            etSubject.setError("Topic and Subject cannot be empty");
            etSubject.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        try {
            topic = AESCrypt.encrypt(key,etTopic.getText().toString());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        try {
            subject=AESCrypt.encrypt(key,etSubject.getText().toString());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        userMap.put("topic",topic);
        userMap.put("subject",subject);
        root.push().setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(NotesActivity.this, "Note Added...",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
        etTopic.setText("");
        etSubject.setText("");
        startActivity(new Intent(NotesActivity.this,ShowActivity.class));
    }
    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {
            add();
        }
        return super.onOptionsItemSelected(item);
    }
}