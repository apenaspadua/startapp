package com.treinamento.mdomingos.startapp.activity.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.UserAdapterContacts;
import com.treinamento.mdomingos.startapp.model.Usuarios;

import java.util.ArrayList;
import java.util.List;

public class ContatosChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapterContacts userAdapterContacts;
    private List<Usuarios> usuariosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos_chat);

        recyclerView = findViewById(R.id.recycler_view_chat_contacts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usuariosList = new ArrayList<>();
        readUser();
    }

    public void readUser(){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuariosList.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Usuarios usuarios = snapshot.getValue(Usuarios.class);

                    assert usuarios != null;
                    assert  firebaseUser != null;

                    if(!usuarios.getId().equals(firebaseUser.getUid())) {
                        usuariosList.add(usuarios);
                    }
                }

                userAdapterContacts = new UserAdapterContacts(ContatosChatActivity.this, usuariosList);
                recyclerView.setAdapter(userAdapterContacts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ChatActivity.class));
        finish();
    }
}
