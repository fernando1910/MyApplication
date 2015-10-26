package project.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

import domain.Evento;
import domain.Usuario;
import domain.Util;


public class VisualizarEvento extends AppCompatActivity implements View.OnClickListener {
    final private String TAG = "ERRO";
    private TextView tvDescricaoEvento, tvPrivado, tvEndereco,tvDataHora;
    private Util util;
    private int codigoEvento = Integer.MIN_VALUE;
    private Evento objEvento;
    private Usuario objUsuario;
    private ImageView ivEvento;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private RatingBar mRatingBar;
    private ProgressDialog mProgressDialog;
    FloatingActionButton fab4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_padrao_visulizar_evento);


            //region Vinculação das variaveis com o layout XML
            ivEvento = (ImageView) findViewById(R.id.ivEvento);
            tvPrivado = (TextView) findViewById(R.id.tvPrivado);
            tvDescricaoEvento = (TextView) findViewById(R.id.tvDescricaoEvento);
            FloatingActionMenu mFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.menu);
            mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
            tvEndereco = (TextView) findViewById(R.id.tvEndereco);
            tvDataHora = (TextView) findViewById(R.id.tvDataHora);

            mCollapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


            Toolbar mToolbar = (Toolbar) findViewById(R.id.tb_main);
            setSupportActionBar(mToolbar);

            try {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(false);
            } catch (Exception e) {
                Toast.makeText(VisualizarEvento.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            mFloatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
                @Override
                public void onMenuToggle(boolean b) {

                }
            });

            FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
            FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
            FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
            fab4 = (FloatingActionButton) findViewById(R.id.fab4);
            fab1.setOnClickListener(this);
            fab2.setOnClickListener(this);
            fab3.setOnClickListener(this);
            fab4.setOnClickListener(this);
            //endregion

            util = new Util();
            objUsuario = new Usuario();
            objUsuario.carregar(this);

            Bundle parameters = getIntent().getExtras();
            if (parameters != null) {
                codigoEvento = parameters.getInt("codigoEvento");
                if (codigoEvento == Integer.MIN_VALUE){
                    Toast.makeText(VisualizarEvento.this, "Houve um problema", Toast.LENGTH_SHORT).show();
                    VisualizarEvento.this.finish();
                    startActivity(new Intent(VisualizarEvento.this, MenuPrincipalNovo.class));
                }
                final String url = getString(R.string.caminho_foto_capa_evento) + String.valueOf(codigoEvento) + ".png";
                Picasso.with(ivEvento.getContext()).load(url).placeholder(R.drawable.ic_placeholder_evento).into(ivEvento);

                objEvento = new Evento();
                objEvento.carregarLocal(codigoEvento, this);
                if (objEvento.getCodigoEvento() != 0)
                    carregarControles();
                else
                    new carregarEventoOnline().execute();

            } else {
                Toast.makeText(getApplicationContext(), "Falha ao carregar o evento", Toast.LENGTH_LONG).show();

            }

            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    try {
                        objEvento.classificarEvento(getApplicationContext(), codigoEvento, rating, objUsuario.getCodigoUsuario());
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                }
            });

        }catch (Exception ex){
            Toast.makeText(VisualizarEvento.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void carregarControles(){
        mCollapsingToolbarLayout.setTitle(objEvento.getTituloEvento());
        tvDescricaoEvento.setText(objEvento.getDescricao());
        tvEndereco.setText(objEvento.getEndereco());
        if (objEvento.getEventoPrivado() == 1) {
            tvPrivado.setText("Este evento é privado");
            if (objEvento.getCodigoUsarioInclusao() != objUsuario.getCodigoUsuario()){
                fab4.setVisibility(View.GONE);
            }
        }
        else {
            tvPrivado.setText("Este evento é publico");

        }


        String mDataHora = util.formatarDataTela(objEvento.getDataEvento()) + " ás " + util.formatarHoraTela(objEvento.getDataEvento());
        tvDataHora.setText(mDataHora);
        mRatingBar.setRating(objEvento.getClassificacao());



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_padrao_visualizar_evento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        if (id == R.id.action_editar){
            this.finish();
            editarEvento();
            return true;
        }

        if (id == R.id.action_convidados)
        {
            startActivity(new Intent(this, VisualizarConvidados.class));
            return true;

        }

        if (id == R.id.action_cancelar){
            try {
                new cancelarEvento().execute();
                Toast.makeText(this, "Evento cancelado!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Não foi possível cancelar o evento!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }

    public void visualizarMapa()
    {
        String nr_latitude = String.valueOf(objEvento.getLatitude());
        String nr_longitude = String.valueOf(objEvento.getLongitude());
        String ds_evento = objEvento.getTituloEvento();

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" +nr_latitude+","+nr_longitude+"("+ds_evento+")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }

    public void comentar()
    {
        Intent intent = new Intent(this,CadComentario.class);
        intent.putExtra("codigoEvento", codigoEvento);
        startActivity(intent);
    }

    public void convidar()
    {
        Intent intent = new Intent(this,CadContato.class);
        intent.putExtra("codigoEvento", codigoEvento);
        startActivity(intent);
    }

    public void editarEvento()
    {
        Intent intent = new Intent(this,CadEvento.class);
        intent.putExtra("codigoEvento", codigoEvento);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab1:
                comentar();
                break;

            case R.id.fab2:
                visualizarMapa();
                break;
            case R.id.fab3:
                convidar();
                break;

            case R.id.fab4:

                break;

        }
    }

    private class carregarEventoOnline extends AsyncTask<Void,Integer,Void>{
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(VisualizarEvento.this);
            mProgressDialog = ProgressDialog.show(VisualizarEvento.this, "Carregando...",
                    "Estamos validando seu perfil, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this){
                try {
                    objEvento.carregarOnline(codigoEvento, getApplicationContext());
                }catch ( Exception ex){
                    Toast.makeText(VisualizarEvento.this, "Falha ao carregar o evento", Toast.LENGTH_SHORT).show();
                    VisualizarEvento.this.finish();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            carregarControles();
            mProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressDialog.setProgress(values[0]);
        }
    }

    private class cancelarEvento extends AsyncTask<Void, Integer,Void>{
        boolean fg_cancelado;
        
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(VisualizarEvento.this);
            mProgressDialog = ProgressDialog.show(VisualizarEvento.this, "Carregando...",
                    "Estamos validando seu perfil, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                fg_cancelado = objEvento.cancelar(VisualizarEvento.this, codigoEvento);
            } catch (Exception e) {
                mProgressDialog.dismiss();
                Log.i(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressDialog.dismiss();
            if (fg_cancelado)
                Toast.makeText(VisualizarEvento.this, "Evento cancelado", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(VisualizarEvento.this, "Falha ao cancelar evento", Toast.LENGTH_SHORT).show();
        }
    }
}
