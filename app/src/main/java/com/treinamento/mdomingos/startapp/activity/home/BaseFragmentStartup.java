package com.treinamento.mdomingos.startapp.activity.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.treinamento.mdomingos.startapp.activity.login.LoginActivity;
import com.treinamento.mdomingos.startapp.activity.others.ListaGeralActivity;
import com.treinamento.mdomingos.startapp.activity.others.SlideActivity;
import com.treinamento.mdomingos.startapp.activity.others.SlidesPosCadastroActivity;
import com.treinamento.mdomingos.startapp.adapter.TabsAdapter;
import com.treinamento.mdomingos.startapp.activity.chat.ChatActivity;
import com.treinamento.mdomingos.startapp.fragments_startup.FeedFragmentStartup;
import com.treinamento.mdomingos.startapp.fragments_startup.NotifyFragment_Startup;
import com.treinamento.mdomingos.startapp.fragments_startup.PerfilFragment_Startup;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseFragmentStartup extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FeedFragmentStartup feedFragmentStartup;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private TextView titulo;
    private FirebaseUser user;
    private String imageURL;
    private Task<Uri> storageReference;
    private ImageView imageViewChat, imageViewList;
    private CircleImageView imageViewProfile;
    private ProgressBar progressBar;


    @Override
    protected void onResume() {
        super.onResume();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(BaseFragmentStartup.this, LoginActivity.class));
            finish();
            return;
        }

        loadUserInformation();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment_startup);

        titulo = findViewById(R.id.titulo_home_startup_id);
        viewPager = findViewById(R.id.vp_pagina_startup_id);
        tabLayout = findViewById(R.id.tabLayout_startup);
        imageViewProfile = findViewById(R.id.imageview_home_startup_id);
        imageViewChat = findViewById(R.id.imageview_chat_startup_id);
        imageViewList = findViewById(R.id.imageview_lista_geral_startup_id);
        toolbar = findViewById(R.id.toolbar_startup);
        progressBar = findViewById(R.id.progressBar_base_startup);
        progressDialog = new ProgressDialog(this);

        setSupportActionBar(toolbar);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        feedFragmentStartup = new FeedFragmentStartup();
        tabsAdapter.addFragment(feedFragmentStartup, "");
        tabsAdapter.addFragment(new NotifyFragment_Startup(), "");
        tabsAdapter.addFragment(new PerfilFragment_Startup(), "");

        viewPager.setAdapter(tabsAdapter);
//        viewPager.setPageTransformer(true, new AccordionTransformer());
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
                startActivity(new Intent(BaseFragmentStartup.this, ChatActivity.class));

            }
        });

        titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseFragmentStartup.this, SlideActivity.class));

            }
        });

        imageViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseFragmentStartup.this, ListaGeralActivity.class));
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
