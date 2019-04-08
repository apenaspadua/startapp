package com.treinamento.mdomingos.startapp.activity.investidor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Scroller;

import com.treinamento.mdomingos.startapp.R;

public class BioInvestidorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_investidor);

        final EditText et =  findViewById(R.id.et);

        et.setScroller(new Scroller(getApplicationContext()));
        et.setVerticalScrollBarEnabled(true);

        et.setMinLines(100);
        et.setMaxLines(100);

    }
}
