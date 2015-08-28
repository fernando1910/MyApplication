package project.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

import classes.Evento;
import classes.Util;


public class VisulizarEvento extends AppCompatActivity {

    private TextView tvTituloEvento, tvDescricaoEvento, tvEndereco, tvPrivado, tvTituloEvento2;
    private Util util;
    private int codigoEvento;
    private Evento objEvento;
    private ImageView ivEvento;
    private CollapsingToolbarLayout collapsingToolbarLayout ;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_visulizar_evento);


        //region Vinculação das variaveis com o layout XML
        ivEvento = (ImageView)findViewById(R.id.ivEvento);
        tvTituloEvento = (TextView)findViewById(R.id.tvTituloEvento);
        tvDescricaoEvento = (TextView)findViewById(R.id.tvDescricaoEvento);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Seu evento");

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(mToolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(false);
        }catch (Exception e){
            Toast.makeText(VisulizarEvento.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
/*


        tvEndereco = (TextView)findViewById(R.id.tvEndereco);
        tvPrivado = (TextView)findViewById(R.id.tvPrivado);
*/
        //endregion

        util = new Util();

  //      tvEndereco.setCompoundDrawables(null,null,util.retornarIcone(getResources().getDrawable(R.drawable.ic_localizacao), getResources()),null);

        Bundle parameters = getIntent().getExtras();
        if(parameters != null)
        {
            codigoEvento =  parameters.getInt("codigoEvento");
            final String url = getString(R.string.caminho_foto_capa_evento) + String.valueOf(codigoEvento) + ".png";
            Thread thread = new Thread(){
                public void run()
                {
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
            objEvento = objEvento.carregar(String.valueOf(codigoEvento),getString(R.string.padrao_evento));
            tvTituloEvento.setText(objEvento.getTituloEvento());
            tvDescricaoEvento.setText(objEvento.getDescricao());
            /*
            tvEndereco.setText(objEvento.getEndereco());
            if (objEvento.getEventoPrivado() == 1)
                tvPrivado.setText("Este evento é privado");
            else
                tvPrivado.setText("Este evento é publico");
            */

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Falha ao carregar o evento", Toast.LENGTH_LONG);

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
        startActivity(new Intent(this,PainelMeusEventos.class));

    }

    public void visualizarMapa(View view)
    {
        String nr_latitude = String.valueOf(objEvento.getLatitude());
        String nr_longitude = String.valueOf(objEvento.getLongitude());
        String ds_evento = objEvento.getTituloEvento();

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" +nr_latitude+","+nr_longitude+"("+ds_evento+")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }

    public void onClickChamarComentarios(View view)
    {
        Intent intent = new Intent();
        intent.putExtra("codigoEvento", codigoEvento);
        startActivity(intent);
    }
}
