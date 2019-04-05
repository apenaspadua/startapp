package com.treinamento.mdomingos.startapp.activity.startup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentInvestidor;
import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentStartup;
import com.treinamento.mdomingos.startapp.activity.investidor.CadastroInvestidorActivity;
import com.treinamento.mdomingos.startapp.model.CEP;
import com.treinamento.mdomingos.startapp.model.Startup;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;
import com.treinamento.mdomingos.startapp.utils.HttpService;
import com.treinamento.mdomingos.startapp.utils.MaskFormatter;
import com.treinamento.mdomingos.startapp.utils.Validator;

import java.util.concurrent.ExecutionException;

public class CadastroStartupActivity extends AppCompatActivity {

    private EditText razaoSocial, nomeFantasia, email, cep, cnpj, rua, cidade, bairro, estado;
    private RelativeLayout botaoConcluir;
    private TextView irPerfil;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_startup);

        razaoSocial = findViewById(R.id.razao_social_cadastro_startup_id);
        nomeFantasia = findViewById(R.id.nome_cadastro_startup_id);
        email = findViewById(R.id.email_cadastro_startup_id);
        cep = findViewById(R.id.cep_cadastro_startup_id);
        cnpj = findViewById(R.id.cnpj_cadastro_startup_id);
        rua = findViewById(R.id.rua_cadastro_startup_id);
        cidade = findViewById(R.id.cidade_cadastro_startup_id);
        bairro = findViewById(R.id.bairro_cadastro_startup_id);
        estado = findViewById(R.id.estado_cadastro_startup_id);
        botaoConcluir = findViewById(R.id.botao_terminar_cadastro_startup_id);
        irPerfil = findViewById(R.id.ir_para_perfil_cadastro_startup);
        progressDialog = new ProgressDialog(CadastroStartupActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //Mascara para cnpj
        MaskFormatter.simpleFormatterCnpj(cnpj);

        irPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irPerfil.setTextColor(Color.parseColor("#0289BE"));
                startActivity(new Intent(CadastroStartupActivity.this, BaseFragmentInvestidor.class));
                finish();
            }
        });


        cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    CEP retorno = new HttpService(s.toString()).execute().get();

                    if (retorno == null) {
                        cep.setError("Cep inválido");
                    } else {
                        if (retorno.getCidade() != null) {
                            cidade.setText(retorno.getCidade());
                            estado.setText(retorno.getEstado());
                            rua.setText(retorno.getLogradouro());
                            bairro.setText(retorno.getBairro());
                        } else {
                            cep.setError("Cep inválido");
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        botaoConcluir.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        if(FirebaseConfig.firebaseConection()){

            final String razaoSocialRecebido = razaoSocial.getText().toString();
            final String nomeFantasiaRecebido = nomeFantasia.getText().toString();
            final String emailRecebido = email.getText().toString();
            final String cepRecebido = cep.getText().toString();
            final String ruaRecebida = rua.getText().toString();
            final String bairroRecebido = bairro.getText().toString();
            final String cidadeRecebido = cidade.getText().toString();
            final String estadoRecebido = estado.getText().toString();
            final String cnpjRecebido = cnpj.getText().toString();

            String cnpjSemFormatacao = cnpjRecebido.replace(".", "");
            cnpjSemFormatacao = cnpjSemFormatacao.replace("/", "");
            cnpjSemFormatacao = cnpjSemFormatacao.replace("-", "");

            if (Validator.stringEmpty(razaoSocialRecebido)) {
                razaoSocial.setError("Insira sua Razão Social");

            } else if (Validator.stringEmpty(nomeFantasiaRecebido)) {
                nomeFantasia.setError("Insira o nome da empresa");

            } else if (Validator.stringEmpty(emailRecebido)) {
                email.setError("Insira seu email");

            } else if (Validator.validateEmailFormat(emailRecebido) ==  false) {
                email.setError("Insira um email válido");

            } else if (Validator.stringEmpty(cepRecebido)) {
                cep.setError("Insira seu cep");

            } else if (Validator.stringEmpty(ruaRecebida)) {
                rua.setError("Insira seu logradouro");

            } else if (Validator.stringEmpty(bairroRecebido)) {
                bairro.setError("Insira seu bairro");

            } else if (Validator.stringEmpty(cidadeRecebido)) {
                cidade.setError("Insira sua cidade");

            } else if (Validator.stringEmpty(estadoRecebido)) {
                estado.setError("Insira seu estado");

            } else if (Validator.stringEmpty(cnpjRecebido)) {
                cnpj.setError("Insira o CNPJ da empresa");

            } else if (Validator.isCNPJ(cnpjSemFormatacao) ==  false) {
                cnpj.setError("Insira um CNPJ da válido");

            } else {

                Startup startup = new Startup(razaoSocialRecebido, nomeFantasiaRecebido, emailRecebido, cepRecebido, ruaRecebida, bairroRecebido,cidadeRecebido, estadoRecebido, cnpjRecebido);
                startup.salvarStartup(firebaseUser.getUid());
                progressDialog.setMessage("Salvando dados...");
                progressDialog.show();
                Toast.makeText(CadastroStartupActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CadastroStartupActivity.this, BaseFragmentStartup.class));
                finish();
            }

        }else {
            Log.i("sem internet", "sem conexao");
            Toast.makeText(CadastroStartupActivity.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }
        }
    });

    }

    @Override
    protected void onResume() {
        irPerfil.setTextColor(Color.parseColor("#ffffff"));
        super.onResume();
    }

}
