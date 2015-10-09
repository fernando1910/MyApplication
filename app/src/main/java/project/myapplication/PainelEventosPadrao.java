package project.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import domain.Evento;


public class PainelEventosPadrao extends AppCompatActivity  {

    private ProgressDialog progressDialog;

    private List<Evento> eventos;

    private String mDataCalendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_meus_eventos);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mDataCalendario = getIntent().getStringExtra("mDataCalendario");


            //new Carregar().execute();
        } catch (Exception ex) {
            Toast.makeText(PainelEventosPadrao.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_padrao_meus_eventos, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == android.R.id.home) {
            this.finish();
            return true;
        }


        if (item.getItemId() == R.id.action_example) {
            startActivity(new Intent(this, CadEvento.class));
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
    }


    public class Carregar extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PainelEventosPadrao.this);
            progressDialog = ProgressDialog.show(PainelEventosPadrao.this, "Carregando...",
                    "Carregando seus eventos, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                synchronized (this) {
                    Evento objEvento = new Evento();
                    String jsonString = null;

                    eventos = new ArrayList<>();

                    try {
                        JSONArray jsonArray = new JSONArray(jsonString);
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = new JSONObject(jsonArray.getString(i));
                            Evento evento = new Evento();
                            evento.setTituloEvento((jsonObject.getString("ds_titulo_evento")));
                            evento.setCodigoEvento((jsonObject.getInt("cd_evento")));
                            evento.setUrlFoto(getString(R.string.caminho_foto_capa_evento) + evento.getCodigoEvento() + ".png");
                            eventos.add(evento);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();

        }

    }
}
