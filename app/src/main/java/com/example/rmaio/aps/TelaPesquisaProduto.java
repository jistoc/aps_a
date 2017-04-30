package com.example.rmaio.aps;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by usuario1 on 26/04/2017.
 */

public class TelaPesquisaProduto extends Activity {

    private SQLiteDatabase db;
    EditText produto;
    TextView total;
    int REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_pesquisa_produto);
        Banco banco = new Banco(this);
        db = banco.getReadableDatabase();
        total = (TextView) findViewById(R.id.txTotal);
        produto = (EditText) findViewById(R.id.txtProduto);
        produto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pesquisarProduto(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pesquisarProduto(null);
    }
    private void pesquisarProduto(View v){

        ArrayList<String> resultado = new ArrayList<String>();
        String selection = null;
        String[] selectionArgs = null;
        if(produto.getText().toString().length()>0){
            selection = Banco.TProduto.NOME +" like ? ";
            selectionArgs = new String[]{"%"+produto.getText().toString()+"%"};
        }
        String[] columns = {Banco.TProduto.NOME,Banco.TProduto.DESCRICAO};
        String orderBy = null;
        String having = null;
        String groupBy = null;
        Cursor c = db.query(Banco.TProduto.TABELA, columns, selection, selectionArgs, groupBy, having, orderBy);
        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    resultado.add(c.getString(c.getColumnIndex(Banco.TProduto.NOME)));
                }while (c.moveToNext());
            }
        }
        total.setText("Produtos encontrados: "+c.getCount());
        ListView lista = (ListView) findViewById(R.id.listaProdutos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, resultado);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, final int position, long id) {

                String p = adapter.getItemAtPosition(position).toString();
                Intent it = new Intent(getBaseContext(),TelaModificaProduto.class);
                it.putExtra("produto",p);
                startActivityForResult(it,REQUEST);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode == REQUEST){
            pesquisarProduto(null);
        }
    }

}
