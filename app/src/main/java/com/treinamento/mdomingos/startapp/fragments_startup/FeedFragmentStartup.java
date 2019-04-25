package com.treinamento.mdomingos.startapp.fragments_startup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.FeedAdapter;
import com.treinamento.mdomingos.startapp.utils.UploadStorage;

import java.util.ArrayList;
import java.util.List;

public class FeedFragmentStartup extends Fragment {
    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;

    private DatabaseReference databaseReference;
    private List<UploadStorage> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_startup, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_feed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
         databaseReference = FirebaseDatabase.getInstance().getReference();




        return view;
    }

}
