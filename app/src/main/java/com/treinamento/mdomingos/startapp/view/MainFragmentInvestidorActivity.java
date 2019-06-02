package com.treinamento.mdomingos.startapp.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.TabsAdapter;
import com.treinamento.mdomingos.startapp.fragment.FeedFragmentInvestidor;
import com.treinamento.mdomingos.startapp.fragment.NotifyFragmentInvestidor;
import com.treinamento.mdomingos.startapp.fragment.PerfilFragmentInvestidor;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainFragmentInvestidorActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FeedFragmentInvestidor feedFragmentInvestidor;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private TextView titulo;
    private ImageView imageViewChat, imageViewList;
    private CircleImageView imageViewProfile;
    private String imageURL;
    private Task<Uri> storageReference;
    private FirebaseUser user;
    private ProgressBar progressBar;


    @Override
    protected void onResume() {
        super.onResume();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainFragmentInvestidorActivity.this, LoginActivity.class));
            finish();
            return;
        }

        loadUserInformation();
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment_investidor);

        titulo = findViewById(R.id.titulo_home_investidor_id);
        viewPager = findViewById(R.id.vp_pagina_id);
        tabLayout = findViewById(R.id.tabLayout);
        imageViewProfile = findViewById(R.id.imageview_home_investidor_id);
        imageViewChat = findViewById(R.id.imageview_chat_investidor_id);
        imageViewList = findViewById(R.id.imageview_lista_geral_investidor_id);
        toolbar = findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(this);
        progressBar = findViewById(R.id.progressBar_base_investidor);

        setSupportActionBar(toolbar);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        feedFragmentInvestidor = new FeedFragmentInvestidor();
        tabsAdapter.addFragment(feedFragmentInvestidor, "");
        tabsAdapter.addFragment(new NotifyFragmentInvestidor(), "");
        tabsAdapter.addFragment(new PerfilFragmentInvestidor(), "");

        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_ouline);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_notification_outline);
        tabLayout.getTabAt(2).setIcon(R.drawable.profile_outline);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    toolbar.setBackgroundResource(R.color.marine);
                    titulo.setText("StartApp");
                    titulo.setVisibility(View.VISIBLE);
                    imageViewProfile.setVisibility(View.VISIBLE);
                    imageViewChat.setVisibility(View.VISIBLE);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_ouline);

                } else if (tab.getPosition() == 1) {
                    toolbar.setBackgroundResource(R.color.marine);
                    titulo.setText("Minhas notificações");
                    titulo.setVisibility(View.VISIBLE);
                    imageViewProfile.setVisibility(View.GONE);
                    imageViewChat.setVisibility(View.VISIBLE);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_notification_online);

                } else if (tab.getPosition() == 2) {
                    toolbar.setBackgroundResource(R.color.colorTranspatenteLight);
                    titulo.setVisibility(View.GONE);
                    imageViewChat.setVisibility(View.GONE);
                    imageViewProfile.setVisibility(View.GONE);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_profile_online);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_outline);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_notification_outline);
                tabLayout.getTabAt(2).setIcon(R.drawable.profile_outline);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        imageViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainFragmentInvestidorActivity.this, ChatActivity.class));

            }
        });


        titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainFragmentInvestidorActivity.this, SlideActivity.class));

            }
        });

        imageViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainFragmentInvestidorActivity.this, StartupsSalvasActivity.class));
            }
        });
    }
    private void loadUserInformation() {
        storageReference = FirebaseStorage.getInstance().getReference().child("foto_perfil").
                child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                imageURL = uri.toString();
                progressBar.setVisibility(View.GONE);
                Glide.with(getApplicationContext()).load(imageURL).into(imageViewProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }

}


