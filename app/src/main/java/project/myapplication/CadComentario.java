package project.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import adapters.CustomListViewComentario;
import domain.Comentario;
import domain.Evento;
import domain.Usuario;
import domain.Util;


public class CadComentario extends AppCompatActivity {
    private final String TAG = "LOG";
    private int codigoEvento, codigoUsuario;
    private EditText etComentario;
    private Evento objEvento;
    private String comentario;
    private ProgressDialog mProgressDialog;
    private Util util;
    private ListView lvComentarios;
    private List<Comentario> mComentarios;
    private CustomListViewComentario mAdapter;
    private Comentario objComentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_comentario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    //region Vinculação XML
        etComentario = (EditText) findViewById(R.id.etComentario);
        lvComentarios = (ListView) findViewById(R.id.lvComentarios);
    //endregion

        Bundle parameters = getIntent().getExtras();
        if(parameters != null) {
            codigoEvento = parameters.getInt("codigoEvento");
        }
        else {
            Toast.makeText(CadComentario.this, "Houve uma falha interna", Toast.LENGTH_SHORT).show();
            finish();
        }
        objEvento = new Evento();
        util = new Util();
        Usuario objUsuario = new Usuario();
        objUsuario.carregar(this);
        codigoUsuario = objUsuario.getCodigoUsuario();
        new buscarComentarios().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_padrao_comentario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void onClickComentar(View v)
    {
        if(validarCampos())
        {
            comentario = etComentario.getText().toString();
            new comentar().execute();
        }
    }

    private boolean validarCampos()
    {
        boolean validado = true;
        if (etComentario.getText().length() == 0) {
            Toast.makeText(this, "Comentario inválido", Toast.LENGTH_SHORT).show();
            validado = false;
        }
        else if(!util.verificaInternet(this))
        {
            Toast.makeText(this, "Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
            validado = false;
        }
        return  validado;
    }

    private class comentar extends AsyncTask<Void,Integer,Void>{
        @Override
        protected void onPreExecute()
        {
            //Create a new progress dialog
            mProgressDialog = new ProgressDialog(CadComentario.this);
            mProgressDialog = ProgressDialog.show(CadComentario.this,"Carregando...",
                    "Enviando seu comentario, por favor aguarde...", false, false);
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                synchronized (this) {
                    String mResposta = objEvento.enviarComentario(codigoEvento, codigoUsuario, comentario, getString(R.string.wsBlueDate));
                    Log.i(TAG,mResposta);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, e.getMessage());
                mProgressDialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressDialog.dismiss();
        }
    }

    private class buscarComentarios extends AsyncTask<Void,Integer,Void>{

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(CadComentario.this);
            mProgressDialog = ProgressDialog.show(CadComentario.this,"Carregando...",
                    "Buscando os ultimos comentários, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this){
                try{
                    mComentarios = objComentario.selecionarComentariosOnline(getApplicationContext(),codigoEvento);
                    mAdapter = new CustomListViewComentario(getApplicationContext(),mComentarios);
                }catch (Exception ex){
                    mProgressDialog.dismiss();
                    Log.i(TAG,ex.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressDialog.dismiss();
            lvComentarios.setAdapter(mAdapter);
            lvComentarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int codigoComentario = mAdapter.getCodigoComentario(position);
                    Intent intent = new Intent(getApplicationContext(), VisualizarComentario.class);
                    intent.putExtra("codigoComentario", codigoComentario);
                    startActivity(intent);
                }
            });
        }
    }

}
