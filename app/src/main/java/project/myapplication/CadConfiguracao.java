package project.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import domain.Configuracoes;

public class CadConfiguracao extends AppCompatActivity {
    private CheckBox ckPermitePush, ckPermiteAlarme, ckNotificaComentario, ckNotificaMudanca, ckTelefoneVisivel, ckBuscarFotosOnline;
    private Configuracoes objConfig;
    private Spinner spKm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_padrao_notificacao);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ckPermitePush = (CheckBox) findViewById(R.id.ckPermitePush);
            ckPermiteAlarme = (CheckBox) findViewById(R.id.ckPermiteAlarme);
            ckNotificaComentario = (CheckBox) findViewById(R.id.ckNotificaComentario);
            ckNotificaMudanca = (CheckBox) findViewById(R.id.ckNotificaMudanca);
            ckTelefoneVisivel = (CheckBox) findViewById(R.id.ckTelefoneVisivel);
            ckBuscarFotosOnline = (CheckBox) findViewById(R.id.ckBuscarFotosOnline);

            spKm = (Spinner) findViewById(R.id.spKm);

            objConfig = new Configuracoes();
            objConfig.carregar(this);
            //region Carrega as notificações
            if (objConfig.getPermitePush() == 1)
                ckPermitePush.setChecked(true);

            if (objConfig.getPermiteAlarme() == 1)
                ckPermiteAlarme.setChecked(true);

            if (objConfig.getNotificaComentario() == 1)
                ckNotificaComentario.setChecked(true);

            if (objConfig.getNotificaMudanca() == 1)
                ckNotificaMudanca.setChecked(true);

            if (objConfig.getNotificaMudanca() == 1)
                ckTelefoneVisivel.setChecked(true);


            // endregion

        }catch (Exception e){
            Toast.makeText(CadConfiguracao.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_padrao_notificacao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            SalvarConfiguracoes();
            this.finish();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SalvarConfiguracoes();
        this.finish();
    }

    public void SalvarConfiguracoes()
    {

        if(ckPermitePush.isChecked())
            objConfig.setPermitePush(1);
        else
            objConfig.setPermitePush(0);

        if(ckPermiteAlarme.isChecked())
            objConfig.setPermiteAlarme(1);
        else
            objConfig.setPermiteAlarme(0);

        if(ckNotificaComentario.isChecked())
            objConfig.setNotificaComentario(1);
        else
            objConfig.setNotificaComentario(0);

        if(ckNotificaMudanca.isChecked())
            objConfig.setNotificaMudanca(1);
        else
            objConfig.setNotificaMudanca(0);


        if(ckTelefoneVisivel.isChecked())
            objConfig.setTelefoneVisivel(1);
        else
            objConfig.setTelefoneVisivel(0);

        objConfig.atualizar(this);

    }
}
