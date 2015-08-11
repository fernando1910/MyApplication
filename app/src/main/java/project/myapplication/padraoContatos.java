package project.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class padraoContatos extends ActionBarActivity {

    private ListView lvContatos;
    private TextView itemTextView;
    private clsContatos objContatos;
    private Button btConfirmar;
    private int codigoEvento = 0;
    boolean cbContatoVisivel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_contatos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region Vinculação com XML
        itemTextView = (TextView)findViewById(R.id.itemTextView);
        lvContatos = (ListView)findViewById(R.id.lvContatos);
        btConfirmar = (Button)findViewById(R.id.btConfirmar);
        //endregion

        objContatos = new clsContatos();

        Bundle parameters = getIntent().getExtras();
        if(parameters != null) {
            codigoEvento = parameters.getInt("codigoEvento");
            btConfirmar.setVisibility(View.VISIBLE);
            cbContatoVisivel = true;
        }

        try {

            List<clsContatos> contatosList = objContatos.retonarContatos(this);
            final CustomListViewContato arrayAdapter = new CustomListViewContato(this, contatosList, cbContatoVisivel);
            lvContatos.setAdapter(arrayAdapter);
        }catch (Exception ex)
        {
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (codigoEvento == 0)
        {
            this.finish();
            startActivity(new Intent(this, padraoConfiguracao.class));
        }
        else
        {
            this.finish();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            if (codigoEvento == 0)
            {
                this.finish();
                startActivity(new Intent(this, padraoConfiguracao.class));
            }
            else
            {
                this.finish();
            }

            return true;
        }


        if (id == R.id.action_settings) {
            atualizarContatos();
            return id == R.id.action_settings;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_padrao_contatos, menu);
        return true;
    }

    public void atualizarContatos()
    {
        try{
            objContatos.AtualizarContatos(getContentResolver(),getString(R.string.padrao_contatos),this);
        }catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        try {

            List<clsContatos> contatosList = objContatos.retonarContatos(this);
            final CustomListViewContato arrayAdapter = new CustomListViewContato(this, contatosList, cbContatoVisivel);
            lvContatos.setAdapter(arrayAdapter);
        }catch (Exception ex)
        {
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}
