package com.treinamento.mdomingos.startapp.model;


import com.google.firebase.database.DatabaseReference;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;

/**
 * Created by Matheus de Padua on 29/03/2019.
 * mdomingos@luxfacta.com
 * For Luxfacta Soluções de TI
 * {@see more in https://www.luxfacta.com}
 */
public class Startup {

    private String razaoSocial;
    private String nomeFantasia;
    private String email;
    private String cep;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cnpj;
    private String meta;
    private String investido;
    private String telefone;
    private String biografia;
    private String apresentacao;
    private String link;
    private String imagemPerfil;
    private int isImpulse;

    public Startup(){

    }

    public Startup(String biografia, String apresentacao, String link) {
        this.biografia = biografia;
        this.apresentacao = apresentacao;
        this.link = link;
    }

    public Startup(String razaoSocial, String nomeFantasia, String email, String telefone, String cep, String rua, String bairro, String cidade, String estado, String cnpj) {
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.email = email;
        this.telefone = telefone;
        this.cep = cep;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cnpj = cnpj;
    }

    public void salvarStartup(String id){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Usuarios").child(id).child("detalhe_startup").setValue(this);
        databaseReference.child("Usuarios").child(id).child("detalhes_completo").setValue(1);
    }

    public void salvarBioStartup(String id){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Usuarios").child(id).child("detalhe_startup").child("biografia").setValue(biografia);
        databaseReference.child("Usuarios").child(id).child("detalhe_startup").child("apresentacao").setValue(apresentacao);
        databaseReference.child("Usuarios").child(id).child("detalhe_startup").child("link").setValue(link);
        databaseReference.child("Usuarios").child(id).child("bio_completa").setValue(1);
    }

//    public void salvarFotoPerfil(String id){
//        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
//        databaseReference.child("Usuarios").child(id).child("detalhe_startup").child("foto_perfil").setValue(imagemPerfil);
//    }

    public void isImpulse(String id, int value){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Usuarios").child(id).child("detalhe_startup").child("isImpulse").setValue(value);
    }

    public void salvarMetaProgesso(String id){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Usuarios").child(id).child("progresso_startup").child("meta").setValue(meta);
        databaseReference.child("Usuarios").child(id).child("progresso_startup").child("investido").setValue(investido);
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getInvestido() {
        return investido;
    }

    public void setInvestido(String investido) {
        this.investido = investido;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getApresentacao() {
        return apresentacao;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }


    public int getIsImpulse() {
        return isImpulse;
    }

    public void setIsImpulse(int isImpulse) {
        this.isImpulse = isImpulse;
    }
}