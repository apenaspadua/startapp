package com.treinamento.mdomingos.startapp.model;


/**
 * Created by Matheus de Padua on 29/03/2019.
 * mdomingos@luxfacta.com
 * For Luxfacta Soluções de TI
 * {@see more in https://www.luxfacta.com}
 */
public class Investidor {

    private String nome;
    private int cpf;
    private String tipoInvestidor;


    public void salvarInvestidor(){
//        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
//        databaseReference.child("Startups").child(getId()).setValue(this);
//        databaseReference.child("Startups").child(getId()).child("perfil").setValue(getPerfil());
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCpf() {
        return cpf;
    }

    public void setCpf(int cpf) {
        this.cpf = cpf;
    }

    public String getTipoInvestidor() {
        return tipoInvestidor;
    }

    public void setTipoInvestidor(String tipoInvestidor) {
        this.tipoInvestidor = tipoInvestidor;
    }

    public Investidor() {
    }


}


