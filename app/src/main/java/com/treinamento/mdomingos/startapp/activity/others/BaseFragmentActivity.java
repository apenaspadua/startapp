package com.treinamento.mdomingos.startapp.activity.others;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.TabsAdapter;
import com.treinamento.mdomingos.startapp.fragments.FeedFragment;
import com.treinamento.mdomingos.startapp.fragments.NotifyFragment;
import com.treinamento.mdomingos.startapp.fragments.PerfilFragment;

public class BaseFragmentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FeedFragment feedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_base);

        viewPager = findViewById(R.id.vp_pagina_id);
        tabLayout = findViewById(R.id.tabLayout);

        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        feedFragment = new FeedFragment();
        tabsAdapter.addFragment(feedFragment, "feed");
        tabsAdapter.addFragment(new NotifyFragment(), "notificação");
        tabsAdapter.addFragment(new PerfilFragment(), "perfil");

        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_feed);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_notifications);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_account);


    }

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
}
