package project.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class padraoLogin extends Activity {
    EditText etNome;
    ImageButton b;
    RoundImage roundedImage;
    Uri imgPerfil;
    Button btAvancar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_login);

        clsConfiguracoes objConfig;
        ConfiguracoesDAO config_dao = new ConfiguracoesDAO(this.getApplicationContext());
        objConfig = config_dao.Carregar();
        pegarParametro();

        switch (objConfig.getStatusPerfil())
        {
            case 0:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case 1:
                startActivity(new Intent(this,padraoBoasVindas.class));
                break;
            case 2:
                startActivity(new Intent(this,padraoCadTelefone.class));
                break;
            case 3:
                startActivity(new Intent(this,padraoValidarTelefone.class));
                break;
        }

        etNome = (EditText)findViewById(R.id.etNome);
        btAvancar = (Button)findViewById(R.id.btAvancar);


        b=(ImageButton)findViewById(R.id.btnSelectPhoto);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.image);
        roundedImage  = new RoundImage(bm);
        b.setImageDrawable(roundedImage);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImagem();
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

    private void SelectImagem() {

        final CharSequence[] options = {"Tirar foto", "Escolher da Galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(padraoLogin.this);
        builder.setTitle("Adcionar Foto");

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

    public int pegarParametro(){
        int parametro=0;
        int teste = 0;
        Intent intent = getIntent();

        if(intent != null )
           parametro = intent.getIntExtra("Parametro",teste);

        return parametro;
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
        else
        {
            return true;
        }
    }

    public boolean SalvarUsuario()
    {
        clsUsuario objUsuario = new clsUsuario();
        objUsuario.carregar(this);
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

            String usuario = objUsuario.gerarUsuarioJSON(objUsuario);
           /* usuario = Integer.parseInt(enviarUsuarioServidor(usuario));

            if (usuario == 0)
            {
                Toast.makeText(this, "Erro1:Telefone não foi Salvo", Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                objUsuario.setCodigoUsuario(usuario);
                objUsuario.salvar(this.getApplicationContext(), objUsuario);
                return true;
            } */

            objUsuario.atualizar(this);
            return true;

        }catch (Exception e)
        {
            Toast.makeText(this, "Erro: Perfil nao foi salvo", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public String enviarUsuarioServidor(final String data) throws InterruptedException {

        final String[] resposta = new String[1];
        try{
            Thread thread =  new Thread( new Runnable(){
            public void run(){
                resposta[0] =  project.myapplication.HttpConnection.getSetDataWeb(getString(R.string.padrao_atualiza_usuario), "send-json",data);

            }
        });
        thread.start();


            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return resposta[0];
    }

}
