package com.treinamento.mdomingos.startapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.UserAdapterContacts;
import com.treinamento.mdomingos.startapp.model.Chatlist;
import com.treinamento.mdomingos.startapp.model.Usuarios;
import com.treinamento.mdomingos.startapp.service.notifications.Token;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton contatos;

    private UserAdapterContacts userAdapterContacts;
    private RecyclerView recyclerView;
    private List<Usuarios> mUsers;

    FirebaseUser user;
    DatabaseReference reference;

    private List<Chatlist> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        contatos = findViewById(R.id.floatingActionButton);

        recyclerView = findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                chatList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());


        contatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, UsersChatActivity.class));
                finish();
            }
        });
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(user.getUid()).setValue(token1);
    }

    private void chatList(){
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuarios  usuarios = snapshot.getValue(Usuarios.class);
                    for (Chatlist chatlist : usersList){
                        if (usuarios.getId().equals(chatlist.getId())){
                            mUsers.add(usuarios);
                        }
                    }
                }
                userAdapterContacts = new UserAdapterContacts(ChatActivity.this, mUsers, true);
                recyclerView.setAdapter(userAdapterContacts);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

}
