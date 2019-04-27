package com.treinamento.mdomingos.startapp.model;

import com.google.firebase.database.DatabaseReference;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;
import com.treinamento.mdomingos.startapp.utils.UploadStorage;

public class Publicacao {

    private String id;
    private String nomeUsuario;
    private String descricao;
    private String cidade;
    private String estado;
    private String imagemPublicacao;
    private String imagemPerfil;

    public Publicacao(){
    }

    public Publicacao(String nomeUsuario, String cidade, String estado){
        this.nomeUsuario = nomeUsuario;
        this.cidade = cidade;
        this.estado = estado;
    }

    public void salvarDados(String id) {
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Publicacoes").child(id).child("nomeFantasia").setValue(nomeUsuario);
        databaseReference.child("Publicacoes").child(id).child("cidade").setValue(cidade);
        databaseReference.child("Publicacoes").child(id).child("estado").setValue(estado);
    }

    public void salvarFotoPublicacao(String id){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Publicacoes").child(id).child("fotoPublicacao").setValue(imagemPublicacao);
    }

    public void salvarFotoPerfil(String id){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Publicacoes").child(id).child("fotoPerfil").setValue(imagemPerfil);
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

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
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

    public String getImagemPublicacao() {
        return imagemPublicacao;
    }

    public void setImagemPublicacao(String imagemPublicacao) {
        this.imagemPublicacao = imagemPublicacao;
    }

    public String getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }
}
