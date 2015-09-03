package project.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import domain.Configuracoes;
import domain.Usuario;
import domain.Util;
import domain.clsSms;
import helpers.HttpConnection;

public class CadTelefone extends Activity {
    private EditText etTelefone;
    private ProgressDialog progressDialog;
    Configuracoes objConfig;
    String strCodigo;
    int codigoUsuario;
    private Util util;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_cad_telefone);
        etTelefone = (EditText)findViewById(R.id.etTelefone);
        util = new Util();
        strCodigo = util.gerarCodigo();
        util.validarTela(this,2);
    }

    public boolean validarCampos()
    {
       if (etTelefone.getText().equals("")) {
           Toast.makeText(this,"Campo telefone invalido", Toast.LENGTH_LONG).show();
           return false;
       }
       else
        return true;
    }


    public void onClick_Avancar(View v)
    {
        if (validarCampos()) {
            if (util.verificaInternet(getApplicationContext()))
                new aguardarTelefone().execute();

            else
                Toast.makeText(this, "Verifique sua conexão com a internet", Toast.LENGTH_LONG).show();
        }

    }

    public boolean SalvarTelefone() throws  InterruptedException{
        Usuario objUsuario = new Usuario();
        try
        {
            objUsuario.setTelefone(etTelefone.getText().toString());
            objUsuario.setCodigoVerificardor(strCodigo.toString());
            objUsuario.salvar(this.getApplicationContext());
            return true;

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
            resposta[0] =  HttpConnection.getSetDataWeb(getString(R.string.padrao_telefone), "send-json", data);
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
        sms.enviarSms(CadTelefone.this,numero,mensagem);
        return true;
    }

    public class aguardarTelefone extends AsyncTask <Void, Integer,Void>{
        boolean criou;
        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                synchronized (this)
                {
                    criou= SalvarTelefone();
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(CadTelefone.this);

            progressDialog = ProgressDialog.show(CadTelefone.this,"Carregando...",
                    "Estamos validando suas informações, por favor aguarde...", false, false);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if(criou == true)
            {
                objConfig = new Configuracoes();
                objConfig.carregar(CadTelefone.this);
                objConfig.atualizarStatus(CadTelefone.this,3);
                startActivity(new Intent(CadTelefone.this, ValidarTelefone.class));

            }else
            {

            }
        }
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
