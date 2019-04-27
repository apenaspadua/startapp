package com.treinamento.mdomingos.startapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.treinamento.mdomingos.startapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setDetails(Context context, String nome, String cidade, String estado, String descricao,
                           String imagemPerfil, String imagemPublicacao){

        TextView name = mView.findViewById(R.id.nome_startup_feed_id);
        TextView city = mView.findViewById(R.id.text_cidade_feed_startup);
        TextView state = mView.findViewById(R.id.text_estado_feed_startup);
        TextView descri = mView.findViewById(R.id.texto_descricao_feed_id);
        CircleImageView imageProfile = mView.findViewById(R.id.imageview_feed_startup_id);
        ImageView imagePublication = mView.findViewById(R.id.imagem_publicacao_feed);

        //set data

        name.setText(nome);
        city.setText(cidade);
        state.setText(estado);
        descri.setText(descricao);
        Picasso.get().load(imagemPerfil).into(imageProfile);
        Picasso.get().load(imagemPublicacao).into(imagePublication);
    }
}
