package com.example.securenotes;

import com.google.firebase.database.Exclude;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.GeneralSecurityException;


public class Model {
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser user=mAuth.getCurrentUser();
    public final String Skey=user.getUid();
    String topic,subject;


    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    String key;

    public String getKey(){
        return key;
    }

    public String getTopic() {
        try {
            return AESCrypt.decrypt(Skey,topic);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSubject() {
        try {
            return AESCrypt.decrypt(Skey,subject);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}
