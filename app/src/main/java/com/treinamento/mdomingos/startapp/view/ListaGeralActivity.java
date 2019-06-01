package com.treinamento.mdomingos.startapp.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

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

public class ListaGeralActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapterContacts userAdapterContacts;
    private List<Usuarios> mUsers;
    private EditText serach_users;
    private ImageView imageSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_geral);

        recyclerView = findViewById(R.id.recycler_view_lista_geral);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        imageSearch = findViewById(R.id.imageview_chat_startup_id);


        mUsers = new ArrayList<>();
        readUser();

    }

    public void readUser(){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (serach_users.getText().toString().equals("")) {
                    mUsers.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);

                        if (!usuarios.getId().equals(firebaseUser.getUid())) {
                            mUsers.add(usuarios);
                        }
                    }
                    userAdapterContacts = new UserAdapterContacts(ListaGeralActivity.this, mUsers, false);
                    recyclerView.setAdapter(userAdapterContacts);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
