package com.treinamento.mdomingos.startapp.model;

import com.google.firebase.database.DatabaseReference;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;

/**
 * Created by Matheus de Padua on 29/03/2019.
 * mdomingos@luxfacta.com
 * For Luxfacta Soluções de TI
 * {@see more in https://www.luxfacta.com}
 */
public class Usuarios {

    private String id;
    private String email;
    private int perfil;


    public Usuarios() {
    }

    public void salvar(){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Usuarios").child(getId()).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }


}

