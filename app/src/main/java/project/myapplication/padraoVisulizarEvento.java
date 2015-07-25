package project.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class padraoVisulizarEvento extends ActionBarActivity {

    TextView tvTituloEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_visulizar_evento);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTituloEvento = (TextView)findViewById(R.id.tvTituloEvento);

        Bundle parameters = getIntent().getExtras();
        if(parameters != null)
        {
            int codigoEvento =  parameters.getInt("codigoEvento");
            clsEvento objEvento = new clsEvento();
            objEvento = objEvento.carregar(String.valueOf(codigoEvento),getString(R.string.padrao_evento));
            tvTituloEvento.setText(objEvento.getTituloEvento());

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Falha ao carregar o evento", Toast.LENGTH_LONG);

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
            startActivity(new Intent(this,padraoMeusEventos.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(this,padraoMeusEventos.class));

    }
}
