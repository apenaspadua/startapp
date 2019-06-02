package com.treinamento.mdomingos.startapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.model.InvestidorResponse;
import com.treinamento.mdomingos.startapp.view.ConfigActivity;
import com.treinamento.mdomingos.startapp.view.EditarPerfilInvestidorActivity;
import com.treinamento.mdomingos.startapp.view.LoginActivity;
import com.treinamento.mdomingos.startapp.view.StartupsSalvasActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragmentInvestidor extends Fragment {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private TextView nome, cidade, empresa, email, rua, bairro, estado, telefone, bio, editar, apresentacao;
    private ProgressDialog progressDialog;
    private CircleImageView foto;
    private Task<Uri> storageReference;
    private FirebaseUser user;
    private String imageURL;
    private ProgressBar progressBar;
    private RelativeLayout verSalvos;

    @Override
    public void onStart() {
        super.onStart();
        loadUserInformation();
        progressBar.setVisibility(View.VISIBLE);
        editar.setTextColor(Color.parseColor("#57BC90"));
    }

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
        } else if (item.getItemId() == R.id.editar_perfil_item_dropdown_menu_id){
            startActivity(new Intent(getActivity(), EditarPerfilInvestidorActivity.class));
            getActivity().finish();

        }else if (item.getItemId() == R.id.config_item_dropdown_menu_id){
            startActivity(new Intent(getActivity(), ConfigActivity.class));
            getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        View view = inflater.inflate(R.layout.fragment_perfil_investidor, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();

        editar = view.findViewById(R.id.text_editar_investidor_startup);
        foto = view.findViewById(R.id.foto_perfil_investidor_id);
        nome = view.findViewById(R.id.nome_perfil_investidor_id);
        cidade = view.findViewById(R.id.text_cidade_perfil_investidor);
        empresa = view.findViewById(R.id.text_empresa_perfil_investidor);
        email = view.findViewById(R.id.text_email_perfil_investidor);
        rua = view.findViewById(R.id.rua_perfil_investidor_id);
        bairro = view.findViewById(R.id.bairro_perfil_investidor_id);
        estado = view.findViewById(R.id.estado_perfil_investidor_id);
        telefone = view.findViewById(R.id.telefone_perfil_investidor_id);
        bio = view.findViewById(R.id.text_biografia_perfil_investidor);
        apresentacao = view.findViewById(R.id.text_apresentacao_perfil_investidor);
        progressDialog = new ProgressDialog(getActivity());
        progressBar = view.findViewById(R.id.progressBar_perfil_investidor);
        verSalvos = view.findViewById(R.id.botao_ver_salvos_investidor_id);

        verSalvos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StartupsSalvasActivity.class));
            }
        });


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                InvestidorResponse investidor = dataSnapshot.getValue(InvestidorResponse.class);
                nome.setText(investidor.getDetalhe_investidor().getNome());
                cidade.setText(investidor.getDetalhe_investidor().getCidade());
                empresa.setText(investidor.getDetalhe_investidor().getEmpresa());
                email.setText(investidor.getDetalhe_investidor().getEmail());
                rua.setText(investidor.getDetalhe_investidor().getRua());
                bairro.setText(investidor.getDetalhe_investidor().getBairro());
                estado.setText(investidor.getDetalhe_investidor().getEstado());
                telefone.setText(investidor.getDetalhe_investidor().getTelefone());
                bio.setText(investidor.getDetalhe_investidor().getBiografia());
                apresentacao.setText(investidor.getDetalhe_investidor().getApresentacao());

                editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editar.setTextColor(Color.parseColor("#0289BE"));
                        startActivity(new Intent(getActivity(), EditarPerfilInvestidorActivity.class));
                        getActivity().finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;

    }

    private void loadUserInformation() {
        storageReference = FirebaseStorage.getInstance().getReference().child("foto_perfil").
                child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                 @Override
                 public void onSuccess(Uri uri) {
                   imageURL = uri.toString();
                    Glide.with(getActivity()).load(imageURL).into(foto);
                    progressBar.setVisibility(View.GONE);
                 }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressBar.setVisibility(View.GONE);
                }
        });
    }
}
