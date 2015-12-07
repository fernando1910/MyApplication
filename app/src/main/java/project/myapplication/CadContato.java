package project.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import adapters.CustomListViewContato;
import domain.Contatos;
import domain.Usuario;
import domain.Util;


public class CadContato extends AppCompatActivity {
    private final String TAG = "LOG";
    private ListView lvContatos;
    private Contatos objContatos;
    private Button btConfirmar;
    private int codigoEvento = 0;
    private boolean cbContatoVisivel = false, fg_convidou = false;
    private List<Contatos> contatosList;
    private CustomListViewContato arrayAdapter;
    private Util util;
    MenuItem mActionProgressItem;
    private ProgressDialog mProgressDialog;
    private int mQuantidadeContatos;
    private Usuario objUsuario;
    private CustomListViewContato mAdapter;
    private List<Contatos> mContatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_padrao_contatos);
            ActionBar mActionBar = getSupportActionBar();
            if (mActionBar != null)
                mActionBar .setDisplayHomeAsUpEnabled(true);


            //region Vinculação com XML
            lvContatos = (ListView) findViewById(R.id.lvContatos);
            btConfirmar = (Button) findViewById(R.id.btConfirmar);
            //endregion

            objContatos = new Contatos();
            objUsuario = new Usuario();
            objUsuario.carregar(this);
            util = new Util();

            Bundle parameters = getIntent().getExtras();
            if (parameters != null) {
                codigoEvento = parameters.getInt("codigoEvento");
                btConfirmar.setVisibility(View.VISIBLE);
                cbContatoVisivel = true;
            }
            contatosList = objContatos.retonarContatos(this);
            arrayAdapter = new CustomListViewContato(this, contatosList, cbContatoVisivel);
            lvContatos.setAdapter(arrayAdapter);
            mQuantidadeContatos = lvContatos.getCount();

        } catch (Exception ex) {
            this.finish();
            Log.i(TAG, ex.getMessage());
        }

    }

    //region Métodos do Android

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        if (id == R.id.action_settings) {
            new sincrozinarContatos().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_padrao_contatos, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mActionProgressItem = menu.findItem(R.id.miActionProgress);
        ProgressBar v = (ProgressBar) MenuItemCompat.getActionView(mActionProgressItem);
        return super.onPrepareOptionsMenu(menu);
    }

    //endregion

    //region Métodos de implementação

    public class sincrozinarContatos extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            mActionProgressItem.setVisible(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                objContatos.atualizarContatos(getContentResolver(), getString(R.string.wsBlueDate), CadContato.this);
            } catch (Exception ex) {
                Toast.makeText(CadContato.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            mActionProgressItem.setVisible(false);
            List<Contatos> contatosList = objContatos.retonarContatos(CadContato.this);
            arrayAdapter = new CustomListViewContato(CadContato.this, contatosList, cbContatoVisivel);
            lvContatos.setAdapter(arrayAdapter);
        }
    }

    public void onCLickConvidarContatos(View view) {
        new convidar().execute();
    }


    public class convidar extends AsyncTask<Void, Integer, Void> {
        String mResposta;
        int mQuantidadeConvidados = Integer.MIN_VALUE;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(CadContato.this);
            mProgressDialog = ProgressDialog.show(CadContato.this, "Carregando...",
                    "Convidado os seus contatos, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this) {
                try {
                    boolean checked;
                    boolean selecionou = false;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cd_evento", String.valueOf(codigoEvento));
                    jsonObject.put("ds_nome", objUsuario.getNome());
                    jsonObject.put("cd_usuario_inclusao", objUsuario.getCodigoUsuario());
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < mQuantidadeContatos; i++) {
                        checked = arrayAdapter.isChecked(i);
                        if (checked) {
                            selecionou = true;
                            JSONObject aux = new JSONObject();
                            aux.put("cd_usuario", String.valueOf(arrayAdapter.getValue(i)));
                            jsonArray.put(aux);
                        }
                    }
                    if (selecionou) {
                        jsonObject.put("convidados", jsonArray);
                        mResposta = util.enviarServidor(getString(R.string.wsBlueDate), jsonObject.toString(), "convidarUsuario");
                        mQuantidadeConvidados = Integer.parseInt(mResposta);
                        fg_convidou = true;
                    }

                } catch (Exception ex) {
                    Log.i(TAG, ex.getMessage());
                    mProgressDialog.dismiss();
                    fg_convidou = false;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                mProgressDialog.dismiss();
                if (fg_convidou)
                    Toast.makeText(CadContato.this, "Você convidou " + mQuantidadeConvidados + " pessoas." , Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(CadContato.this, "Falha", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Log.i(TAG, ex.getMessage());
            }
        }
    }

    public class visualizarContatos extends AsyncTask<Void,Integer,Void >{
        boolean fg_conexao_internet;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this){
                try {
                    fg_conexao_internet = util.verificaInternet(CadContato.this.getApplicationContext());
                    if (fg_conexao_internet) {
                        mContatos = objContatos.buscarConvidados(CadContato.this.getApplicationContext(), codigoEvento, util);
                        mAdapter = new CustomListViewContato(CadContato.this, mContatos, false);
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
        }
    }


    //endregion
}
