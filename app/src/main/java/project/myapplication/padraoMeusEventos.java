package project.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

    private clsEvento objEvento;
    private ListView lvEventos;
    private ProgressDialog progressDialog;
    String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Carregar().execute();

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
        startActivity(new Intent(this, padraoMenu.class));

    }

    public class Carregar extends AsyncTask<Void,Integer,Void>
    {
        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(padraoMeusEventos.this);
            progressDialog = ProgressDialog.show(padraoMeusEventos.this,"Carregando...",
                    "Carregando seus eventos, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                synchronized (this)
                {
                    objEvento = new clsEvento();
                    jsonString =  objEvento.carregarEventos(getString(R.string.padrao_evento),null);
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result)
        {
            progressDialog.dismiss();

            setContentView(R.layout.activity_padrao_meus_eventos);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            lvEventos = (ListView)findViewById(R.id.lvEventos);
            List<menuEvento> eventos = new ArrayList<>();

            try {
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

            } catch (JSONException e) {
                e.printStackTrace();
            }

            final CustomListViewEvento arrayAdapter = new CustomListViewEvento(padraoMeusEventos.this,eventos);
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
    }
}
