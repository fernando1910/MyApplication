package project.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class padraoVisulizarEvento extends ActionBarActivity {

    private TextView tvTituloEvento, tvDescricaoEvento, tvEndereco, tvPrivado;
    private clsUtil util;
    private int codigoEvento;
    private clsEvento objEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_visulizar_evento);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region Vinculação das variaveis com o layout XML

        tvTituloEvento = (TextView)findViewById(R.id.tvTituloEvento);
        tvDescricaoEvento = (TextView)findViewById(R.id.tvDescricaoEvento);
        tvEndereco = (TextView)findViewById(R.id.tvEndereco);
        tvPrivado = (TextView)findViewById(R.id.tvPrivado);

        //endregion

        util = new clsUtil();

        tvEndereco.setCompoundDrawables(null,null,util.retornarIcone(getResources().getDrawable(R.drawable.ic_localizacao), getResources()),null);

        Bundle parameters = getIntent().getExtras();
        if(parameters != null)
        {
            codigoEvento =  parameters.getInt("codigoEvento");
            objEvento = new clsEvento();
            objEvento = objEvento.carregar(String.valueOf(codigoEvento),getString(R.string.padrao_evento));
            tvTituloEvento.setText(objEvento.getTituloEvento());
            tvDescricaoEvento.setText(objEvento.getDescricao());
            tvEndereco.setText(objEvento.getEndereco());
            if (objEvento.getEventoPrivado() == 1)
                tvPrivado.setText("Este evento é privado");
            else
                tvPrivado.setText("Este evento é publico");


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
            Intent intent = new Intent(this,padraoContatos.class);
            intent.putExtra("codigoEvento",codigoEvento);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(this,padraoMeusEventos.class));

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
