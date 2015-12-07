package project.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
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

import java.util.Date;

import domain.Evento;
import domain.Usuario;
import domain.Util;


public class VisualizarEvento extends AppCompatActivity implements View.OnClickListener {
    final private String TAG = "ERRO";
    private TextView tvDescricaoEvento, tvPrivado, tvEndereco, tvDataHora;
    private Util util;
    private int codigoEvento = Integer.MIN_VALUE;
    private Evento objEvento;
    private Usuario objUsuario;
    private ImageView ivEvento;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private RatingBar mRatingBar;
    private ProgressDialog mProgressDialog;
    private FloatingActionButton fab4, fab3, fab1, fab2;
    Menu menu;

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
            menu = (Menu) findViewById(R.id.action_editar);

            mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


            Toolbar mToolbar = (Toolbar) findViewById(R.id.tb_main);
            setSupportActionBar(mToolbar);

            try {
                ActionBar mActionBar = getSupportActionBar();
                if (mActionBar != null) {
                    mActionBar.setDisplayHomeAsUpEnabled(true);
                    mActionBar.setHomeButtonEnabled(false);
                }
            } catch (Exception e) {
                Toast.makeText(VisualizarEvento.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            mFloatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
                @Override
                public void onMenuToggle(boolean b) {

                }
            });

            fab1 = (FloatingActionButton) findViewById(R.id.fab1);
            fab2 = (FloatingActionButton) findViewById(R.id.fab2);
            fab3 = (FloatingActionButton) findViewById(R.id.fab3);
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
                if (codigoEvento == Integer.MIN_VALUE || codigoEvento == 0) {
                    Toast.makeText(VisualizarEvento.this, "Houve um problema", Toast.LENGTH_SHORT).show();
                    VisualizarEvento.this.finish();
                    startActivity(new Intent(VisualizarEvento.this, MenuPrincipalNovo.class));
                } else {
                    final String url = getString(R.string.caminho_foto_capa_evento) + String.valueOf(codigoEvento) + ".png";
                    Picasso.with(ivEvento.getContext()).load(url).placeholder(R.drawable.ic_placeholder_evento).into(ivEvento);
                    objEvento = new Evento();
                    new carregarEventoOnline().execute();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Falha ao carregar o evento", Toast.LENGTH_LONG).show();

            }

            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    final float ind_classificacao = rating;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                objEvento.classificarEvento(getApplicationContext(), codigoEvento, ind_classificacao, objUsuario.getCodigoUsuario());
                            } catch (Exception e) {
                                Log.i(TAG, e.getMessage());
                            }
                        }
                    }).start();
                }
            });

        } catch (Exception ex) {
            Toast.makeText(VisualizarEvento.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void carregarControles() {
        mCollapsingToolbarLayout.setTitle(objEvento.getTituloEvento());
        tvDescricaoEvento.setText(objEvento.getDescricao());
        tvEndereco.setText(objEvento.getEndereco());
        if (objEvento.getEventoPrivado() == 1) {
            tvPrivado.setText("Este evento é privado");
            if (objEvento.getCodigoUsuarioInclusao() != objUsuario.getCodigoUsuario()) {
                fab4.setVisibility(View.GONE);
            }
        } else {
            tvPrivado.setText("Este evento é publico");
        }
        if (objEvento.getParticipa() == 1 || objEvento.getCodigoUsuarioInclusao() == objUsuario.getCodigoUsuario()) {
            fab3.setVisibility(View.VISIBLE);
            fab1.setVisibility(View.VISIBLE);
            fab4.setVisibility(View.GONE);

        }

        String mDataHora = util.formatarDataTela(objEvento.getDataEvento()) + " ás " + util.formatarHoraTela(objEvento.getDataEvento());
        tvDataHora.setText(mDataHora);
        mRatingBar.setRating(objEvento.getClassificacao());

        // evento antes da data de hoje
        if (new Date().compareTo(objEvento.getDataEvento()) != -1) {
            fab3.setVisibility(View.GONE);
            fab4.setVisibility(View.GONE);
        } else {
            mRatingBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_padrao_visualizar_evento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        if (id == R.id.action_editar) {
            this.finish();
            editarEvento();
            return true;
        }

        if (id == R.id.action_convidados) {
            Intent intent = new Intent(this, VisualizarConvidados.class);
            intent.putExtra("codigoEvento", codigoEvento);
            startActivity(intent);
            return true;

        }

        if (id == R.id.action_cancelar) {
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
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem itemEditar = menu.findItem(R.id.action_editar);
        MenuItem itemCancelar = menu.findItem(R.id.action_cancelar);
        if (objEvento.getCodigoUsuarioInclusao() == objUsuario.getCodigoUsuario() && new Date().compareTo(objEvento.getDataEvento()) != 1) {
            itemEditar.setVisible(true);
            itemCancelar.setVisible(true);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

    }

    public void visualizarMapa() {
        String nr_latitude = String.valueOf(objEvento.getLatitude());
        String nr_longitude = String.valueOf(objEvento.getLongitude());
        String ds_evento = objEvento.getTituloEvento();

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + nr_latitude + "," + nr_longitude + "(" + ds_evento + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }

    public void comentar() {
        Intent intent = new Intent(this, CadComentario.class);
        intent.putExtra("codigoEvento", codigoEvento);
        startActivity(intent);
    }

    public void convidar() {
        Intent intent = new Intent(this, CadContato.class);
        intent.putExtra("codigoEvento", codigoEvento);
        startActivity(intent);
    }

    public void editarEvento() {
        Intent intent = new Intent(this, CadEvento.class);
        intent.putExtra("codigoEvento", codigoEvento);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                new participarEvento().execute();
                break;

        }
    }

    private class carregarEventoOnline extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(VisualizarEvento.this);
            mProgressDialog = ProgressDialog.show(VisualizarEvento.this, "Carregando...",
                    "Estamos carregando as últimas informações do evento, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this) {
                try {
                    objEvento.carregarOnline(codigoEvento, getApplicationContext(), objUsuario.getCodigoUsuario());
                } catch (Exception ex) {
                    objEvento.setCodigoEvento(Integer.MIN_VALUE);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (objEvento.getCodigoEvento() == Integer.MIN_VALUE) {
                Toast.makeText(VisualizarEvento.this.getApplicationContext(), "Falha ao carregar o evento", Toast.LENGTH_SHORT).show();
                VisualizarEvento.this.finish();
            } else
                carregarControles();
            mProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            onBackPressed();
        }
    }

    private class cancelarEvento extends AsyncTask<Void, Integer, Void> {
        boolean fg_cancelado;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(VisualizarEvento.this);
            mProgressDialog = ProgressDialog.show(VisualizarEvento.this, "Carregando...",
                    "Estamos cancelando o evento, por favor aguarde...", false, false);
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

    private class participarEvento extends AsyncTask<Void, Integer, Void> {
        private boolean fg_participa;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(VisualizarEvento.this);
            mProgressDialog = ProgressDialog.show(VisualizarEvento.this, "Carregando...",
                    "Estamos confirmando a sua presença, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                fg_participa = objEvento.participar(VisualizarEvento.this, objUsuario.getCodigoUsuario(), codigoEvento, objUsuario.getNome());
            } catch (Exception e) {
                mProgressDialog.dismiss();
                Log.i(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressDialog.dismiss();
            if (fg_participa)
                Toast.makeText(VisualizarEvento.this, "Confirmado", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(VisualizarEvento.this, "Falha ao confirmar sua presença", Toast.LENGTH_SHORT).show();
        }
    }
}
