package com.treinamento.mdomingos.startapp.fragments_startup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.ViewHolder;
import com.treinamento.mdomingos.startapp.model.Publicacao;

public class FeedFragmentStartup extends Fragment {

     RecyclerView recyclerView;
     FirebaseDatabase firebaseDatabase;
     DatabaseReference mRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_startup, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_feed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("Publicacoes");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Publicacao, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Publicacao, ViewHolder>(
                Publicacao.class,
                R.layout.publicacao_item,
                ViewHolder.class,
                mRef
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Publicacao model, int position) {

                viewHolder.setDetails(getContext(), model.getNomeUsuario(), model.getCidade(), model.getEstado(),
                        model.getDescricao(), model.getImagemPerfil(), model.getImagemPublicacao());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
