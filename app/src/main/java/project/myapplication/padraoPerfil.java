package project.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;


public class padraoPerfil extends ActionBarActivity {
    EditText etNome;
    ImageButton ibPerfil;
    RoundImage roundedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_perfil);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ibPerfil = (ImageButton)findViewById(R.id.ibPerfil);
        etNome = (EditText)findViewById(R.id.etNome);

        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.image);
        roundedImage  = new RoundImage(bm);
        ibPerfil.setImageDrawable(roundedImage);

        String result;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpPost httpPost = new HttpPost("http://www.fiesta1.hol.es/CadUsuario.php");


                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();

                InputStream inputStream = httpEntity.getContent();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    inputStream.close();
                    result = stringBuilder.toString();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            catch (NetworkOnMainThreadException ex)
            {
                result = ex.getMessage();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        /*

        clsUsuario objUsuario;
        UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
        objUsuario = usuarioDAO.getUsuario(1);
        String caminhoFoto = objUsuario.getCaminhoFoto();

        etNome.setText(objUsuario.getNome());

        try{
            Bitmap bitmap = BitmapFactory.decodeByteArray(objUsuario.getImagemPerfil(),0,objUsuario.getImagemPerfil().length);
            roundedImage = new RoundImage(bitmap);
            ibPerfil =(ImageButton)findViewById(R.id.ibPerfil);
            ibPerfil.setImageDrawable(roundedImage);

            File file = new File(caminhoFoto);

            /*if(file.exists())
            {
                //Bitmap image = BitmapFactory.decodeFile(file.getPath());
                //ibPerfil.setImageBitmap(image);
                //ibPerfil.setImageURI(uri);
            }



        }catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        */

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

}
