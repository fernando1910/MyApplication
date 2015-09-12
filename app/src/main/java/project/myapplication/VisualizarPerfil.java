package project.myapplication;

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
    private ImageButton ibPerfil, ibEditarNome, ibSalvarNome;
    private RoundImage roundedImage;
    private Uri imgPerfil;
    private Usuario objUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_padrao_perfil);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ibPerfil = (ImageButton) findViewById(R.id.ibPerfil);
            ibEditarNome = (ImageButton) findViewById(R.id.ibEditarNome);
            ibSalvarNome = (ImageButton) findViewById(R.id.ibSalvarNome);
            etNome = (EditText) findViewById(R.id.etNome);
            etNome.setEnabled(false);

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.image);
            roundedImage = new RoundImage(bm);
            ibPerfil.setImageDrawable(roundedImage);

            objUsuario = new Usuario();
            objUsuario.carregar(this);

            etNome.setText(objUsuario.getNome());
            Util objUtil = new Util();
            ibEditarNome.setImageDrawable(objUtil.retornarIcone(getResources().getDrawable(R.drawable.ic_edit), getResources()));
            ibSalvarNome.setImageDrawable(objUtil.retornarIcone(getResources().getDrawable(R.drawable.ic_salvar_perfil), getResources()));

            if (objUsuario.getImagemPerfil() != null) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(objUsuario.getImagemPerfil(), 0, objUsuario.getImagemPerfil().length);
                    roundedImage = new RoundImage(bitmap);
                    ibPerfil = (ImageButton) findViewById(R.id.ibPerfil);
                    ibPerfil.setImageDrawable(roundedImage);

                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            ibPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alterarFoto();
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

    public void onClickEditarNome(View v) {
        etNome.setEnabled(true);

    }

    public void onClickSalvarNome(View v) {
        etNome.setEnabled(false);
        objUsuario.atualizarNome(this, String.valueOf(etNome.getText()));

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
                    Util util = new Util();
                    imgPerfil = Uri.fromFile(new File(diretorio,
                            "img_perfil_" + String.valueOf(util.RetornaDataHoraMinuto() + ".jpg")));

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
                Bitmap pic = roundedImage.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                pic.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                objUsuario.atualizarFoto(this, byteArrayOutputStream.toByteArray());
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
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, 3);

    }

}
