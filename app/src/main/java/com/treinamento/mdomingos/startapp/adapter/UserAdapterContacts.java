package com.treinamento.mdomingos.startapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.chat.MensagemActivity;
import com.treinamento.mdomingos.startapp.model.Chat;
import com.treinamento.mdomingos.startapp.model.Usuarios;

import java.util.List;

public class UserAdapterContacts extends RecyclerView.Adapter<UserAdapterContacts.ViewHolder> {

    private Context mContext;
    private List<Usuarios> mUsers;
    private boolean ischat;

    String theLastMessage;

    public UserAdapterContacts(Context context, List<Usuarios> mUsers, boolean ischat){
        this.mContext = context;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_chat_item, viewGroup, false);
        return new UserAdapterContacts.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Usuarios usuarios = mUsers.get(i);
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

            if (ischat){
                lastMessage(usuarios.getId(), viewHolder.last_msg);
            } else {
                viewHolder.last_msg.setVisibility(View.GONE);
            }

        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MensagemActivity.class);
                intent.putExtra("userid", usuarios.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView username, last_msg;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.usermame_chat_list);
            last_msg = itemView.findViewById(R.id.last_msg);
            profile_image = itemView.findViewById(R.id.image_profile_list_contacts_chat);

        }
    }

    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }

                switch (theLastMessage){
                    case  "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setVisibility(View.VISIBLE);
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
