package com.treinamento.mdomingos.startapp.activity.investidor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentActivity;
import com.treinamento.mdomingos.startapp.activity.startup.CadastroStartupActivity;
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
    private TextView irPerfil;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

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
        irPerfil = findViewById(R.id.ir_para_perfil_cadastro_investidor);
        radioGroup = findViewById(R.id.radioGroup);
        progressDialog = new ProgressDialog(CadastroInvestidorActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //Mascaras
        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NN/NN/NNNN"); //Mascara para data
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(dataNasc, simpleMaskFormatter);
        dataNasc.addTextChangedListener(maskTextWatcher);

        SimpleMaskFormatter simpleMaskFormatterCpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN"); //Mascara para cpf
        MaskTextWatcher maskTextWatcherCpf = new MaskTextWatcher(cpf, simpleMaskFormatterCpf);
        cpf.addTextChangedListener(maskTextWatcherCpf);

        SimpleMaskFormatter simpleMaskFormatterCnpj = new SimpleMaskFormatter("NN.NNN.NNN/NNNN-NN"); //Mascara para cnpj
        MaskTextWatcher maskTextWatcherCnpj = new MaskTextWatcher(cnpj, simpleMaskFormatterCnpj);
        cnpj.addTextChangedListener(maskTextWatcherCnpj);

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

//        databaseReference.child("Usuarios").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                InvestidorResponse investidor = dataSnapshot.getValue(InvestidorResponse.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        irPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irPerfil.setTextColor(Color.parseColor("#0289BE"));
                startActivity(new Intent(CadastroInvestidorActivity.this, BaseFragmentActivity.class));
                finish();
            }
        });

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

                    String cpfSemFormatacao = cpfRecebido.replace(".", "");
                    cpfSemFormatacao = cpfSemFormatacao.replace("-", "");

                    String cnpjSemFormatacao = cnpjRecebido.replace(".", "");
                    cnpjSemFormatacao = cnpjSemFormatacao.replace("/", "");
                    cnpjSemFormatacao = cnpjSemFormatacao.replace("-", "");

                    if (Validator.stringEmpty(nomeRecebido)) {
                        nome.setError("Insira seu nome");

                    } else if (Validator.stringEmpty(emailRecebido)) {
                        email.setError("Insira seu email");

                    } else if (Validator.validateEmailFormat(emailRecebido) ==  false) {
                        email.setError("Insira um email válido");

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
                                Investidor investidor = new Investidor(nomeRecebido, emailRecebido, dataRecebida, cepRecebido, ruaRecebida, bairroRecebido, cidadeRecebido, estadoRecebido, null, cpfRecebido);
                                investidor.salvarInvestidor(firebaseUser.getUid());
                                progressDialog.setMessage("Salvando dados...");
                                progressDialog.show();
                                Toast.makeText(CadastroInvestidorActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CadastroInvestidorActivity.this, BaseFragmentActivity.class));
                                finish();

                            }
                        } else if (radioGroup.getCheckedRadioButtonId() == R.id.pessoaJuridica) {
                            if (Validator.isCNPJ(cnpjSemFormatacao) == false) {
                                cnpj.setError("Insira um CNPJ válido");
                            } else {
                                Investidor investidor = new Investidor(nomeRecebido, emailRecebido, dataRecebida, cepRecebido, ruaRecebida, bairroRecebido, cidadeRecebido, estadoRecebido, cnpjRecebido, null);
                                investidor.salvarInvestidor(firebaseUser.getUid());
                                progressDialog.setMessage("Salvando dados...");
                                progressDialog.show();
                                startActivity(new Intent(CadastroInvestidorActivity.this, BaseFragmentActivity.class));
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

    public boolean FirebaseConection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) // conectado a internet
            return true;
        return false; // nao conectado
    }
}
