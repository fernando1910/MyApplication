package project.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class padraoCadTelefone extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_cad_telefone);
        Button btAvancar = (Button)findViewById(R.id.btAvancar);
    }

    public void onClick_Avancar(View v)
    {
        EnviarSms();
        Toast.makeText(this, "Vai KAraaaai", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, padraoLogin.class));

    }

    public boolean EnviarSms(){
        EditText etTelefone = (EditText)findViewById(R.id.etTelefone);
        String numero = etTelefone.getText().toString();
        String mensagem = "Teste";
        clsSms sms = new clsSms();
        sms.enviarSms(padraoCadTelefone.this,numero,mensagem);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_padrao_cad_telefone, menu);
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
