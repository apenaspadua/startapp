package com.treinamento.mdomingos.startapp.model;

import java.io.Serializable;

/**
 * Created by Matheus de Padua on 01/04/2019.
 * mdomingos@luxfacta.com
 * For Luxfacta Soluções de TI
 * {@see more in https://www.luxfacta.com}
 */
public class InvestidorResponse implements Serializable {
    private String id;
    private int perfil;
    private String email;
    private Investidor detalhe_investidor;
    private String foto_perfil;


    public InvestidorResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto_Perfil() {
        return foto_perfil;
    }

    public void setFoto_Perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public Investidor getDetalhe_investidor() {
        return detalhe_investidor;
    }

    public void setDetalhe_investidor(Investidor detalhe_investidor) {
        this.detalhe_investidor = detalhe_investidor;
    }
}
