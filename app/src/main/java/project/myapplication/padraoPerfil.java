package project.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


public class padraoPerfil extends ActionBarActivity {
    ImageButton ibPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ibPerfil = (ImageButton)findViewById(R.id.ibPerfil);

        clsUsuario objUsuario;
        UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
        objUsuario = usuarioDAO.getUsuario(1);

        String teste = "";

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(this,padraoConfiguracao.class));

    }

}
