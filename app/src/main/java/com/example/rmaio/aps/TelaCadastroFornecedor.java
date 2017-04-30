package com.example.rmaio.aps;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by usuario1 on 25/04/2017.
 */

public class TelaCadastroFornecedor extends Activity {
    EditText telefone;
    EditText razao;
    EditText endereco;
    EditText cnpj;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_fornecedor);
        cnpj = (EditText) findViewById(R.id.txtCNPJ);
        razao = (EditText) findViewById(R.id.txtRazao);
        endereco = (EditText) findViewById(R.id.txtEndereco);
        telefone = (EditText) findViewById(R.id.txtTelefone);
        Banco banco = new Banco(this);
        db = banco.getWritableDatabase();
    }
    private String inserir(){
        ContentValues valores = new ContentValues();
        valores.put(Banco.TFornecedor.CNPJ,cnpj.getText().toString());
        valores.put(Banco.TFornecedor.RAZAO,razao.getText().toString());
        valores.put(Banco.TFornecedor.ENDERECO,endereco.getText().toString());
        valores.put(Banco.TFornecedor.TELEFONE,telefone.getText().toString());
        if (db.insert(Banco.TFornecedor.TABELA,null,valores)!=-1)
            return "Fornecedor cadastrado!";
        else
            return "Erro ao cadastrar";

    }
    public void sair(View v){
        finish();
    }

    public void cadastrarFornecedor(View v){
        if(validarDados()){
            Toast.makeText(this,inserir(), Toast.LENGTH_LONG).show();
            telefone.setText("");
            razao.setText("");
            endereco.setText("");
            cnpj.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } else {
            Toast.makeText(this,"Falha ao validar dados!", Toast.LENGTH_LONG).show();
        }
    }
    private boolean validarDados(){
        if(razao.getText().length()<4)
            return false;
        if(cnpj.getText().length()!=14 || !isNumeric(cnpj.getText().toString()))
            return false;
        if(endereco.getText().length()<10)
            return false;
        if(telefone.getText().length()<9)
            return false;
        return true;
    }
    private boolean isNumeric(String str){
        for (char c : str.toCharArray()){
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}
