package project.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class padraoPerfil extends ActionBarActivity {
    EditText etNome;
    ImageButton ibPerfil, ibEditarNome, ibSalvarNome;
    RoundImage roundedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ibPerfil = (ImageButton)findViewById(R.id.ibPerfil);
        ibEditarNome = (ImageButton)findViewById(R.id.ibEditarNome);
        ibSalvarNome = (ImageButton)findViewById(R.id.ibSalvarNome);
        etNome = (EditText)findViewById(R.id.etNome);
        etNome.setEnabled(false);

        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.image);
        roundedImage  = new RoundImage(bm);
        ibPerfil.setImageDrawable(roundedImage);

        clsUsuario objUsuario;
        UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
        objUsuario = usuarioDAO.getUsuario();

        etNome.setText(objUsuario.getNome());
        clsUtil objUtil = new clsUtil();
        ibEditarNome.setImageDrawable(objUtil.retornarIcone(getResources().getDrawable(R.drawable.ic_edit), getResources()));
        ibSalvarNome.setImageDrawable(objUtil.retornarIcone(getResources().getDrawable(R.drawable.ic_salvar_perfil), getResources()));

        if(objUsuario.getImagemPerfil() != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(objUsuario.getImagemPerfil(), 0, objUsuario.getImagemPerfil().length);
                roundedImage = new RoundImage(bitmap);
                ibPerfil = (ImageButton) findViewById(R.id.ibPerfil);
                ibPerfil.setImageDrawable(roundedImage);

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            this.finish();
            startActivity(new Intent(this,padraoConfiguracao.class));
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

    public void onClickEditarNome(View v)
    {
        etNome.setEnabled(true);

    }

    public void onClickSalvarNome(View v)
    {
        etNome.setEnabled(false);
        clsUsuario objUsuario = new clsUsuario();
        objUsuario.AtualizarNome(this, String.valueOf(etNome.getText()));

    }

}
