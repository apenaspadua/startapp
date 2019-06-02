package com.treinamento.mdomingos.startapp.model;

import com.google.firebase.database.DatabaseReference;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;

public class Salvos {

    private String id;
    private String nome;
    private String fotoPerfil;

    public Salvos(){}


    public void ListarSalvos(String id, String idRandom, String nomeStartup, String fotoStarup){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Salvos").child(id).child(idRandom).child("nome").setValue(nomeStartup);
        databaseReference.child("Salvos").child(id).child(idRandom).child("fotoPerfil").setValue(fotoStarup);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }


}
