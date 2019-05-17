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
import com.treinamento.mdomingos.startapp.model.Chatlist;
import com.treinamento.mdomingos.startapp.model.Usuarios;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.security.AccessController.getContext;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton contatos;

    private UserAdapterContacts userAdapterContacts;
    private RecyclerView recyclerView;
    private List<Usuarios> usuariosList;
    private List<Chatlist> usersList;


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
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuariosList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuarios  usuarios = snapshot.getValue(Usuarios.class);
                    for (Chatlist chatlist : usersList){
                        if (usuarios.getId().equals(chatlist.getId())){
                            usuariosList.add(usuarios);
                        }
                    }
                }
                userAdapterContacts = new UserAdapterContacts(ChatActivity.this, usuariosList, true);
                recyclerView.setAdapter(userAdapterContacts);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

}
