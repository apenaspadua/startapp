package com.treinamento.mdomingos.startapp.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfig {

    private static DatabaseReference reference;

    public static DatabaseReference getFirebase(){

        if(reference == null){
            reference = FirebaseDatabase.getInstance().getReference();
        }

        return reference;
    }
}
