package project.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class padraoMeusEventos extends ActionBarActivity {

    clsEvento objEvento;
    ListView lvEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_meus_eventos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lvEventos = (ListView)findViewById(R.id.lvEventos);

        objEvento = new clsEvento();

        String jsonString;
        List<menuEvento> eventos = new ArrayList<>();

        try {
            jsonString =  objEvento.carregarEventos(getString(R.string.padrao_evento),null);
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject;

            for (int i = 0 ; i < jsonArray.length(); i++)
            {
                jsonObject = new JSONObject(jsonArray.getString(i));
                menuEvento evento = new menuEvento();
                evento.setTitulo((jsonObject.getString("ds_titulo_evento")));
                evento.setCodigoEvento((jsonObject.getInt("cd_evento")));

                eventos.add(evento);
            }


        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }

        /*
        final ArrayAdapter<menuEvento> arrayAdapter = new ArrayAdapter<>(
                this,android.R.layout.simple_expandable_list_item_2,eventos
        );
        */
        final CustomListViewEvento arrayAdapter = new CustomListViewEvento(this,eventos);
        lvEventos.setAdapter(arrayAdapter);

        lvEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int codigoEvento = arrayAdapter.getValue(position);
                Intent intent = new Intent(getApplicationContext(),padraoVisulizarEvento.class);
                Bundle parameters = new Bundle();
                parameters.putInt("codigoEvento", codigoEvento);
                intent.putExtras(parameters);
                try {
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_padrao_meus_eventos, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == android.R.id.home)
        {
            this.finish();
            startActivity(new Intent(this,padraoMenu.class));
            return true;
        }


        if (item.getItemId() == R.id.action_example) {
            startActivity(new Intent(this,padraoCriarEvento.class));
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(this,padraoMenu.class));

    }
}
