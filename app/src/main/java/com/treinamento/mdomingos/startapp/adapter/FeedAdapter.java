package com.treinamento.mdomingos.startapp.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.treinamento.mdomingos.startapp.R;


public class FeedAdapter extends RecyclerView.Adapter {

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public TextView nome, descricao, cidade, estado;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.nome_startup_feed_id);
            descricao = itemView.findViewById(R.id.texto_descricao_feed_id);
            cidade = itemView.findViewById(R.id.text_cidade_feed_startup);
            estado = itemView.findViewById(R.id.text_cidade_feed_startup);


        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
