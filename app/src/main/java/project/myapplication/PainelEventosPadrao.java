package project.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import adapters.CustomListViewEvento;
import domain.Evento;
import domain.Usuario;
import domain.Util;


public class PainelEventosPadrao extends AppCompatActivity {
    private int codigoUsuario;
    private Util util;
    private final String TAG = "ERRO";
    private ProgressDialog progressDialog;
    private List<Evento> mListaEventos;
    private String mTipoAcao;
    private TextView tvMensagem;
    private Evento objEvento;
    private Date dt_evento;
    private ListView lvEventos;
    private CustomListViewEvento mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_meus_eventos);
        try {
            util = new Util();
            objEvento = new Evento();
            Usuario objUsuario = new Usuario();
            objUsuario.carregar(this);
            codigoUsuario = objUsuario.getCodigoUsuario();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            tvMensagem = (TextView) findViewById(R.id.tvMensagem);
            lvEventos = (ListView) findViewById(R.id.lvEventos);

            Bundle parameters = getIntent().getExtras();
            String fg_comentario, fg_convites;

            if (parameters != null) {
                fg_comentario = parameters.getString("fg_comentario");
                if (fg_comentario != null) {
                    mTipoAcao = "buscarNovosComentario";
                }

                fg_convites = parameters.getString("fg_comentario");
                if (fg_convites != null)
                    mTipoAcao = "buscarConvites";

                dt_evento = util.formataData(parameters.getString("dt_evento"));
                if (dt_evento != null)
                    mTipoAcao = "buscarEventoData";
            }


            new carregar().execute();
        } catch (Exception ex) {
            util.gravarLogErro(this, codigoUsuario, "PainelEventosPadrao", ex.getMessage());
            finish();
            startActivity(new Intent(this, MenuPrincipalNovo.class));
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
            startActivity(new Intent(PainelEventosPadrao.this, MenuPrincipalNovo.class));
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
        startActivity(new Intent(PainelEventosPadrao.this, MenuPrincipalNovo.class));
    }


    public class carregar extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PainelEventosPadrao.this);
            progressDialog = ProgressDialog.show(PainelEventosPadrao.this, "Carregando...",
                    "Carregando seus eventos, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this) {
                try {
                    switch (mTipoAcao) {
                        case "buscarEventoData":
                            mListaEventos = objEvento.selecionarEventosPorData(PainelEventosPadrao.this.getApplicationContext(), dt_evento);
                            break;

                        default:
                            PainelEventosPadrao.this.finish();
                            startActivity(new Intent(PainelEventosPadrao.this, MenuPrincipalNovo.class));
                            break;
                    }

                    if (mListaEventos != null){
                        mAdapter = new CustomListViewEvento(PainelEventosPadrao.this, mListaEventos);
                    }

                } catch (Exception ex) {
                    Log.i(TAG, ex.getMessage());
                }
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
            lvEventos.setAdapter(mAdapter);
            lvEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent it =  new Intent(PainelEventosPadrao.this,VisualizarEvento.class);
                    it.putExtra("codigoEvento",mAdapter.getCodigoEvento(position));
                    startActivity(it);
                }
            });
        }
    }
}
