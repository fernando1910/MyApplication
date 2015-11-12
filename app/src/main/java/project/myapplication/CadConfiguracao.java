package project.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import domain.Configuracoes;
import domain.Usuario;

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

            if (objConfig.getBuscarFotosOnline() == 1)
                ckBuscarFotosOnline.setChecked(true);

            try {
                    // TA BOM PROBLEMA
                switch (objConfig.getAlcanceKm()) {
                    case 1:
                        spKm.setSelection(0);
                        break;
                    case 2:
                        spKm.setSelection(1);
                        break;
                    case 3:
                        spKm.setSelection(2);
                        break;
                    case 4:
                        spKm.setSelection(3);
                        break;
                    case 5:
                        spKm.setSelection(4);
                        break;
                    case 10:
                        spKm.setSelection(5);
                        break;
                    case 20:
                        spKm.setSelection(6);
                        break;
                    default:
                        spKm.setSelection(4);
                        break;
                }
            }catch (Exception ex){
                Toast.makeText(CadConfiguracao.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }



            // endregion

        } catch (Exception e) {
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

        if (id == android.R.id.home) {
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

    public void SalvarConfiguracoes() {

        if (ckPermitePush.isChecked())
            objConfig.setPermitePush(1);
        else
            objConfig.setPermitePush(0);

        if (ckPermiteAlarme.isChecked())
            objConfig.setPermiteAlarme(1);
        else
            objConfig.setPermiteAlarme(0);

        if (ckNotificaComentario.isChecked())
            objConfig.setNotificaComentario(1);
        else
            objConfig.setNotificaComentario(0);

        if (ckNotificaMudanca.isChecked())
            objConfig.setNotificaMudanca(1);
        else
            objConfig.setNotificaMudanca(0);

        if (ckTelefoneVisivel.isChecked())
            objConfig.setTelefoneVisivel(1);
        else
            objConfig.setTelefoneVisivel(0);

        if (ckBuscarFotosOnline.isChecked())
            objConfig.setBuscarFotosOnline(1);
        else
            objConfig.setBuscarFotosOnline(0);

        switch (spKm.getSelectedItemPosition()) {
            case 0:
                objConfig.setAlcanceKm(1);
                break;
            case 1:
                objConfig.setAlcanceKm(2);
                break;
            case 2:
                objConfig.setAlcanceKm(3);
                break;
            case 3:
                objConfig.setAlcanceKm(4);
                break;
            case 4:
                objConfig.setAlcanceKm(5);
                break;
            case 5:
                objConfig.setAlcanceKm(10);
                break;
            case 6:
                objConfig.setAlcanceKm(20);
                break;
            default:
                spKm.setSelection(4);
                break;
        }

        objConfig.atualizar(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Usuario objUsuario = new Usuario();
                objUsuario.carregar(CadConfiguracao.this);
                try {
                    objConfig.atualizarTelefoneVisivel(CadConfiguracao.this, objUsuario.getCodigoUsuario(), objConfig.getTelefoneVisivel());
                } catch (Exception e) {
                    Log.i("erro", e.getMessage());
                }
            }
        }).start();

    }
}
