package com.treinamento.mdomingos.startapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.FeedAdapter;
import com.treinamento.mdomingos.startapp.model.Publicacao;

public class FeedFragmentInvestidor extends Fragment {
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        setHasOptionsMenu(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_investidor, container, false);
        setHasOptionsMenu(false);

        recyclerView = view.findViewById(R.id.recycler_view_feed_investidor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("Publicacoes");

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Publicacao, FeedAdapter> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Publicacao, FeedAdapter>(
                Publicacao.class,
                R.layout.publicacao_item,
                FeedAdapter.class,
                mRef
        ) {
            @Override
            protected void populateViewHolder(FeedAdapter viewHolder, Publicacao model, int position) {
                viewHolder.setDetails(getContext(),
                        model.getNomeFantasia(),
                        model.getCidade(),
                        model.getEstado(),
                        model.getDescricao(),
                        model.getFotoPerfil(),
                        model.getFotoPublicacao(),
                        model.getId());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
