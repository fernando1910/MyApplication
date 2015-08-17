package project.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class padraoComentario extends ActionBarActivity {

    private int codigoEvento, codigoUsuario;
    private EditText etComentario;
    private clsEvento objEvento;
    private String comentario;
    private ProgressDialog progressDialog;
    private clsUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_comentario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    //region
        etComentario = (EditText) findViewById(R.id.etComentario);
    //endregion

        Bundle parameters = getIntent().getExtras();
        if(parameters != null) {
            codigoEvento = parameters.getInt("codigoEvento");
        }

        objEvento = new clsEvento();
        util = new clsUtil();
        clsUsuario objUsuario = new clsUsuario();
        objUsuario.carregar(this);
        codigoUsuario = objUsuario.getCodigoUsuario();

        //imprementar comentarios
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

    public boolean validarCampos()
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

    public class comentar extends AsyncTask<Void,Integer,Void>
    {
        @Override
        protected void onPreExecute()
        {
            //Create a new progress dialog
            progressDialog = new ProgressDialog(padraoComentario.this);

            progressDialog = ProgressDialog.show(padraoComentario.this,"Carregando...",
                    "Enviando seu comentario, por favor aguarde...", false, false);
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                synchronized (this) {
                    objEvento.enviarComentario(codigoEvento, codigoUsuario, comentario, getString(R.string.padrao_comentar));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
