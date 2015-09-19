package project.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import adapters.CustomListViewContato;
import domain.Contatos;
import domain.Util;


public class CadContato extends AppCompatActivity{

    private ListView lvContatos;
    private Contatos objContatos;
    private Button btConfirmar;
    private int codigoEvento = 0;
    private boolean cbContatoVisivel = false;
    private List<Contatos> contatosList;
    private CustomListViewContato arrayAdapter;
    private Util util;
    MenuItem mActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_padrao_contatos);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            //region Vinculação com XML
            lvContatos = (ListView)findViewById(R.id.lvContatos);
            btConfirmar = (Button)findViewById(R.id.btConfirmar);
            //endregion

            objContatos = new Contatos();
            util = new Util();

            Bundle parameters = getIntent().getExtras();
            if(parameters != null) {
                codigoEvento = parameters.getInt("codigoEvento");
                btConfirmar.setVisibility(View.VISIBLE);
                cbContatoVisivel = true;
            }
            contatosList = objContatos.retonarContatos(this);
            arrayAdapter = new CustomListViewContato(this, contatosList, cbContatoVisivel);
            lvContatos.setAdapter(arrayAdapter);

        }catch (Exception ex)
        {
            this.finish();
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    //region Métodos do Android

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            this.finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        if (id == R.id.action_settings) {
            new sincrozinarContatos().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_padrao_contatos, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mActionProgressItem = menu.findItem(R.id.miActionProgress);
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(mActionProgressItem);
        return super.onPrepareOptionsMenu(menu);
    }

    //endregion

    //region Métodos de implementação

    public class sincrozinarContatos extends AsyncTask<Void,Integer,Void>{

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            mActionProgressItem.setVisible(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                objContatos.AtualizarContatos(getContentResolver(),getString(R.string.padrao_contatos),CadContato.this);
                List<Contatos> contatosList = objContatos.retonarContatos(CadContato.this);
                arrayAdapter = new CustomListViewContato(CadContato.this, contatosList, cbContatoVisivel);


            }catch (Exception ex)
            {
                Toast.makeText(CadContato.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                CadContato.this.finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            mActionProgressItem.setVisible(false);
            lvContatos.setAdapter(arrayAdapter);
        }
    }

    public void onCLickConvidarContatos(View view){
        boolean checked ;
        boolean selecionou = false;
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < lvContatos.getCount(); i++)
        {
            checked = arrayAdapter.isChecked(i);
            if (checked)
            {
                selecionou = true;
                JSONObject aux = new JSONObject();
                try {
                    aux.put("cd_usuario", String.valueOf(arrayAdapter.getValue(i)));
                    aux.put("cd_evento", String.valueOf(codigoEvento));
                    jsonArray.put(aux);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        if(selecionou) {

            try {
                jsonObject.put("convidados", jsonArray);
                util.enviarServidor(getString(R.string.wsBlueDate), jsonObject.toString(), "send-json");

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this,"Não foi selecionado nenhum contato", Toast.LENGTH_LONG).show();
        }

    }


    //endregion
}
