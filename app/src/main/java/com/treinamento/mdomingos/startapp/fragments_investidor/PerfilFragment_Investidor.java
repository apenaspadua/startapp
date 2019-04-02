package com.treinamento.mdomingos.startapp.fragments_investidor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.treinamento.mdomingos.startapp.R;

public class PerfilFragment_Investidor extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);


//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");


        return inflater.inflate(R.layout.fragment_perfil_investidor, container, false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dropdown_menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sobre_item_dropdown_menu_id){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();

        }
        return super.onOptionsItemSelected(item);
    }
}
