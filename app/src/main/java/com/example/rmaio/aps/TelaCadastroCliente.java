package com.example.rmaio.aps;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by usuario1 on 25/04/2017.
 */

public class TelaCadastroCliente extends Activity {

    EditText cpf;
    EditText nome;
    EditText endereco;
    EditText email;
    EditText senha;
    EditText confirmarSenha;
    Cliente cliente = new Cliente();
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_cliente);
        cpf = (EditText) findViewById(R.id.txtCPF);
        nome = (EditText) findViewById(R.id.txtNome);
        endereco = (EditText) findViewById(R.id.txtEndereco);
        email = (EditText) findViewById(R.id.txtEmail);
        senha = (EditText) findViewById(R.id.txtSenha);
        confirmarSenha = (EditText) findViewById(R.id.txtConfirmarSenha);
        Banco banco = new Banco(this);
        db = banco.getWritableDatabase();
    }
    public void sair(View v){
        finish();
    }
    private long inserir(){
        ContentValues valores = new ContentValues();
        valores.put(Banco.TCliente.CPF,cpf.getText().toString());
        valores.put(Banco.TCliente.NOME,nome.getText().toString());
        valores.put(Banco.TCliente.ENDERECO,endereco.getText().toString());
        valores.put(Banco.TCliente.SENHA,senha.getText().toString());
        valores.put(Banco.TCliente.EMAIL,email.getText().toString());
        return db.insert(Banco.TCliente.TABELA,null,valores);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(cpf.getText().toString()!=null)
            cliente.setCpf(cpf.getText().toString());
        if(nome.getText().toString()!=null)
            cliente.setNome(nome.getText().toString());
        if(endereco.getText().toString()!=null)
            cliente.setEndereco(nome.getText().toString());
        if(email.getText().toString()!=null)
            cliente.setEmail(email.getText().toString());
        outState.putParcelable("cliente",cliente);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        cliente = savedInstanceState.getParcelable("cliente");
        cpf.setText(cliente.getCpf());
        nome.setText(cliente.getNome());
        endereco.setText(cliente.getEndereco());
        email.setText(cliente.getEmail());
    }
    public void criarConta(View v){
        if(validarDados()){
            if(inserir()!=-1) {
                Toast.makeText(this,"Cadastro efetuado!", Toast.LENGTH_LONG).show();
                endereco.setText("");
                email.setText("");
                senha.setText("");
                confirmarSenha.setText("");
                cpf.setText("");
                nome.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                Toast.makeText(this,"Falha ao cadastrar!", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this,"Falha ao validar dados!", Toast.LENGTH_LONG).show();
        }
    }
    private boolean validarDados(){
        if(nome.getText().length()<4)
            return false;
        if(!validarEmail(email.getText().toString()))
            return false;
        if(cpf.getText().length()!=11 || !isNumeric(cpf.getText().toString()))
            return false;
        if(endereco.getText().length()<10)
            return false;
        if(!senha.getText().toString().equals(confirmarSenha.getText().toString()))
            return false;
        return true;
    }
    private boolean isNumeric(String str){
        for (char c : str.toCharArray()){
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
    public boolean validarEmail(final String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
