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
import com.treinamento.mdomingos.startapp.model.Salvos;

import java.util.List;

public class ListStartusAdapter extends RecyclerView.Adapter<ListStartusAdapter.ViewHolder> {

    private Context mContext;
    private List<Salvos> mUsers;

    public ListStartusAdapter(Context context, List<Salvos> mUsers){
        this.mContext = context;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_chat_item, viewGroup, false);
        return new ListStartusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Salvos salvos = mUsers.get(i);
        viewHolder.username.setText(salvos.getNome());
        Glide.with(mContext).load(salvos.getFotoPerfil()).into(viewHolder.profile_image);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.usermame_chat_list);
            profile_image = itemView.findViewById(R.id.image_profile_list_contacts_chat);

        }
    }
}
