package com.treinamento.mdomingos.startapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.startup.PerfilVisitadoStartupActivity;
import com.treinamento.mdomingos.startapp.model.Publicacao;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.ViewHolder {

    View mView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    public FeedAdapter(@NonNull View itemView) {
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
        ImageView imagePublication = mView.findViewById(R.id.imageView_publi);
        RelativeLayout botaoSaberMais = mView.findViewById(R.id.botao_sabermais_feed);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //set data

        name.setText(nomeFantasia);
        city.setText(cidade);
        state.setText(estado);
        descri.setText(descricao);
        Picasso.with(context).load(fotoPerfil).into(imageProfile);
        Picasso.with(context).load(fotoPublicacao).fit().centerCrop().into(imagePublication);

        botaoSaberMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseUser.getUid().equals(id)){
                    Toast.makeText(context, "Para visitar vá até o seu perfil", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, PerfilVisitadoStartupActivity.class);
                    intent.putExtra("publicacoes",  id);
                    context.startActivity(intent);
                }
            }
        });


    }

}