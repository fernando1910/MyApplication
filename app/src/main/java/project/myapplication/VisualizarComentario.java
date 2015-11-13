package project.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import domain.Comentario;
import domain.Util;

public class VisualizarComentario extends AppCompatActivity {
    private final String TAG = "ERRO";
    private int cd_comentario;
    private ProgressDialog mProgressDialog;
    private Comentario objComentario;
    private Util util;
    private TextView tvComentario, tvUsuario, tvDataInclusao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_comentario);

        tvComentario = (TextView) findViewById(R.id.tvComentario);
        tvUsuario = (TextView) findViewById(R.id.tvUsuario);
        tvDataInclusao = (TextView) findViewById(R.id.tvDataInclusao);

        objComentario = new Comentario();
        util = new Util();
        Bundle parameters = getIntent().getExtras();
        if (parameters != null) {
            cd_comentario = parameters.getInt("codigoComentario");
            if (cd_comentario != 0)
                new carregarComentario().execute();
        }
    }

    private void carregarControles(){
        if (objComentario != null){
            tvComentario.setText(objComentario.getComentario());
            tvUsuario.setText(objComentario.getUsuarioComentario());
            tvDataInclusao.setText(util.formatarDataTela(objComentario.getDataInclusao()));
        }
    }

    private class carregarComentario extends AsyncTask<Void,Integer, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(VisualizarComentario.this);
            mProgressDialog = ProgressDialog.show(VisualizarComentario.this, "Carregando...",
                    "Por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this){
                try{
                    objComentario.carregar(VisualizarComentario.this, util, cd_comentario);
                }catch (Exception ex){
                    Log.i(TAG, ex.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            carregarControles();
        }
    }
}
