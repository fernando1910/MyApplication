package project.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import java.io.InputStream;
import java.net.URL;

import domain.Evento;
import domain.Util;


public class VisulizarEvento extends AppCompatActivity implements View.OnClickListener {
    final private String TAG = "VisualizarEvento";
    private TextView tvTituloEvento, tvDescricaoEvento, tvEndereco, tvPrivado, tvTituloEvento2;
    private Util util;
    private int codigoEvento;
    private Evento objEvento;
    private ImageView ivEvento;
    private CollapsingToolbarLayout collapsingToolbarLayout ;
    private Toolbar mToolbar;
    private FloatingActionMenu mFloatingActionMenu;
    private RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_padrao_visulizar_evento);


            //region Vinculação das variaveis com o layout XML
            ivEvento = (ImageView) findViewById(R.id.ivEvento);
            tvPrivado = (TextView) findViewById(R.id.tvPrivado);
            tvDescricaoEvento = (TextView) findViewById(R.id.tvDescricaoEvento);
            mFloatingActionMenu = (FloatingActionMenu) findViewById(R.id.menu);
            mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
            tvEndereco = (TextView)findViewById(R.id.tvEndereco);

            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


            mToolbar = (Toolbar) findViewById(R.id.tb_main);
            setSupportActionBar(mToolbar);

            try {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(false);
            } catch (Exception e) {
                Toast.makeText(VisulizarEvento.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            mFloatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
                @Override
                public void onMenuToggle(boolean b) {

                }
            });

            FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
            FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
            FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
            FloatingActionButton fab4 = (FloatingActionButton) findViewById(R.id.fab4);
            fab1.setOnClickListener(this);
            fab2.setOnClickListener(this);
            fab3.setOnClickListener(this);
            fab4.setOnClickListener(this);

            //endregion

            util = new Util();

            Bundle parameters = getIntent().getExtras();
            if (parameters != null) {
                codigoEvento = parameters.getInt("codigoEvento");
                final String url = getString(R.string.caminho_foto_capa_evento) + String.valueOf(codigoEvento) + ".png";
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            ivEvento.setImageDrawable(Drawable.createFromStream((InputStream) new URL(url).getContent(), "src"));
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                };
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                objEvento = new Evento();
                objEvento.carregarLocal(codigoEvento, this);
                collapsingToolbarLayout.setTitle(objEvento.getTituloEvento());
                tvDescricaoEvento.setText(objEvento.getDescricao());
                tvEndereco.setText(objEvento.getEndereco());
                if (objEvento.getEventoPrivado() == 1)
                    tvPrivado.setText("Este evento é privado");
                else
                    tvPrivado.setText("Este evento é publico");


            } else {
                Toast.makeText(getApplicationContext(), "Falha ao carregar o evento", Toast.LENGTH_LONG).show();

            }

            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    try {
                        objEvento.classificarEventoLocal(getApplicationContext(),rating);
                    } catch (Exception e) {
                        Log.i(TAG,e.getMessage());
                    }
                }
            });

        }catch (Exception ex){
            Toast.makeText(VisulizarEvento.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_padrao_visulizar_evento, menu);
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
        if (id == R.id.action_convidar)
        {
            Intent intent = new Intent(this,CadContato.class);
            intent.putExtra("codigoEvento",codigoEvento);
            startActivity(intent);

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
                editarEvento();
                break;

        }
    }
}
