package com.treinamento.mdomingos.startapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.utils.UploadStorage;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ImageViewHolder> {

    private Context context;
    private List<UploadStorage> listUploads;

    public FeedAdapter(Context context, List<UploadStorage> listUploads){

        context = context;
        listUploads = listUploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.publicacao_item, viewGroup, false);

        return new ImageViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        final UploadStorage uploadCurrent = listUploads.get(i);
        Glide.with(context)
                .load(uploadCurrent.getImageUrl())
                .centerCrop()
                .into(imageViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return listUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public TextView nome, descricao, cidade, estado;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.nome_startup_feed_id);
            descricao = itemView.findViewById(R.id.texto_descricao_feed_id);
            cidade = itemView.findViewById(R.id.text_cidade_feed_startup);
            estado = itemView.findViewById(R.id.text_cidade_feed_startup);
            imageView = itemView.findViewById(R.id.imagem_publicacao_feed);


        }
    }
}
