package com.treinamento.mdomingos.startapp.model;

import com.google.firebase.database.DatabaseReference;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;

public class Notificacao {

    private String id;
    private String senderId;
    private String descricao;
    private String fotoPerfil;


    public Notificacao(){
    }

    public Notificacao(String desc, String fotoPerfil){
        this.descricao = desc;
        this.fotoPerfil = fotoPerfil;
    }

    public void salvarNotificacao(String id, String cont, String senderId) {
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Notificacoes").child(id).child(cont).child("descricao").setValue(descricao);
        databaseReference.child("Notificacoes").child(id).child(cont).child("fotoPerfil").setValue(fotoPerfil);
        databaseReference.child("Notificacoes").child(id).child(cont).child("senderId").setValue(senderId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
