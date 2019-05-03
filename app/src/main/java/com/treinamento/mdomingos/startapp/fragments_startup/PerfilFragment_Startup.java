package com.treinamento.mdomingos.startapp.fragments_startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.login.LoginActivity;
import com.treinamento.mdomingos.startapp.activity.startup.EditarPerfilStartupActivity;
import com.treinamento.mdomingos.startapp.activity.startup.EnviaArquivosActivity;
import com.treinamento.mdomingos.startapp.model.StartupResponse;
import com.treinamento.mdomingos.startapp.utils.UploadStorage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class PerfilFragment_Startup extends Fragment {

    private BarChart barChart;
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private Task<Uri> storageReference;
    private FirebaseUser user;
    private String imageURL;
    private ProgressBar progressBar;
    private TextView nome, cidade, razao, email, rua, bairro, estado, telefone, bio, editar, apresentacao, link ;
    private CircleImageView foto;
    private RelativeLayout editarVideo;
    private FirebaseStorage storage;

    private VideoView videoView;
    private Uri videoUri;
    private MediaController mediaController;

    @Override
    public void onStart() {
        super.onStart();
        loadUserInformation();
        progressBar.setVisibility(View.VISIBLE);
        editar.setTextColor(Color.parseColor("#57BC90"));
        loadVideo();
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
        } else {
            if (item.getItemId() == R.id.editar_perfil_item_dropdown_menu_id){
                startActivity(new Intent(getActivity(), EditarPerfilStartupActivity.class));
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
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();

        barChart = view.findViewById(R.id.grafico_perfil_startup);
        editar = view.findViewById(R.id.text_editar_perfil_startup);
        nome = view.findViewById(R.id.nome_perfil_startup_id);
        cidade = view.findViewById(R.id.text_cidade_perfil_startup);
        razao = view.findViewById(R.id.text_razao_perfil_startup);
        email = view.findViewById(R.id.text_email_perfil_startup);
        rua = view.findViewById(R.id.rua_perfil_startup_id);
        bairro = view.findViewById(R.id.bairro_perfil_startup_id);
        estado = view.findViewById(R.id.estado_perfil_startup_id);
        telefone = view.findViewById(R.id.telefone_perfil_startup_id);
        bio = view.findViewById(R.id.text_biografia_perfil_startup);
        apresentacao = view.findViewById(R.id.text_apresentacao_perfil_startup);
        link = view.findViewById(R.id.text_link_perfil_startup);
        foto = view.findViewById(R.id.foto_perfil_startup_id);
        videoView = view.findViewById(R.id.upload_video_id);
        editarVideo = view.findViewById(R.id.botao_editar_projeto_startup_id);
        progressBar = view.findViewById(R.id.progressBar_perfil_startup);
        progressDialog = new ProgressDialog(getActivity());

        //grafico
        barChart.getDescription().setEnabled(false);
        setData(2);
        barChart.setFitBars(true);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar.setTextColor(Color.parseColor("#0289BE"));
                startActivity(new Intent(getActivity(), EditarPerfilStartupActivity.class));
            }
        });

        editarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EnviaArquivosActivity.class));
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mediaController = new MediaController(getContext());
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });
        videoView.start();


            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
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
                    apresentacao.setText(startup.getDetalhe_startup().getApresentacao());
                    link.setText(startup.getDetalhe_startup().getLink());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return view;
    }

    private void setData(int count){
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for(int i = 0; i < count; i++)
        {
            if(i == 1) {
                double value = 100;
                barEntries.add(new BarEntry(i, (int) value));
            } else{
                double value2 = 101;
                barEntries.add(new BarEntry(i, (int) value2));
            }
        }

        BarDataSet set = new BarDataSet(barEntries, "Meta em R$ / Investido");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);

        BarData data = new BarData(set);

        barChart.setData(data);
        barChart.invalidate();
        barChart.animateY(500);
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

    private void loadVideo(){
        storageReference = FirebaseStorage.getInstance().getReference().child("video_publicacao").
                child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {

                videoUri = Uri.parse(uri.toString());
                videoView.setVideoURI(videoUri);
                videoView.requestFocus();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

}
