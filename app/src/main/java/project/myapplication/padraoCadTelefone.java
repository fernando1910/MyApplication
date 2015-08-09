package project.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class padraoCadTelefone extends Activity {
    EditText etTelefone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_cad_telefone);
        etTelefone = (EditText)findViewById(R.id.etTelefone);
    }

    public void onClick_Avancar(View v)
    {
        if(SalvarTelefone()){
            Toast.makeText(this, "Telefone Salvo!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, padraoLogin.class));
        }
        else{
            Toast.makeText(this, "Não Deu certo não Sofra!", Toast.LENGTH_SHORT).show();
        }


    }

    public boolean SalvarTelefone(){
        clsUsuario objUsuario = new clsUsuario();
        try
        {

            objUsuario.setTelefone(etTelefone.getText().toString());

            String usuario = objUsuario.gerarUsuarioJSON(objUsuario);
            int codigoUsuario = Integer.parseInt(EnviarTelefoneServidor(usuario));

            if (codigoUsuario == 0)
            {
                Toast.makeText(this, "Erro1:Telefone não foi Salvo", Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                //objUsuario.setCodigoUsuario(codigoUsuario);
                //objUsuario.InserirUsuario(this.getApplicationContext(), objUsuario);
                return true;
            }
        }catch (Exception e)
        {
            Toast.makeText(this, "Erro2: Telefone não foi salvo", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    public String EnviarTelefoneServidor(final String data)throws InterruptedException{
        final String[] resposta = new String[1];
        try{
            Thread thread =  new Thread( new Runnable(){
                public void run(){
            resposta[0] =  project.myapplication.HttpConnection.getSetDataWeb(getString(R.string.padrao_telefone), "send-json",data);
            }
        });
        thread.start();

            thread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resposta[0];

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
