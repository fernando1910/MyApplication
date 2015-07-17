package project.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;


public class padraoLogin extends Activity {
    EditText etNome;
    EditText etTelefone;
    ImageButton b;
    RoundImage roundedImage;
    Uri imgPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_login);

        clsConfiguracoes objConfig;
        ConfiguracoesDAO config_dao = new ConfiguracoesDAO(this.getApplicationContext());
        objConfig = config_dao.Carregar();

        switch (objConfig.getStatusPerfil())
        {
            case 0:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case 1:
                startActivity(new Intent(this,padraoLogin.class));
                break;
            case 3:
                startActivity(new Intent(this,padraoMenu.class));
                break;
        }

        etNome = (EditText)findViewById(R.id.etNome);
        etTelefone = (EditText)findViewById(R.id.etTelefone);

        b=(ImageButton)findViewById(R.id.btnSelectPhoto);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.image);
        roundedImage  = new RoundImage(bm);
        b.setImageDrawable(roundedImage);

        //etTelefone = (EditText)findViewById(R.id.etTelefone);

        //telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //etTelefone.setText(telephonyManager.getLine1Number());
        //etTelefone.setText(t);

        b.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                selectImage();
            }

        });
    }

    public void onClick_Avancar(View v) {

        if(ValidarCampos())
        {
            if(SalvarUsuario()) {
                clsUtil util = new clsUtil();
                //util.AtualizarStatus(getApplicationContext(), 3);

                Toast.makeText(this, "Seu perfil foi criado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, padraoMenu.class));
            }
            else
            {
                Toast.makeText(this, "Melou", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_padrao_login, menu);
        return true;
    }

    private void selectImage() {

        final CharSequence[] options = {"Tirar foto", "Escolher da Galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(padraoLogin.this);
        builder.setTitle("Adcionar Foto");

        //builder.setIcon(android.R.drawable.ic_menu_camera);

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Tirar foto"))
                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File diretorio = new File(Environment.getExternalStorageDirectory() + "/"
                            + getString(R.string.app_name));

                        diretorio = new File(diretorio.getPath() + "/Perfil");
                    clsUtil util = new clsUtil();
                    imgPerfil = Uri.fromFile(new File(diretorio,
                            "img_perfil_" + String.valueOf(util.RetornaDataHoraMinuto() + ".jpg")));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imgPerfil);

                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, 1);
                    } catch (ActivityNotFoundException e) {
                        //Do nothing for now
                    }

                }

                else if (options[item].equals("Escolher da Galeria"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }

                else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();

                }
            }

        });

        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                cortarFoto(imgPerfil);

            } else if (requestCode == 2) {
                imgPerfil = data.getData();
                cortarFoto(imgPerfil);
            }
            else if (requestCode == 3)
            {
                Bundle extras = data.getExtras();
                Bitmap thumbnail = extras.getParcelable("data");

                //Log.w("path of image from gallery......******************.........", picturePath+"");

                roundedImage  = new RoundImage(thumbnail);
                b.setImageDrawable(roundedImage);

            }

        }

    }

    public void cortarFoto(Uri selectedImage)    {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        // indicate image type and Uri
        cropIntent.setDataAndType(selectedImage, "image/*");
        // set crop properties
        cropIntent.putExtra("crop", "true");
        // indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        // retrieve data on return
        cropIntent.putExtra("return-data", true);
        // start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 3);

    }

    public boolean ValidarCampos()    {
        if (etNome.getText().length() == 0)
        {
            Toast.makeText(this, "Seu nome não foi informado", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (etTelefone.getText().length() == 0) {
            Toast.makeText(this, "Seu telefone não foi informado", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean SalvarUsuario()
    {
        boolean fg_criou_usuario;

            clsUsuario objUsuario = new clsUsuario();
            try
            {
                if(imgPerfil != null) {
                    Bitmap pic = roundedImage.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    pic.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    objUsuario.setImagemPerfil(byteArrayOutputStream.toByteArray());
                    objUsuario.setCaminhoFoto(imgPerfil.getPath());
                }

                objUsuario.setNome(etNome.getText().toString());
                objUsuario.setTelefone(etTelefone.getText().toString());

                objUsuario.gerarUsuarioJSON(objUsuario);

                objUsuario.InserirUsuario(this.getApplicationContext(), objUsuario);
                fg_criou_usuario = true;
            }catch (Exception e)
            {

                fg_criou_usuario = false;
                Toast.makeText(this, "Erro: Perfil nao foi salvo", Toast.LENGTH_SHORT).show();
            }

        return fg_criou_usuario;
    }

}
