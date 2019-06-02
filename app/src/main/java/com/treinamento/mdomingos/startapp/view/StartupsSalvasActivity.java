package com.treinamento.mdomingos.startapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.treinamento.mdomingos.startapp.adapter.ListStartusAdapter;
import com.treinamento.mdomingos.startapp.model.Salvos;

import java.util.ArrayList;
import java.util.List;

public class StartupsSalvasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListStartusAdapter listStartusAdapter;
    private List<Salvos> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_geral);

        recyclerView = findViewById(R.id.recycler_view_lista_geral);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUsers = new ArrayList<>();
        readUser();

    }

    public void readUser(){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Salvos").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Salvos salvos = snapshot.getValue(Salvos.class);
                            mUsers.add(salvos);
                    }
                    listStartusAdapter = new ListStartusAdapter(StartupsSalvasActivity.this, mUsers);
                    recyclerView.setAdapter(listStartusAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
