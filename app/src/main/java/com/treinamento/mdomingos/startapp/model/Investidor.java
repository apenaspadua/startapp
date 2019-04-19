package com.treinamento.mdomingos.startapp.model;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;

import java.io.Serializable;

/**
 * Created by Matheus de Padua on 29/03/2019.
 * mdomingos@luxfacta.com
 * For Luxfacta Soluções de TI
 * {@see more in https://www.luxfacta.com}
 */
public class Investidor implements Serializable {

    private String nome;
    private String email;
    private String telefone;
    private String empresa;
    private String data;
    private String cep;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cnpj;
    private String cpf;
    private String biografia;
    private String apresentacao;

    public Investidor(){}

    public Investidor(String biografia, String apresentacao){
        this.biografia = biografia;
        this.apresentacao = apresentacao;
    }

    public Investidor(String nome, String email, String telefone, String empresa, String data, String cep, String rua, String bairro, String cidade, String estado, String cnpj, String cpf) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.empresa = empresa;
        this.data = data;
        this.cep = cep;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cnpj = cnpj;
        this.cpf = cpf;
    }

    public void salvarInvestidor(String id){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Usuarios").child(id).child("detalhe_investidor").setValue(this);
        databaseReference.child("Usuarios").child(id).child("detalhes_completo").setValue(1);
    }

    public void salvarBioInvestidor(String id){
        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
        databaseReference.child("Usuarios").child(id).child("detalhe_investidor").child("biografia").setValue(biografia);
        databaseReference.child("Usuarios").child(id).child("detalhe_investidor").child("apresentacao").setValue(apresentacao);
        databaseReference.child("Usuarios").child(id).child("bio_completa").setValue(1);
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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


}


