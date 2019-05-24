package com.treinamento.mdomingos.startapp.model;

import com.google.firebase.database.DatabaseReference;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;

public class Notificacao {

    private String id;
    private String descricao;
    private String foto_perfil;


    public Notificacao(){
    }

    public Notificacao(String desc, String fotoPerfil){
        this.descricao = desc;
        this.foto_perfil = fotoPerfil;
    }

    public void salvarNotificacao(String id, String cont) {
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Notificacoes").child(id).child(cont).child("descricao").setValue(descricao);
        databaseReference.child("Notificacoes").child(id).child(cont).child("fotoPerfil").setValue(foto_perfil);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFoto_Perfil() {
        return foto_perfil;
    }

    public void setFoto_Perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }
}
