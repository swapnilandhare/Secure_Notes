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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.security.GeneralSecurityException;
import java.util.HashMap;

public class NoteDetails extends AppCompatActivity {
    private String key;
    private EditText etTopic,etSubject;
    private String topic,subject;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser user=mAuth.getCurrentUser();
    private FirebaseDatabase db=FirebaseDatabase.getInstance();
    private DatabaseReference root=db.getReference().child(user.getUid());
    private HashMap<String,String> userMap=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        etTopic=findViewById(R.id.textView4);
        etSubject=findViewById(R.id.etsubjectview);
        progressBar=findViewById(R.id.progressSave);

        etTopic.setText(getIntent().getStringExtra("topic"));
        etSubject.setText(getIntent().getStringExtra("subject"));
        key=getIntent().getStringExtra("key");
    }

    private void delete() {
        progressBar.setVisibility(View.VISIBLE);
        root.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(NoteDetails.this, "Note Deleted...",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                finish();
            }
        });
    }

    public void save()
    {
        progressBar.setVisibility(View.VISIBLE);
        topic= etTopic.getText().toString();
        subject=etSubject.getText().toString();
        try {
            topic= AESCrypt.encrypt(user.getUid(),topic);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        try {
            subject= AESCrypt.encrypt(user.getUid(),subject);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        userMap.put("topic",topic);
        userMap.put("subject",subject);
        root.child(key).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(NoteDetails.this, "Note Saved...",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                finish();
            }
        });
    }
    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savedelete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {
            save();
        }
        if (id == R.id.delete) {
            delete();
        }
        return super.onOptionsItemSelected(item);
    }
}