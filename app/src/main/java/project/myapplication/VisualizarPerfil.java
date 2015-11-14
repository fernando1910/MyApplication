package project.myapplication;

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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

import domain.Usuario;
import domain.Util;
import extras.RoundImage;


public class VisualizarPerfil extends AppCompatActivity {
    private EditText etNome;
    private ImageButton ibPerfil;
    private RoundImage roundedImage;
    private Uri imgPerfil;
    private Usuario objUsuario;
    private boolean fg_atualiza_nome;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ProgressDialog mProgressDialog;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_padrao_perfil);
            ActionBar mActionBar = getSupportActionBar();
            if (mActionBar != null)
                mActionBar.setDisplayHomeAsUpEnabled(true);

            ibPerfil = (ImageButton) findViewById(R.id.ibPerfil);
            ImageButton ibSalvarNome = (ImageButton) findViewById(R.id.ibSalvarNome);
            etNome = (EditText) findViewById(R.id.etNome);

            util = new Util();
            objUsuario = new Usuario();
            objUsuario.carregar(this);

            etNome.setText(objUsuario.getNome());

            ibSalvarNome.setImageDrawable(util.retornarIcone(getResources().getDrawable(R.drawable.ic_salvar_perfil), getResources()));

            Bitmap bitmap = null;
            if (!objUsuario.getCaminhoFoto().equals("")) {
                File mFile = new File(objUsuario.getCaminhoFoto());
                if (mFile.exists()) {
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath(),bmOptions);
                }
                else if (objUsuario.getImagemPerfil() != null)
                    bitmap = BitmapFactory.decodeByteArray(objUsuario.getImagemPerfil(), 0, objUsuario.getImagemPerfil().length);
            }

            if (bitmap == null)
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);

            roundedImage = new RoundImage(bitmap);
            ibPerfil = (ImageButton) findViewById(R.id.ibPerfil);
            ibPerfil.setImageDrawable(roundedImage);



            ibPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (util.verificaInternet(VisualizarPerfil.this))
                        alterarFoto();
                    else
                        Toast.makeText(VisualizarPerfil.this, R.string.sem_internet, Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception ex){
            Toast.makeText(VisualizarPerfil.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
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
        this.finish();
    }

    public void onClickSalvarNome(View v) {
        if (util.verificaInternet(this)) {
            objUsuario.setNome(String.valueOf(etNome.getText()));
            new atualizarNome().execute();
        }
        else
        {
            Toast.makeText(VisualizarPerfil.this, R.string.sem_internet, Toast.LENGTH_SHORT).show();
        }
    }

    public void alterarFoto(){
        final CharSequence[] options = {"Tirar foto", "Escolher da Galeria", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(VisualizarPerfil.this);
        builder.setTitle("Adcionar Foto");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Tirar foto")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File diretorio = new File(Environment.getExternalStorageDirectory() + "/"
                            + getString(R.string.app_name));

                    diretorio = new File(diretorio.getPath() + "/Perfil");
                    imgPerfil = Uri.fromFile(new File(diretorio,"0.png"));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imgPerfil);

                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, 1);
                    } catch (ActivityNotFoundException e) {

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
                Bitmap mImamge = roundedImage.getBitmap();
                byteArrayOutputStream = new ByteArrayOutputStream();
                mImamge.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                new atualizarFoto().execute();
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


    private class atualizarNome extends AsyncTask<Void,Integer,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try {
                fg_atualiza_nome = objUsuario.atualizarNome(VisualizarPerfil.this);
            }catch (Exception e){
                fg_atualiza_nome = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (fg_atualiza_nome)
                Toast.makeText(VisualizarPerfil.this, "Atualizado", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(VisualizarPerfil.this, "Erro", Toast.LENGTH_SHORT).show();
        }
    }

    private class atualizarFoto extends AsyncTask<Void,Integer,Void>{

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(VisualizarPerfil.this);
            mProgressDialog = ProgressDialog.show(VisualizarPerfil.this,"Carregando", "Aguarde", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String mCodigoUsuario = String.valueOf(objUsuario.getCodigoUsuario());
                util.salvarFoto(byteArrayOutputStream.toByteArray(),"Perfil", VisualizarPerfil.this,mCodigoUsuario);
                objUsuario.atualizarFoto(VisualizarPerfil.this, byteArrayOutputStream.toByteArray());
            }catch (Exception ex){
                mProgressDialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressDialog.dismiss();
            ibPerfil.setImageDrawable(roundedImage);
        }
    }

}
