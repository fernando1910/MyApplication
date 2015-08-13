package project.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class padraoValidarTelefone extends Activity {
    clsConfiguracoes objConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_validar_telefone);

        clsConfiguracoes objConfig = null;
        ConfiguracoesDAO config_dao = new ConfiguracoesDAO(this.getApplicationContext());
        objConfig = config_dao.Carregar();

        switch (objConfig.getStatusPerfil())
        {
            case 0:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case 1:
                startActivity(new Intent(this,padraoBoasVindas.class));
                break;
            case 2:
                startActivity(new Intent(this,padraoCadTelefone.class));
                break;
            case 4:
                startActivity(new Intent(this,padraoLogin.class));
                break;
        }
    }

    public void onClick_Avancar(View v){

        ConfiguracoesDAO config_dao = new ConfiguracoesDAO(padraoValidarTelefone.this);
        objConfig = config_dao.Carregar();
        objConfig.setStatusPerfil(4);
        config_dao.Atualizar(objConfig);

        startActivity(new Intent(this,padraoLogin.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_padrao_validar_telefone, menu);
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
