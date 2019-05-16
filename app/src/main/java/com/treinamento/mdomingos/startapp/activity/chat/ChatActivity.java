package com.treinamento.mdomingos.startapp.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.UserAdapterContacts;
import com.treinamento.mdomingos.startapp.model.Chat;
import com.treinamento.mdomingos.startapp.model.Usuarios;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton contatos;

    private UserAdapterContacts userAdapterContacts;
    private RecyclerView recyclerView;
    private List<Usuarios> usuariosList;

    FirebaseUser user;
    DatabaseReference reference;

    private List<String> userListString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        contatos = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();

        userListString = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userListString.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getSender().equals(user.getUid())){
                        userListString.add(chat.getReceiver());
                    }

                    if(chat.getReceiver().equals(user.getUid())){
                        userListString.add(chat.getSender());
                    }
                }

                readChats();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        contatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, ContatosChatActivity.class));
                finish();
            }
        });
    }

    private void  readChats(){
        usuariosList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuariosList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Usuarios usuarios = snapshot.getValue(Usuarios.class);

                    for(String id : userListString){
                        if(usuarios.getId().equals(id)){
                            if(usuariosList.size() != 0){
                                for(Usuarios usuarios1: usuariosList){
                                    if(!usuarios.getId().equals(usuarios1.getId())){
                                        usuariosList.add(usuarios);
                                    }
                                }
                            } else {
                                usuariosList.add(usuarios);
                            }
                        }
                    }
                }
                    Glide.with(getApplicationContext());
                    userAdapterContacts = new UserAdapterContacts(ChatActivity.this, usuariosList);
                    recyclerView.setAdapter(userAdapterContacts);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

}
