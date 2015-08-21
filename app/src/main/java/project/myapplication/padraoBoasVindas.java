package project.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

public class padraoBoasVindas extends Activity {

    clsConfiguracoes objConfig;
    clsUtil util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_boas_vindas);

        util = new clsUtil();
        util.validarTela(this,1);

    }

    public void onClick_Avancar(View v)
    {
        try {
            objConfig = new clsConfiguracoes();
            objConfig.carregar(this);
            objConfig.setStatusPerfil(2);
            startActivity(new Intent(this, padraoCadTelefone.class));

        }catch (Exception ex)
        {
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}
