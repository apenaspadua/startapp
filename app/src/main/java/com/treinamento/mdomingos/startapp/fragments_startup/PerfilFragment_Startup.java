package com.treinamento.mdomingos.startapp.fragments_startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.investidor.EditarPerfilInvestidorActivity;
import com.treinamento.mdomingos.startapp.activity.login.LoginActivity;
import com.treinamento.mdomingos.startapp.activity.startup.EditarPerfilStartup;
import com.treinamento.mdomingos.startapp.model.InvestidorResponse;
import com.treinamento.mdomingos.startapp.model.StartupResponse;


public class PerfilFragment_Startup extends Fragment {

    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private TextView nome, cidade, razao, email, rua, bairro, estado, telefone, bio;

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
        } else {
            if (item.getItemId() == R.id.editar_perfil_item_dropdown_menu_id){
                startActivity(new Intent(getActivity(), EditarPerfilStartup.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        View view = inflater.inflate(R.layout.fragment_perfil_startup, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        nome = view.findViewById(R.id.nome_perfil_startup_id);
        cidade = view.findViewById(R.id.text_cidade_perfil_startup);
        razao = view.findViewById(R.id.text_razao_perfil_startup);
        email = view.findViewById(R.id.text_email_perfil_startup);
        rua = view.findViewById(R.id.rua_perfil_startup_id);
        bairro = view.findViewById(R.id.bairro_perfil_startup_id);
        estado = view.findViewById(R.id.estado_perfil_startup_id);
        telefone = view.findViewById(R.id.telefone_perfil_startup_id);
        bio = view.findViewById(R.id.text_biografia_perfil_startup);
        progressDialog = new ProgressDialog(getActivity());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StartupResponse startup = dataSnapshot.getValue(StartupResponse.class);
                nome.setText(startup.getDetalhe_startup().getNomeFantasia());
                cidade.setText(startup.getDetalhe_startup().getCidade());
                razao.setText(startup.getDetalhe_startup().getRazaoSocial());
                email.setText(startup.getDetalhe_startup().getEmail());
                rua.setText(startup.getDetalhe_startup().getRua());
                bairro.setText(startup.getDetalhe_startup().getBairro());
                estado.setText(startup.getDetalhe_startup().getEstado());
                telefone.setText(startup.getDetalhe_startup().getTelefone());
                bio.setText(startup.getDetalhe_startup().getBiografia());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

}
