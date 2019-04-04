package com.treinamento.mdomingos.startapp.activity.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.inicial.InicialActivity;
import com.treinamento.mdomingos.startapp.activity.investidor.CadastroInvestidorActivity;
import com.treinamento.mdomingos.startapp.activity.investidor.EditarPerfilInvestidorActivity;
import com.treinamento.mdomingos.startapp.activity.login.LoginActivity;
import com.treinamento.mdomingos.startapp.adapter.TabsAdapter;
import com.treinamento.mdomingos.startapp.fragments_investidor.FeedFragment_Investidor;
import com.treinamento.mdomingos.startapp.fragments_investidor.NotifyFragment_Investidor;
import com.treinamento.mdomingos.startapp.fragments_investidor.PerfilFragment_Investidor;

public class BaseFragmentInvestidor extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FeedFragment_Investidor feedFragmentInvestidor;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private TextView titulo;
    private ImageView imageViewProfile, imageViewChat, imageViewBack;

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(BaseFragmentInvestidor.this, LoginActivity.class));
            finish();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.dropdown_menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout_item_dropdown_menu_id){
            progressDialog.setMessage("Saindo...");
            progressDialog.show();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(BaseFragmentInvestidor.this, LoginActivity.class));
            finish();
        } else {
            if (item.getItemId() == R.id.editar_perfil_item_dropdown_menu_id){
                startActivity(new Intent(BaseFragmentInvestidor.this, EditarPerfilInvestidorActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_base_investidor);

        titulo = findViewById(R.id.titulo_home_investidor_id);
        viewPager = findViewById(R.id.vp_pagina_id);
        tabLayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.ToolBar_feed_investidor);
        imageViewProfile = findViewById(R.id.imageview_home_investidor_id);
        imageViewChat = findViewById(R.id.imageview_chat_investidor_id);
        imageViewBack = findViewById(R.id.imageview_back_investidor_id);
        toolbar = findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(this);

        setSupportActionBar(toolbar);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        feedFragmentInvestidor = new FeedFragment_Investidor();
        tabsAdapter.addFragment(feedFragmentInvestidor, "");
        tabsAdapter.addFragment(new NotifyFragment_Investidor(), "");
        tabsAdapter.addFragment(new PerfilFragment_Investidor(), "");

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
                    toolbar.setBackgroundResource(R.color.colorPrimaryDark);
                    titulo.setText("StartApp");
                    titulo.setVisibility(View.VISIBLE);
                    imageViewProfile.setVisibility(View.VISIBLE);
                    imageViewChat.setVisibility(View.VISIBLE);
                    imageViewBack.setVisibility(View.GONE);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_ouline);

                } else if (tab.getPosition() == 1) {
                    toolbar.setBackgroundResource(R.color.colorPrimaryDark);
                    titulo.setText("Minhas notificações");
                    titulo.setVisibility(View.VISIBLE);
                    imageViewProfile.setVisibility(View.GONE);
                    imageViewChat.setVisibility(View.VISIBLE);
                    imageViewBack.setVisibility(View.GONE);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_notification_online);

                } else if (tab.getPosition() == 2) {
                    toolbar.setBackgroundResource(R.color.colorTranspatenteLight);
                    titulo.setVisibility(View.GONE);
                    imageViewChat.setVisibility(View.GONE);
                    imageViewProfile.setVisibility(View.GONE);
                    imageViewBack.setVisibility(View.VISIBLE);
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
    }

}
