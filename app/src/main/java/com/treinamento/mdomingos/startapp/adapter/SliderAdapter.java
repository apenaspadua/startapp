package com.treinamento.mdomingos.startapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.treinamento.mdomingos.startapp.R;

/**
 * Created by Matheus de Padua on 25/03/2019.
 * mdomingos@luxfacta.com
 * For Luxfacta Soluções de TI
 * {@see more in https://www.luxfacta.com}
 */
public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public  SliderAdapter(Context context){
        this.context = context;
    }


    // list of titles
    public String[] lst_title = {
            "BEM VINDO A STARTAAP",
            "PARA STARTUP'S",
            "PARA INVESTIDORES",
            "APROVEITE"
    };

    // list of descriptions
    public String[] lst_description = {
            "Nosso objetivo aqui é apenas um, encontrar a melhor opçāo de investidor e investimento.",
            "Apresente seu projeto, mostre o quanto é incrível e inovador, assim ganhe investidores.",
            "Conheça e escolha os projetos que mais combine com você e invista sem medo. ",
            "Aqui é uma terra sem lei, cadastre-se e encontre o sucesso!"
    };

    // list of background colors
    public int[]  lst_backgroundcolor = {
            Color.rgb(55,55,55),
            Color.rgb(239,85,85),
            Color.rgb(110,49,89),
            Color.rgb(55,55,55),
    };

    @Override
    public int getCount() {
        return lst_description.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        TextView slideHeading = view.findViewById(R.id.title_Slide_id);
        TextView slideDescription = view.findViewById(R.id.subtitle_slide_id);
        LinearLayout layoutslide = view.findViewById(R.id.slideLinearLayout);


        slideHeading.setText(lst_title[position]);
        slideDescription.setText(lst_description[position]);
        layoutslide.setBackgroundColor(lst_backgroundcolor[position]);


        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}


