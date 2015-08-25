package project.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import classes.Configuracoes;
import classes.Util;


public class ValidarTelefone extends Activity {
    private Configuracoes objConfig;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_validar_telefone);

        objConfig = new Configuracoes();
        util = new Util();

        util.validarTela(this, 3);

        objConfig.carregar(this);

    }

    public void onClick_Avancar(View v){
        objConfig = new Configuracoes();
        objConfig.atualizarStatus(this,4);
        startActivity(new Intent(this,CadPerfil.class));

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
