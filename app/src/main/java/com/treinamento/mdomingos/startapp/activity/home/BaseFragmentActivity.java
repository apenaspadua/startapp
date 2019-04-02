package com.treinamento.mdomingos.startapp.activity.home;

import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.TabsAdapter;
import com.treinamento.mdomingos.startapp.fragments_investidor.FeedFragment_Investidor;
import com.treinamento.mdomingos.startapp.fragments_investidor.NotifyFragment_Investidor;
import com.treinamento.mdomingos.startapp.fragments_investidor.PerfilFragment_Investidor;

public class BaseFragmentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FeedFragment_Investidor feedFragmentInvestidor;
    private Toolbar toolbar;
//    private TextView titulo;
//    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_base);

//        titulo = findViewById(R.id.title_toolbar_id);
        viewPager = findViewById(R.id.vp_pagina_id);
        tabLayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.ToolBar_feed_investidor);
//        imageView = findViewById(R.id.imageProfile_id);

        setSupportActionBar(toolbar);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        feedFragmentInvestidor = new FeedFragment_Investidor();
        tabsAdapter.addFragment(feedFragmentInvestidor, "");
        tabsAdapter.addFragment(new NotifyFragment_Investidor(), "");
        tabsAdapter.addFragment(new PerfilFragment_Investidor(), "");

        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_feed_blue);
//        titulo.setText("feed");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_notifications);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_account);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
//                    titulo.setText("feed");
//                    toolbar.setBackgroundResource(R.color.colorPrimaryDark);
//                    imageView.setVisibility(View.VISIBLE);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_feed_blue);

                } else if (tab.getPosition() == 1) {
//                    titulo.setText("notify");
//                    toolbar.setBackgroundResource(R.color.colorPrimaryDark);
//                    imageView.setVisibility(View.VISIBLE);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_notifications_blue);

                } else if (tab.getPosition() == 2) {
//                    titulo.setText("perfil");
//                    toolbar.setBackgroundResource(R.color.colorTranspatenteLight);

//                    imageView.setVisibility(View.GONE);

                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_account_blue);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_feed);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_notifications);
                tabLayout.getTabAt(2).setIcon(R.drawable.ic_account);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });
    }
}
