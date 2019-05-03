package com.treinamento.mdomingos.startapp.model;

import com.google.firebase.database.DatabaseReference;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;
import com.treinamento.mdomingos.startapp.utils.UploadStorage;

public class Publicacao {

    private String id;
    private String nomeFantasia;
    private String descricao;
    private String cidade;
    private String estado;
    private String fotoPublicacao;
    private String fotoPerfil;

    public Publicacao(){
    }

    public Publicacao(String nomeFantasia, String cidade, String estado){
        this.nomeFantasia = nomeFantasia;
        this.cidade = cidade;
        this.estado = estado;
    }

    public void salvarDados(String id) {
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Publicacoes").child(id).child("nomeFantasia").setValue(nomeFantasia);
        databaseReference.child("Publicacoes").child(id).child("cidade").setValue(cidade);
        databaseReference.child("Publicacoes").child(id).child("estado").setValue(estado);
        databaseReference.child("Publicacoes").child(id).child("id").setValue(id);
    }

    public void salvarFotoPublicacao(String id){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Publicacoes").child(id).child("fotoPublicacao").setValue(fotoPublicacao);
    }

    public void salvarFotoPerfil(String id){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Publicacoes").child(id).child("fotoPerfil").setValue(fotoPerfil);
    }

    public void salvarDescricao(String id){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Publicacoes").child(id).child("descricao").setValue(descricao);
    }

    public String getId() {
        return id;
    }

        public void setId(String id) {
            this.id = id;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFotoPublicacao() {
        return fotoPublicacao;
    }

    public void setFotoPublicacao(String fotoPublicacao) {
        this.fotoPublicacao = fotoPublicacao;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
