package project.myapplication;

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

        ckPermitePush = (CheckBox)findViewById(R.id.ckPermitePush);
        ckPermiteAlarme = (CheckBox)findViewById(R.id.ckPermiteAlarme);
        ckNotificaComentario = (CheckBox)findViewById(R.id.ckNotificaComentario);
        ckNotificaMudanca = (CheckBox)findViewById(R.id.ckNotificaMudanca);
        ckTelefoneVisivel = (CheckBox)findViewById(R.id.ckTelefoneVisivel);

        clsConfiguracoes objConfig = null;
        ConfiguracoesDAO config_dao = new ConfiguracoesDAO(this.getApplicationContext());
        objConfig = config_dao.Carregar();

        //Carrega as notificações
       if(objConfig.getPermitePush()==true)
       {ckPermitePush.setChecked(true);}
       else
       {ckPermitePush.setChecked(false);}

        if(objConfig.getPermiteAlarme()==true)
        { ckPermiteAlarme.setChecked(true);}
        else
        {ckPermiteAlarme.setChecked(false);}

        if(objConfig.getNotificaComentario()==true)
        {ckNotificaComentario.setChecked(true);}
        else
        {ckNotificaComentario.setChecked(false);}

        if(objConfig.getNotificaMudanca()==true)
        {ckNotificaMudanca.setChecked(true);}
        else
        {ckNotificaMudanca.setChecked(false);}

        if(objConfig.getTelefoneVisivel()==true)
        {ckTelefoneVisivel.setChecked(true);}
        else
        {ckTelefoneVisivel.setChecked(false);}
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
