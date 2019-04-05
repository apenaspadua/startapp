package com.treinamento.mdomingos.startapp.utils;

import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

/**
 * Created by Matheus de Padua on 05/04/2019.
 * mdomingos@luxfacta.com
 * For Luxfacta Soluções de TI
 * {@see more in https://www.luxfacta.com}
 */
public class MaskFormatter {

    public static void simpleFormatterCell(EditText string){

        SimpleMaskFormatter simpleMaskFormatterTel = new SimpleMaskFormatter("(NN)NNNNN-NNNN"); //Mascara para telefone
        MaskTextWatcher maskTextWatcherTel = new MaskTextWatcher(string, simpleMaskFormatterTel);
        string.addTextChangedListener(maskTextWatcherTel);
    }

    public static void simpleFormatterData(EditText string){
        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NN/NN/NNNN"); //Mascara para data
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(string, simpleMaskFormatter);
        string.addTextChangedListener(maskTextWatcher);
    }

    public static void simpleFormatterCpf(EditText string){

        SimpleMaskFormatter simpleMaskFormatterCpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN"); //Mascara para cpf
        MaskTextWatcher maskTextWatcherCpf = new MaskTextWatcher(string, simpleMaskFormatterCpf);
        string.addTextChangedListener(maskTextWatcherCpf);
    }

    public static void simpleFormatterCnpj(EditText string) {
        SimpleMaskFormatter simpleMaskFormatterCnpj = new SimpleMaskFormatter("NN.NNN.NNN/NNNN-NN"); //Mascara para cnpj
        MaskTextWatcher maskTextWatcherCnpj = new MaskTextWatcher(string, simpleMaskFormatterCnpj);
        string.addTextChangedListener(maskTextWatcherCnpj);
    }
}
