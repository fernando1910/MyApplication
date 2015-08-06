package project.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class padraoContatos extends ActionBarActivity {

    ListView lvContatos;
    Cursor c;
    TextView itemTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_contatos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemTextView = (TextView)findViewById(R.id.itemTextView);
        lvContatos = (ListView)findViewById(R.id.lvContatos);
        clsContatos objContatos = new clsContatos();
        try{
            objContatos.AtualizarContatos(getContentResolver(),getString(R.string.padrao_contatos),this);
        }catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        try {

            List<clsContatos> contatosList = objContatos.retonarContatos(this);
            final CustomListViewContato arrayAdapter = new CustomListViewContato(this, contatosList);
            lvContatos.setAdapter(arrayAdapter);
        }catch (Exception ex)
        {
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(this,padraoConfiguracao.class));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            this.finish();
            startActivity(new Intent(this,padraoConfiguracao.class));
            return true;
        }


        if (id == R.id.action_settings) {
            return id == R.id.action_settings;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_padrao_contatos, menu);
        return true;


    }
}
