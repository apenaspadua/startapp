package com.treinamento.mdomingos.startapp.utils;

import java.util.ArrayList;

public class Validator {

    public static boolean stringEmpty(String string){

        if(string.isEmpty())
            return true;
        else
            return false;
    }


    public static boolean listLessThantwo(ArrayList arrayList){

        if(arrayList.size() < 2)
            return true;
        else
            return false;
    }


}
