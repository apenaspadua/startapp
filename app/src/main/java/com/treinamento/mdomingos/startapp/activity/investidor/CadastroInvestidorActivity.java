package com.treinamento.mdomingos.startapp.activity.investidor;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.model.CEP;
import com.treinamento.mdomingos.startapp.model.Investidor;
import com.treinamento.mdomingos.startapp.utils.HttpService;
import com.treinamento.mdomingos.startapp.utils.Validator;

import java.util.concurrent.ExecutionException;

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
        dataNasc = findViewById(R.id.data_nascimento_cadastro_investidor_id);
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
                if (checkedId == R.id.pessoaFisica) {

                    cnpj.setVisibility(View.GONE);
                    inputPj.setVisibility(View.GONE);
                    inputPf.setVisibility(View.VISIBLE);
                    cpf.setVisibility(View.VISIBLE);

                } else if (checkedId == R.id.pessoaJuridica) {

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

                    if (retorno == null) {

                        cep.setError("Cep inválido");
                        progressBar.setVisibility(View.GONE);
                    } else {

                        if (retorno.getCidade() != null) {
                            cidade.setText(retorno.getCidade());
                            estado.setText(retorno.getEstado());
                            rua.setText(retorno.getLogradouro());
                            bairro.setText(retorno.getBairro());
                            progressBar.setVisibility(View.GONE);
                        } else {

                            cep.setError("Cep inválido");
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
                if (FirebaseConection()) {

                    final String nomeRecebido = nome.getText().toString();
                    final String emailRecebido = email.getText().toString();
                    final String dataRecebida = dataNasc.getText().toString();
                    final String cepRecebido = cep.getText().toString();
                    final String ruaRecebida = rua.getText().toString();
                    final String bairroRecebido = bairro.getText().toString();
                    final String cidadeRecebido = cidade.getText().toString();
                    final String estadoRecebido = estado.getText().toString();
                    final String cpfRecebido = cpf.getText().toString();
                    final String cnpjRecebido = cnpj.getText().toString();


                    if (Validator.stringEmpty(nomeRecebido) || nomeRecebido.equals("")) {
                        email.setError("Insira seu nome");

                    } else if (Validator.stringEmpty(emailRecebido) || emailRecebido.equals("")) {
                        email.setError("Insira seu email");

                    } else if (Validator.validateEmailFormat(emailRecebido) == false) {
                        email.setError("Insira um email válido");

                    } else if (Validator.stringEmpty(dataRecebida) || dataRecebida.equals("")) {
                        dataNasc.setError("Insira sua data de nascimento");

                    } else if (Validator.stringEmpty(cepRecebido) || cepRecebido.equals("")) {
                        cpf.setError("Insira seu cep");

                    } else if (Validator.stringEmpty(ruaRecebida) || ruaRecebida.equals("")) {
                        rua.setError("Insira seu logradouro");

                    } else if (Validator.stringEmpty(bairroRecebido) || bairroRecebido.equals("")) {
                        bairro.setError("Insira seu bairro");

                    } else if (Validator.stringEmpty(cidadeRecebido) || cidadeRecebido.equals("")) {
                        cidade.setError("Insira sua cidade");

                    } else if (Validator.stringEmpty(estadoRecebido) || estadoRecebido.equals("")) {
                        estado.setError("Insira seu estado");

                    } else if (Validator.isCPF(cpfRecebido)) {
                        cpf.setError("Insira um cpf válido");

                    } else if (Validator.isCNPJ(cnpjRecebido) == false) {
                        cnpj.setError("Insira um cnpj válido");

                    } else {

                        Investidor investidor = new Investidor();
                        investidor.salvarInvestidor();
                        Toast.makeText(CadastroInvestidorActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Log.i("sem internet", "sem conexao");
                    Toast.makeText(CadastroInvestidorActivity.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
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
