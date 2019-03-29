package com.treinamento.mdomingos.startapp.activity.investidor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.login.CadastroLoginActivity;
import com.treinamento.mdomingos.startapp.model.CEP;
import com.treinamento.mdomingos.startapp.utils.HttpService;
import com.treinamento.mdomingos.startapp.utils.Validator;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class CadastroInvestidorActivity extends AppCompatActivity {

    private EditText nome, email, cep, cpf, cnpj, dataNasc, rua, cidade, bairro, estado;
    private RadioGroup radioGroup;
    private TextInputLayout inputPf, inputPj;
    private RelativeLayout botaoConcluir;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_investidor);

        nome = findViewById(R.id.nome_cadastro_investidor_id);
        email = findViewById(R.id.email_cadastro_investidor_id);
        cep = findViewById(R.id.cep_cadastro_investidor_id);
        cpf = findViewById(R.id.cpf_cadastro_investidor_id);
        cnpj = findViewById(R.id.cnpj_cadastro_investidor_id);
        rua = findViewById(R.id.rua_cadastro_investidor_id);
        cidade = findViewById(R.id.cidade_cadastro_investidor_id);
        bairro = findViewById(R.id.bairro_cadastro_investidor_id);
        estado = findViewById(R.id.estado_cadastro_investidor_id);
        inputPf = findViewById(R.id.textInputPF);
        inputPj = findViewById(R.id.textInputPJ);
        botaoConcluir = findViewById(R.id.botao_concluir_cadastro_investidor_id);
        radioGroup = findViewById(R.id.radioGroup);
        progressBar = findViewById(R.id.cep_progress_bar_id);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 if(checkedId == R.id.pessoaFisica){

                     cnpj.setVisibility(View.GONE);
                     inputPj.setVisibility(View.GONE);
                     inputPf.setVisibility(View.VISIBLE);
                     cpf.setVisibility(View.VISIBLE);

                 } else if (checkedId == R.id.pessoaJuridica){

                     cnpj.setVisibility(View.VISIBLE);
                     inputPj.setVisibility(View.VISIBLE);
                     inputPf.setVisibility(View.GONE);
                     cpf.setVisibility(View.GONE);

                 }
            }
        });


        cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    CEP retorno = new HttpService(s.toString()).execute().get();

                    if(retorno == null){

                        cep.setError("Cep inválido");
                        progressBar.setVisibility(View.GONE);
                    } else {

                        if(retorno.getCidade() != null) {
                            cidade.setText(retorno.getCidade());
                            estado.setText(retorno.getEstado());
                            rua.setText(retorno.getLogradouro());
                            bairro.setText(retorno.getBairro());
                            progressBar.setVisibility(View.GONE);
                        } else {

                            cep.setError("teste");
                            progressBar.setVisibility(View.GONE);
                        }


                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        botaoConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nomeRecebido = nome.getText().toString();
                final String emailRecebido = email.getText().toString();
                final String dataRecebida = dataNasc.getText().toString();
                final String cepRecebido = cep.getText().toString();
                final String cpfRecebido = cpf.getText().toString();
                final String cnpjRecebido = cnpj.getText().toString();


                if (Validator.stringEmpty(nomeRecebido) || nomeRecebido.equals("")) {
                    email.setError("Insira seu nome");

                }else if(Validator.stringEmpty(emailRecebido) || emailRecebido.equals("")){
                    email.setError("Insira seu email");

                } else if (Validator.validateEmailFormat(emailRecebido) == false) {
                    email.setError("Insira um email válido");

                }else if (Validator.stringEmpty(dataRecebida) || dataRecebida.equals("")){
                    dataNasc.setError("Insira sua data de nascimento");

                } else if (Validator.isCPF(cpfRecebido) == false) {
                    cpf.setError("Insira um cpf válido");

                } else if (Validator.isCNPJ(cnpjRecebido) == false) {
                    cnpj.setError("Insira um cnpj válido");

                } else {



                    if(FirebaseConection()){



                    } else {

                        Log.i("sem internet", "sem conexao");
                        Toast.makeText(CadastroInvestidorActivity.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                    }




                }







            }
        });


    }



    public boolean FirebaseConection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) // conectado a internet
            return true;
        return false; // nao conectado
    }
}
