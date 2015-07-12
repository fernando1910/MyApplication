package project.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;

public class padraoNotificacao extends ActionBarActivity {
    CheckBox ckPermitePush;
    CheckBox ckPermiteAlarme;
    CheckBox ckNotificaComentario;
    CheckBox ckNotificaMudanca;
    CheckBox ckTelefoneVisivel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_notificacao);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ckPermitePush = (CheckBox)findViewById(R.id.ckPermitePush);
        ckPermiteAlarme = (CheckBox)findViewById(R.id.ckPermiteAlarme);
        ckNotificaComentario = (CheckBox)findViewById(R.id.ckNotificaComentario);
        ckNotificaMudanca = (CheckBox)findViewById(R.id.ckNotificaMudanca);
        ckTelefoneVisivel = (CheckBox)findViewById(R.id.ckTelefoneVisivel);

        clsConfiguracoes objConfig = null;
        ConfiguracoesDAO config_dao = new ConfiguracoesDAO(this.getApplicationContext());
        objConfig = config_dao.Carregar();

        //Carrega as notificações
       if(objConfig.getPermitePush()== 1)
            ckPermitePush.setChecked(true);

        if(objConfig.getPermiteAlarme()==1)
            ckPermiteAlarme.setChecked(true);

        if(objConfig.getNotificaComentario()==1)
            ckNotificaComentario.setChecked(true);

        if(objConfig.getNotificaMudanca()==1)
            ckNotificaMudanca.setChecked(true);

        if(objConfig.getTelefoneVisivel()==1)
            ckTelefoneVisivel.setChecked(true);
        //Verificar se a Check ta ticada ou não e alterar
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
            this.finish();
            startActivity(new Intent(this,padraoMenu.class));
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
