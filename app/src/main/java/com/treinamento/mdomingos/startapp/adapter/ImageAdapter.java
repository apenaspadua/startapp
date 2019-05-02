package com.treinamento.mdomingos.startapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.startup.PerfilVisitadoStartupActivity;
import com.treinamento.mdomingos.startapp.model.Publicacao;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageAdapter extends RecyclerView.ViewHolder {

    View mView;


    public ImageAdapter(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setDetails(final Context context, String nomeFantasia, String cidade, String estado, String descricao,
                           String fotoPerfil, String fotoPublicacao, final String id){

        TextView name = mView.findViewById(R.id.nome_startup_feed_id);
        TextView city = mView.findViewById(R.id.text_cidade_feed_startup);
        TextView state = mView.findViewById(R.id.text_estado_feed_startup);
        TextView descri = mView.findViewById(R.id.texto_descricao_feed_id);
        CircleImageView imageProfile = mView.findViewById(R.id.imageview_feed_startup_id);
        ImageView imagePublication = mView.findViewById(R.id.imagem_publicacao_feed);
        RelativeLayout botaoSaberMais = mView.findViewById(R.id.botao_sabermais_feed);

        //set data
        name.setText(nomeFantasia);
        city.setText(cidade);
        state.setText(estado);
        descri.setText(descricao);
        Picasso.get().load(fotoPerfil).into(imageProfile);
        Picasso.get().load(fotoPublicacao).fit().centerCrop().into(imagePublication);

        botaoSaberMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PerfilVisitadoStartupActivity.class);
                intent.putExtra("publicacoes",  id);

            }
        });


    }

}