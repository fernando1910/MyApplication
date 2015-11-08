package project.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.CustomListViewContato;
import domain.Contatos;
import domain.Evento;
import domain.Util;

public class VisualizarConvidados extends AppCompatActivity {
    private final String TAG = "LOG";
    private ListView lvConvidados;
    private int codigoEvento;
    private Contatos objContatos;
    private CustomListViewContato mAdapter;
    private List<Contatos> mContatos;
    private Util util;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.action_convidar);
            setContentView(R.layout.activity_visualizar_convidados);
            lvConvidados = (ListView) findViewById(R.id.lvConvidados);
            mProgressBar = (ProgressBar) findViewById(R.id.pbFooterLoading);
            objContatos = new Contatos();
            util = new Util();
            mContatos = new ArrayList<>();

            Bundle parameters = getIntent().getExtras();
            if (parameters != null) {
                codigoEvento = parameters.getInt("codigoEvento");
                new buscarContatos().execute();
            }
            else{
                this.finish();
                Toast.makeText(VisualizarConvidados.this, "Erro interno cod. 1", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
            this.finish();
            Toast.makeText(VisualizarConvidados.this, "Erro interno cod. 0", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class buscarContatos extends AsyncTask<Void, Integer, Void> {
        boolean fg_conexao_internet;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this) {
                try {
                    fg_conexao_internet = util.verificaInternet(VisualizarConvidados.this.getApplicationContext());
                    if (fg_conexao_internet) {
                        mContatos = objContatos.buscarConvidados(VisualizarConvidados.this.getApplicationContext(), codigoEvento, util);
                        mAdapter = new CustomListViewContato(VisualizarConvidados.this, mContatos, false);
                    }
                } catch (Exception ex) {
                    Log.i(TAG, ex.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            if (fg_conexao_internet) {
                lvConvidados.setAdapter(mAdapter);
            }
        }
    }
}
