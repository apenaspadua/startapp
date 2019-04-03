package com.treinamento.mdomingos.startapp.fragments_investidor;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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
import com.treinamento.mdomingos.startapp.model.InvestidorResponse;

public class PerfilFragment_Investidor extends Fragment {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private TextView nome, cidade, empresa, email, data, rua, bairro, estado, telefone;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        View view = inflater.inflate(R.layout.fragment_perfil_investidor, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        nome = view.findViewById(R.id.nome_perfil_investidor_id);
        cidade = view.findViewById(R.id.text_cidade_perfil_investidor);
        empresa = view.findViewById(R.id.text_empresa_perfil_investidor);
        email = view.findViewById(R.id.text_email_perfil_investidor);
        data = view.findViewById(R.id.data_perfil_investidor_id);
        rua = view.findViewById(R.id.rua_perfil_investidor_id);
        bairro = view.findViewById(R.id.bairro_perfil_investidor_id);
        estado = view.findViewById(R.id.estado_perfil_investidor_id);
        telefone = view.findViewById(R.id.telefone_perfil_investidor_id);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Usuarios").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                InvestidorResponse investidor = dataSnapshot.getValue(InvestidorResponse.class);
                nome.setText(investidor.getDetalhe_investidor().getNome());
                cidade.setText(investidor.getDetalhe_investidor().getCidade());
                empresa.setText(investidor.getDetalhe_investidor().getEmpresa());
                email.setText(investidor.getDetalhe_investidor().getEmail());
                data.setText(investidor.getDetalhe_investidor().getData());
                rua.setText(investidor.getDetalhe_investidor().getRua());
                bairro.setText(investidor.getDetalhe_investidor().getBairro());
                estado.setText(investidor.getDetalhe_investidor().getEstado());
                telefone.setText(investidor.getDetalhe_investidor().getTelefone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


}
