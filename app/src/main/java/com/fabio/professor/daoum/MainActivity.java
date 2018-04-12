package com.fabio.professor.daoum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fabio.professor.daoum.dao.DaoAdapter;
import com.fabio.professor.daoum.dao.DaoPessoa;
import com.fabio.professor.daoum.domain.Pessoa;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int ACAO_ADICIONAR = 1111;
    private ListView lista;
    private DaoPessoa daoPessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Criamos o banco de dados
        DaoAdapter daoAdapter = new DaoAdapter(this);
        daoAdapter.onCreate(daoAdapter.getWritableDatabase());
        //Criamos o banco de dados

        lista = findViewById(R.id.lista);

        daoPessoa = new DaoPessoa(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<String> values = new ArrayList<String>();
        final ArrayList<Pessoa> pessoas = daoPessoa.getTodos(); //pegamos todas pessoas do banco

        if(pessoas.size() > 0) setTitle("Todas Pessoas");
        else setTitle("Não Há Pessoas Cadastradas");

        //inserimos na lista de nomes que será exibida no listview
        for(Pessoa p : pessoas){
            values.add(p.getNome());
        }
        //inserimos na lista de nomes que será exibida no listview

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pessoa selecionada = pessoas.get(position);
                Intent i = new Intent(MainActivity.this, NovoActivity.class);
                i.putExtra("id", selecionada.getId());
                startActivity(i);
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Pessoa selecionada = pessoas.get(position);
                boolean test = daoPessoa.delete(selecionada.getId());
                if(test){
                    Toast.makeText(MainActivity.this, "Sucesso!", Toast.LENGTH_SHORT).show();
                    onResume();
                } else{
                    Toast.makeText(MainActivity.this, "Erro!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, ACAO_ADICIONAR, 0, "Novo");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case ACAO_ADICIONAR:
                Intent i = new Intent(this, NovoActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
