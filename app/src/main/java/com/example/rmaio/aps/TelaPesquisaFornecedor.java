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
 * Created by usuario1 on 27/04/2017.
 */

public class TelaPesquisaFornecedor extends Activity{
    private SQLiteDatabase db;
    EditText fornecedor;
    TextView total;
    int REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_pesquisa_fornecedor);
        Banco banco = new Banco(this);
        db = banco.getReadableDatabase();
        total = (TextView) findViewById(R.id.txTotal);
        fornecedor = (EditText) findViewById(R.id.txtFornecedor);
        fornecedor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pesquisarFornecedor(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pesquisarFornecedor(null);
    }
    private void pesquisarFornecedor(View v){

        ArrayList<String> resultado = new ArrayList<String>();
        String selection = null;
        String[] selectionArgs = null;
        if(fornecedor.getText().toString().length()>0){
            selection = Banco.TFornecedor.RAZAO +" like ? ";
            selectionArgs = new String[]{"%"+fornecedor.getText().toString()+"%"};
        }
        String[] columns = {Banco.TFornecedor.RAZAO};
        String orderBy = null;
        String having = null;
        String groupBy = null;
        Cursor c = db.query(Banco.TFornecedor.TABELA, columns, selection, selectionArgs, groupBy, having, orderBy);
        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    resultado.add(c.getString(c.getColumnIndex(Banco.TFornecedor.RAZAO)));
                }while (c.moveToNext());
            }
        }
        total.setText("Fornecedores encontrados: "+c.getCount());
        ListView lista = (ListView) findViewById(R.id.listaFornecedores);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, resultado);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, final int position, long id) {

                String p = adapter.getItemAtPosition(position).toString();
                Intent it = new Intent(getBaseContext(),TelaModificaFornecedor.class);
                it.putExtra("fornecedor",p);
                startActivityForResult(it,REQUEST);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode == REQUEST){
            pesquisarFornecedor(null);
        }
    }
}
