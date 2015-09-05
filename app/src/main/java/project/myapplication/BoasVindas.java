package project.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import domain.Configuracoes;
import domain.Util;

public class BoasVindas extends Activity {

    private Configuracoes objConfig;
    private Util util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_padrao_boas_vindas);

            util = new Util();
            util.validarTela(this, 1);
        }catch (Exception e){
            Toast.makeText(BoasVindas.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick_Avancar(View v)
    {
        try {
            objConfig = new Configuracoes();
            objConfig.carregar(this);
            objConfig.atualizarStatus(this, 2);
            startActivity(new Intent(this, CadTelefone.class));

        }catch (Exception ex)
        {
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}
