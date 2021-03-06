package project.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

import domain.Configuracoes;
import domain.Usuario;
import domain.Util;
import extras.RoundImage;
import services.RegistrationIntentService;


public class CadPerfil extends Activity {
    private final String TAG = "ERRO";
    private EditText etNome;
    private ImageButton ibPerfil;
    private RoundImage roundedImage;
    private Uri imgPerfil;
    private Button btAvancar;
    private boolean fg_criou;
    private ProgressDialog progressDialog;
    private Configuracoes objConfig ;
    private Util util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_login);

        util = new Util();
        util.validarTela(this,4);

        objConfig = new Configuracoes();
        objConfig.carregar(this);

        etNome = (EditText) findViewById(R.id.etNome);
        btAvancar = (Button) findViewById(R.id.btAvancar);


        ibPerfil = (ImageButton) findViewById(R.id.btnSelectPhoto);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        roundedImage = new RoundImage(bm);
        ibPerfil.setImageDrawable(roundedImage);

        ibPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImagem();
            }
        });
    }

    public void onClick_Avancar(View v) {

        if (ValidarCampos()) {
            new criarPerfil().execute();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_padrao_login, menu);
        return true;
    }

    private void SelectImagem() {
        try {
            final CharSequence[] options = {"Tirar foto", "Escolher da Galeria", "Cancelar"};
            AlertDialog.Builder builder = new AlertDialog.Builder(CadPerfil.this);
            builder.setTitle("Adcionar Foto");

            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (options[item].equals("Tirar foto")) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File diretorio = new File(Environment.getExternalStorageDirectory() + "/"
                                + getString(R.string.app_name));

                        diretorio = new File(diretorio.getPath() + "/Perfil");
                        File mFile = new File(diretorio, "0.png");
                        if (mFile.exists())
                            mFile.delete();

                        imgPerfil = Uri.fromFile(mFile);

                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imgPerfil);

                        try {
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, 1);
                        } catch (ActivityNotFoundException e) {
                            Log.i(TAG, e.getMessage());
                        }
                    } else if (options[item].equals("Escolher da Galeria")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } else if (options[item].equals("Cancelar")) {
                        dialog.dismiss();
                    }
                }
            });

            builder.show();
        }catch (Exception ex){
            Log.i(TAG,ex.getMessage());
        }
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
            } else if (requestCode == 3) {
                Bundle extras = data.getExtras();
                Bitmap thumbnail = extras.getParcelable("data");
                roundedImage = new RoundImage(thumbnail);
                ibPerfil.setImageDrawable(roundedImage);
            }
        }
    }

    public void cortarFoto(Uri selectedImage) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(selectedImage, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 640);
        cropIntent.putExtra("outputY", 640);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, 3);
    }

    public boolean ValidarCampos() {
        if (etNome.getText().length() == 0) {
            Toast.makeText(this, "Seu nome não foi informado", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void preencherObjeto(Usuario objUsuario) {

        try {
            if (imgPerfil != null) {
                Bitmap pic = roundedImage.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                pic.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                objUsuario.setImagemPerfil(byteArrayOutputStream.toByteArray());
                objUsuario.setFotoPerfilServidor(Base64.encodeToString(objUsuario.getImagemPerfil(), 0));

            }
            objUsuario.setNome(etNome.getText().toString());

        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }


    public class criarPerfil extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CadPerfil.this);
            progressDialog = ProgressDialog.show(CadPerfil.this, "Carregando...",
                    "Estamos validando seu perfil, por favor aguarde...", false, false);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this) {
                try{
                    Usuario objUsuario = new Usuario();
                    objUsuario.carregar(CadPerfil.this);
                    preencherObjeto(objUsuario);
                    fg_criou = objUsuario.salvarPerfilOnline(CadPerfil.this);
                }catch (Exception e){
                    fg_criou = false;
                    progressDialog.dismiss();
                    Toast.makeText(CadPerfil.this, "Lamentamos houve um erro", Toast.LENGTH_SHORT).show();

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            if (fg_criou) {
                Intent intent = new Intent(CadPerfil.this, RegistrationIntentService.class);
                CadPerfil.this.startService(intent);
                objConfig.atualizarStatus(CadPerfil.this, 5);
                startActivity(new Intent(CadPerfil.this, MenuPrincipalNovo.class));
                CadPerfil.this.finish();
            }
            else{
                Toast.makeText(getApplication(), "Lamentamos ocorreu um erro interno", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
