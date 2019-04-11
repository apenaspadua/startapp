package com.treinamento.mdomingos.startapp.activity.investidor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.model.CEP;
import com.treinamento.mdomingos.startapp.model.Investidor;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;
import com.treinamento.mdomingos.startapp.utils.HttpService;
import com.treinamento.mdomingos.startapp.utils.MaskFormatter;
import com.treinamento.mdomingos.startapp.utils.Validator;

import java.util.concurrent.ExecutionException;

public class CadastroInvestidorActivity extends AppCompatActivity {

    private EditText nome, email, empresa, telefone, cep, cpf, cnpj, dataNasc, rua, cidade, bairro, estado;
    private RadioGroup radioGroup;
    private TextInputLayout inputPf, inputPj;
    private RelativeLayout botaoAvancar;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_investidor);

        nome = findViewById(R.id.nome_cadastro_investidor_id);
        email = findViewById(R.id.email_cadastro_investidor_id);
        telefone = findViewById(R.id.telefone_cadastro_investidor_id);
        empresa = findViewById(R.id.empresa_cadastro_investidor_id);
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
        botaoAvancar = findViewById(R.id.botao_avancar_cadastro_investidor_id);
        radioGroup = findViewById(R.id.radioGroup);
        progressDialog = new ProgressDialog(CadastroInvestidorActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //Mascaras
        MaskFormatter.simpleFormatterCell(telefone);
        MaskFormatter.simpleFormatterData(dataNasc);
        MaskFormatter.simpleFormatterCpf(cpf);
        MaskFormatter.simpleFormatterCnpj(cnpj);

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
            }

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
            public void afterTextChanged(Editable s) {
            }
        });

        botaoAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseConfig.firebaseConection()) {

                    progressDialog.setMessage("Guardando dados...");
                    progressDialog.show();

                    final String nomeRecebido = nome.getText().toString();
                    final String emailRecebido = email.getText().toString();
                    final String telefoneRecebido = telefone.getText().toString();
                    final String empresaRecebida = empresa.getText().toString();
                    final String dataRecebida = dataNasc.getText().toString();
                    final String cepRecebido = cep.getText().toString();
                    final String ruaRecebida = rua.getText().toString();
                    final String bairroRecebido = bairro.getText().toString();
                    final String cidadeRecebido = cidade.getText().toString();
                    final String estadoRecebido = estado.getText().toString();
                    final String cpfRecebido = cpf.getText().toString();
                    final String cnpjRecebido = cnpj.getText().toString();

                    String cpfSemFormatacao = cpfRecebido.replace(".", "");
                    cpfSemFormatacao = cpfSemFormatacao.replace("-", "");

                    String cnpjSemFormatacao = cnpjRecebido.replace(".", "");
                    cnpjSemFormatacao = cnpjSemFormatacao.replace("/", "");
                    cnpjSemFormatacao = cnpjSemFormatacao.replace("-", "");

                    if (Validator.stringEmpty(nomeRecebido)) {
                        nome.setError("Insira seu nome");

                    } else if (Validator.stringEmpty(emailRecebido)) {
                        email.setError("Insira seu email");

                    } else if (Validator.validateEmailFormat(emailRecebido) == false) {
                        email.setError("Insira um email válido");

                    } else if (Validator.stringEmpty(telefoneRecebido)) {
                        telefone.setError("Insira seu numero");

                    } else if (Validator.stringEmpty(empresaRecebida)) {
                        empresa.setError("Insira sua compania");

                    } else if (Validator.stringEmpty(dataRecebida)) {
                        dataNasc.setError("Insira sua data de nascimento");

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

                    } else {

                        if (radioGroup.getCheckedRadioButtonId() == R.id.pessoaFisica) {
                            if (Validator.isCPF(cpfSemFormatacao) == false) {
                                cpf.setError("Insira um CPF válido");
                                return;

                            } else {

                                Investidor investidor = new Investidor(nomeRecebido, emailRecebido, telefoneRecebido, empresaRecebida, dataRecebida, cepRecebido, ruaRecebida, bairroRecebido, cidadeRecebido, estadoRecebido, null, cpfRecebido);
                                investidor.salvarInvestidor(firebaseUser.getUid());
                                Intent intent = new Intent(CadastroInvestidorActivity.this, BioInvestidorActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        } else if (radioGroup.getCheckedRadioButtonId() == R.id.pessoaJuridica) {
                            if (Validator.isCNPJ(cnpjSemFormatacao) == false) {
                                cnpj.setError("Insira um CNPJ válido");
                            } else {
                                Investidor investidor = new Investidor(nomeRecebido, emailRecebido, telefoneRecebido, empresaRecebida, dataRecebida, cepRecebido, ruaRecebida, bairroRecebido, cidadeRecebido, estadoRecebido, cnpjRecebido, null);
                                investidor.salvarInvestidor(firebaseUser.getUid());
                                progressDialog.setMessage("Guardando dados...");
                                progressDialog.show();
                                Intent intent = new Intent(CadastroInvestidorActivity.this, BioInvestidorActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                } else {
                    Log.i("sem internet", "sem conexao");
                    Toast.makeText(CadastroInvestidorActivity.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    @Override
    public void finish() {
        System.runFinalizersOnExit(true) ;
        super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }




}
