package com.treinamento.mdomingos.startapp.activity.home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.TabsAdapter;
import com.treinamento.mdomingos.startapp.fragments.FeedFragment;
import com.treinamento.mdomingos.startapp.fragments.NotifyFragment;
import com.treinamento.mdomingos.startapp.fragments.PerfilFragment;

public class BaseFragmentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FeedFragment feedFragment;
    private Toolbar toolbar;
    private TextView titulo;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dropdown_menu_home, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_base);

        titulo = findViewById(R.id.title_toolbar_id);
        viewPager = findViewById(R.id.vp_pagina_id);
        tabLayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.toolbar);


        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        feedFragment = new FeedFragment();
        tabsAdapter.addFragment(feedFragment, "");
        tabsAdapter.addFragment(new NotifyFragment(), "");
        tabsAdapter.addFragment(new PerfilFragment(), "");

        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_feed_blue);
        titulo.setText("feed");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_notifications);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_account);


        setSupportActionBar(toolbar);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    titulo.setText("feed");
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_feed_blue);

                } else if (tab.getPosition() == 1) {
                    titulo.setText("notify");
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_notifications_blue);

                } else if (tab.getPosition() == 2) {
                    titulo.setText("perfil");
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
