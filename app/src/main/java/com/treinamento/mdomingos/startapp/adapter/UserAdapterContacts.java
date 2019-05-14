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
import com.treinamento.mdomingos.startapp.model.Usuarios;

import java.util.List;

public class UserAdapterContacts extends RecyclerView.Adapter<UserAdapterContacts.ViewHolder> {

    private Context mContext;
    private List<Usuarios> mUsers;

    public UserAdapterContacts(Context context, List<Usuarios> mUsers){
        this.mContext = context;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_chat_item, viewGroup, false);
        return new UserAdapterContacts.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Usuarios usuarios = mUsers.get(i);
        viewHolder.username.setText(usuarios.getNome());
        if(usuarios.getPerfil() == 1) {
            if (usuarios.getFoto_perfil() == null) {
                viewHolder.profile_image.setImageResource(R.drawable.investidor_icon2);
            } else {
                Glide.with(mContext).load(usuarios.getFoto_perfil()).into(viewHolder.profile_image);
            }
        } else if(usuarios.getPerfil() == 2){
            if (usuarios.getFoto_perfil() == null) {
                viewHolder.profile_image.setImageResource(R.drawable.startup_icon2);
            } else {
                Glide.with(mContext).load(usuarios.getFoto_perfil()).into(viewHolder.profile_image);
            }
        }
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
