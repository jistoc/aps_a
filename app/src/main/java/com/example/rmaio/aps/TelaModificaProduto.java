package com.example.rmaio.aps;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usuario1 on 27/04/2017.
 */

public class TelaModificaProduto extends Activity {

    EditText nome;
    EditText preco;
    EditText descricao;
    Produto produto;
    Spinner listaFornecedores;
    int id;
    private SQLiteDatabase db;
    String sFornecedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_modifica_produto);
        Banco banco = new Banco(this);
        db = banco.getWritableDatabase();
        nome = (EditText) findViewById(R.id.txtProduto);
        preco = (EditText) findViewById(R.id.txtPreco);
        descricao = (EditText) findViewById(R.id.txtDesc);
        listaFornecedores = (Spinner) findViewById(R.id.spFornecedor);
        pesquisarProduto();
        listarFornecedores();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        produto = new Produto();
        produto.setNome(nome.getText().toString());
        produto.setDescricao(descricao.getText().toString());
        produto.setPreco(preco.getText().toString());
        outState.putParcelable("produto",produto);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        produto = savedInstanceState.getParcelable("produto");
        nome.setText(produto.getNome());
        preco.setText(produto.getPreco());
        descricao.setText(produto.getDescricao());

    }

    public void voltar(View v){
        finish();
    }

    private void pesquisarProduto(){

        String nProduto = getIntent().getStringExtra("produto");
        String selection = Banco.TProduto.NOME +" = ? ";
        String[] selectionArgs = {nProduto};
        String orderBy = null;
        String having = null;
        String groupBy = null;
        Cursor c = db.query(Banco.TProduto.TABELA, null, selection, selectionArgs, groupBy, having, orderBy);
        if (c.getCount()>0) {
            if  (c.moveToFirst()) {
                id = c.getInt(c.getColumnIndex(Banco.TProduto._ID));
                nome.setText(c.getString(c.getColumnIndex(Banco.TProduto.NOME)));
                preco.setText(c.getString(c.getColumnIndex(Banco.TProduto.PRECO)));
                descricao.setText(c.getString(c.getColumnIndex(Banco.TProduto.DESCRICAO)));
                sFornecedor = c.getString(c.getColumnIndex(Banco.TProduto.FORNECEDOR));
            }
        }

    }
    public void excluir(View v){
        String whereClause = "_id = ?";
        String[] whereArgs = {String.valueOf(id)};
        if(db.delete(Banco.TProduto.TABELA,whereClause,whereArgs)!=-1){
            Toast.makeText(this,"Produto excluido!", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this,"Falha ao excluir!", Toast.LENGTH_LONG).show();
        }
    }
    public void alterarProduto(View v){

        String whereClause = "_id = ?";
        String[] whereArgs = {String.valueOf(id)};

        ContentValues values = new ContentValues();
        values.put(Banco.TProduto.NOME,nome.getText().toString());
        values.put(Banco.TProduto.FORNECEDOR,sFornecedor);
        values.put(Banco.TProduto.PRECO,preco.getText().toString());
        values.put(Banco.TProduto.DESCRICAO,descricao.getText().toString());
        values.put(Banco.TProduto.FORNECEDOR,listaFornecedores.getSelectedItem().toString());

        if(db.update(Banco.TProduto.TABELA,values,whereClause,whereArgs)!=0){
            Toast.makeText(this,"Produto alterado!", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this,"Falha ao alterar!", Toast.LENGTH_LONG).show();
        }
    }
    private void listarFornecedores(){

        List<String> fornecedores=new ArrayList<String>();
        String[] columns = {Banco.TFornecedor.RAZAO};
        Cursor c = db.query(Banco.TFornecedor.TABELA, columns, null, null, null, null, null);
        int x = c.getCount();
        if(x>0){
            try{
                while(c.moveToNext()){
                    fornecedores.add(c.getString(c.getColumnIndex(Banco.TFornecedor.RAZAO)));
                }
            }finally {
                c.close();
            }
        } else {

        }
        if(!fornecedores.contains(sFornecedor))
            fornecedores.add(sFornecedor);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fornecedores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listaFornecedores.setAdapter(adapter);
        listaFornecedores.setSelection(getIndex(listaFornecedores,sFornecedor));

    }
    private int getIndex(Spinner spinner, String string) {
        int index = -1;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)){
                index = i;
                break;
            }
        }
        return index;
    }
}
