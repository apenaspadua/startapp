package com.treinamento.mdomingos.startapp.activity.others;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentInvestidor;
import com.treinamento.mdomingos.startapp.adapter.SliderAdapter;

public class SlidesPosCadastroActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private SliderAdapter sliderAdapter;
    private Button botaoProximo;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slides_pos_cadastro);


        //Intance
        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout =  findViewById(R.id.dotsLayout);
        botaoProximo = findViewById(R.id.botao_slide_proximo_id);

        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDots(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        //OnClick
        botaoProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SlidesPosCadastroActivity.this, BaseFragmentInvestidor.class);
                startActivity(intent);
            }
        });
    }

    public  void  addDots(int positon){

        mDots = new TextView[4];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++){

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparenteDark));

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[positon].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDots(i);
            mCurrentPage = i;

            if(i == 0){

                botaoProximo.setEnabled(true);

                botaoProximo.setText("PULAR INTRO");

            } else if(i == mDots.length -1){

                botaoProximo.setEnabled(true);
                botaoProximo.setText("COMEÇAR");

            } else {

                botaoProximo.setEnabled(true);
                botaoProximo.setText("PULAR INTRO");

            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };


}
