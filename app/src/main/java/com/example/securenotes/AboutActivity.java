package com.example.securenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    ImageView linkdin,insta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        linkdin=findViewById(R.id.linkdin);
        insta=findViewById(R.id.imageView4);
        linkdin.setOnClickListener(view -> {
            linkdin();
        });
        insta.setOnClickListener(view -> {
            insta();
        });
    }

    private void insta() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://instagram.com/swapnil_0077_?utm_medium=copy_link"));
        startActivity(intent);
    }

    private void linkdin() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.linkedin.com/in/swapnil-andhare"));
        startActivity(intent);
    }
}