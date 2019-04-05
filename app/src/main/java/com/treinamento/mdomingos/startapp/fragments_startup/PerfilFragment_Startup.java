package com.treinamento.mdomingos.startapp.fragments_startup;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.treinamento.mdomingos.startapp.activity.investidor.EditarPerfilInvestidorActivity;
import com.treinamento.mdomingos.startapp.activity.login.LoginActivity;


public class PerfilFragment_Startup extends Fragment {

    private ProgressDialog progressDialog;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.dropdown_menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout_item_dropdown_menu_id){
            progressDialog.setMessage("Saindo...");
            progressDialog.show();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
//        } else {
//            if (item.getItemId() == R.id.editar_perfil_item_dropdown_menu_id){
//                startActivity(new Intent(getActivity(), EditarPerfilInvestidorActivity.class));
//            }
        }
        return super.onOptionsItemSelected(item);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        View view = inflater.inflate(R.layout.fragment_perfil_startup, container, false);

        progressDialog = new ProgressDialog(getActivity());


        return view;



    }

}
