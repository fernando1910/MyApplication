package project.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.EventoAdapter;
import classes.Evento;
import interfaces.RecyclerViewOnClickListenerHack;


public class PainelMeusEventos extends ActionBarActivity implements RecyclerViewOnClickListenerHack {

    private Evento objEvento;
    private ListView lvEventos;
    private ProgressDialog progressDialog;
    private String jsonString;
    private RecyclerView rvEvento;
    private List<Evento> eventos;
    private EventoAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_padrao_meus_eventos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvEventos = (ListView)findViewById(R.id.lvEventos);
        rvEvento = (RecyclerView)findViewById(R.id.rvEvento);
        rvEvento.setHasFixedSize(true);
        rvEvento.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvEvento.getLayoutManager();
                EventoAdapter adapter = (EventoAdapter) rvEvento.getAdapter();

                if(eventos.size()== linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) {

                }
            }
        });



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvEvento.setLayoutManager(linearLayoutManager);


        new Carregar().execute();

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


        if (id == android.R.id.home)
        {
            this.finish();
            startActivity(new Intent(this,MenuPrincipal.class));
            return true;
        }


        if (item.getItemId() == R.id.action_example) {
            startActivity(new Intent(this,CadEvento.class));
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
        startActivity(new Intent(this, MenuPrincipal.class));

    }

    @Override
    public void onClickListener(View view, int position) {
        int codigoEvento = adapter.getCodigoEvento(position);
        Toast.makeText(PainelMeusEventos.this, String.valueOf(codigoEvento), Toast.LENGTH_SHORT).show();
    }

    public class Carregar extends AsyncTask<Void,Integer,Void>
    {
        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(PainelMeusEventos.this);
            progressDialog = ProgressDialog.show(PainelMeusEventos.this,"Carregando...",
                    "Carregando seus eventos, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                synchronized (this)
                {
                    objEvento = new Evento();
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

            eventos = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                JSONObject jsonObject;

                for (int i = 0 ; i < jsonArray.length(); i++)
                {
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

            adapter = new EventoAdapter(PainelMeusEventos.this,eventos);
            adapter.setRecyclerViewOnClickListenerHack(PainelMeusEventos.this);
            rvEvento.setAdapter(adapter);


        }

    }
}
