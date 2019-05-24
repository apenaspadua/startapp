package com.treinamento.mdomingos.startapp.fragments_startup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.FeedAdapter;
import com.treinamento.mdomingos.startapp.adapter.NotifyAdapter;
import com.treinamento.mdomingos.startapp.model.Notificacao;
import com.treinamento.mdomingos.startapp.model.Publicacao;

public class NotifyFragment_Startup extends Fragment {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    FirebaseUser user;
    FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify_startup, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView = view.findViewById(R.id.recycler_view_notify_startup);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("Notificacoes").child(user.getUid());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Notificacao, NotifyAdapter> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notificacao, NotifyAdapter>(
                Notificacao.class,
                R.layout.item_notify,
                NotifyAdapter.class,
                mRef
        ) {
            @Override
            protected void populateViewHolder(NotifyAdapter viewHolder, Notificacao model, int position) {
                viewHolder.setDetails(getContext(),
                        model.getFotoPerfil(),
                        model.getDescricao(),
                        model.getId());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
