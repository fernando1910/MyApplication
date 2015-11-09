package project.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import domain.Configuracoes;
import domain.Evento;
import domain.Usuario;
import domain.Util;


public class CadEvento extends AppCompatActivity {
    //region Variaveis
    private String TAG = "LOG";
    private int codigoEvento;
    private int tipoOperacao = 0; // 1 inclusão, 2 alteração
    private DateFormat formatDate = DateFormat.getDateInstance();
    private DateFormat formatHour = DateFormat.getTimeInstance(DateFormat.SHORT);
    private Calendar calendar = Calendar.getInstance();
    private TextView tvData, tvHora, tvEndereco;
    private ImageButton btData, ibTimePicker, ibEndereco, ibFotoCapa;
    private EditText etTitulo, etDescricao;
    private RadioGroup rgStatusEvento;
    private Util util;
    private RadioButton rbPrivate;
    private double nr_latitude, nr_longitude;
    private Configuracoes objConf;
    private Uri imgEvento;
    private Bitmap imgRetorno;
    private boolean fg_criou = true, fg_mudanca;
    private ProgressDialog progressDialog;
    private Evento objEvento, objEventoBkp;
    private Usuario objUsuario;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_criar_evento);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region Vincular variaveis com interface
        tvData = (TextView) findViewById(R.id.tvData);
        tvHora = (TextView) findViewById(R.id.tvHora);
        btData = (ImageButton) findViewById(R.id.btDataPicker);
        etTitulo = (EditText) findViewById(R.id.etTitulo);
        etDescricao = (EditText) findViewById(R.id.etDescricao);
        tvEndereco = (TextView) findViewById(R.id.tvEndereco);
        rgStatusEvento = (RadioGroup) findViewById(R.id.rgStatusEvento);
        ibTimePicker = (ImageButton) findViewById(R.id.ibTimePicker);
        rbPrivate = (RadioButton) findViewById(R.id.rbPrivate);
        ibEndereco = (ImageButton) findViewById(R.id.ibEndereco);
        ibFotoCapa = (ImageButton) findViewById(R.id.ibFotoCapa);
        //endregion

        atualizarData();
        atualizarHora();

        util = new Util();
        objConf = new Configuracoes();
        objConf.carregar(this);
        objEvento = new Evento();
        objUsuario = new Usuario();
        objUsuario.carregar(this);


        btData.setImageDrawable(util.retornarIcone(getResources().getDrawable(R.drawable.ic_calendar1), getResources()));
        ibTimePicker.setImageDrawable(util.retornarIcone(getResources().getDrawable(R.drawable.ic_clock), getResources()));
        ibEndereco.setImageDrawable(util.retornarIcone(getResources().getDrawable(R.drawable.ic_localizacao), getResources()));

        ibFotoCapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFoto();
            }
        });

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        ibFotoCapa.getLayoutParams().width = width;
        ibFotoCapa.getLayoutParams().height = (int) Math.ceil(height / 2.5);

        Bundle parameters = getIntent().getExtras();
        if (parameters != null) {
            codigoEvento = parameters.getInt("codigoEvento");
            final String url = getString(R.string.caminho_foto_capa_evento) + String.valueOf(codigoEvento) + ".png";
            Picasso.with(ibFotoCapa.getContext()).load(url).placeholder(R.drawable.ic_placeholder_evento).into(ibFotoCapa);
            tipoOperacao = 2;
            new carregarEvento().execute();
        } else {
            tipoOperacao = 1;
        }
    }

    public void carregarControles() {
        if (objEvento != null) {
            etTitulo.setText(objEvento.getTituloEvento());
            etDescricao.setText(objEvento.getDescricao());
            tvEndereco.setText(objEvento.getEndereco());
            tvData.setText(formatDate.format(objEvento.getDataEvento()));
            tvHora.setText(formatHour.format(objEvento.getDataEvento()));
            calendar.setTime(objEvento.getDataEvento());
        } else {
            Toast.makeText(CadEvento.this, "Falha ao carregar evento", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //region Validações de Imagens
    public void selecionarFoto() {
        final CharSequence[] options = {"Tirar foto", "Escolher da Galeria", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CadEvento.this);
        builder.setTitle("Adcionar Foto");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Tirar foto")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File diretorio = new File(Environment.getExternalStorageDirectory() + "/"
                            + getString(R.string.app_name));

                    diretorio = new File(diretorio.getPath() + "/Evento");

                    imgEvento = Uri.fromFile(new File(diretorio, "0.png"));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imgEvento);

                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, 2);
                    } catch (ActivityNotFoundException e) {

                    }
                } else if (options[item].equals("Escolher da Galeria")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 3);
                } else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    public void cortarFoto(Uri selectedImage) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setDataAndType(selectedImage, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1.5);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 800);
        cropIntent.putExtra("outputY", 500);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, 4);
    }

    //endregion

    //region Validações de data e hora
    public void atualizarData() {
        tvData.setText(formatDate.format(calendar.getTime()));
    }

    public void atualizarHora() {
        tvHora.setText(formatHour.format(calendar.getTime()));
    }

    public void setDate() {
        new DatePickerDialog(CadEvento.this, d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setHour() {
        new TimePickerDialog(CadEvento.this, timePickerDialog, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    TimePickerDialog.OnTimeSetListener timePickerDialog = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            atualizarHora();

        }
    };

    public void onClickMostrarDataPicker(View v) {
        setDate();
    }

    public void onClickShowTimerPicker(View v) {
        setHour();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            atualizarData();

        }
    };

    //endregion

    //region Métodos do Android
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_padrao_criar_evento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            this.finish();
            startActivity(new Intent(this, MenuPrincipalNovo.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(this, MenuPrincipalNovo.class));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String endereco;
                nr_latitude = data.getDoubleExtra("latitude", 0);
                nr_longitude = data.getDoubleExtra("longitude", 0);
                endereco = data.getStringExtra("endereco");
                tvEndereco.setText(endereco);

            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                cortarFoto(imgEvento);

            } else if (requestCode == 3) {
                imgEvento = data.getData();
                cortarFoto(imgEvento);
            } else if (requestCode == 4) {
                Bundle extras = data.getExtras();
                imgRetorno = extras.getParcelable("data");

                ibFotoCapa.setImageBitmap(imgRetorno);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //endregion

    public void chamarMapa(View view) {
        try {
            Bundle parameters = new Bundle();
            Intent intent = new Intent(getApplicationContext(), PesquisarEndereco.class);
            startActivityForResult(intent, 1, parameters);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //region validações criação de evento

    public void criarEventoCalendarioAndroid() {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", calendar.getTimeInMillis());
        intent.putExtra("allDay", false);
        //intent.putExtra("rrule", "FREQ=DAILY");
        intent.putExtra("endTime", calendar.getTimeInMillis() + 60 * 60 * 1000);
        intent.putExtra("title", etTitulo.getText().toString());
        startActivity(intent);
    }

    public class salvarEvento extends AsyncTask<Void, Integer, Void> {
        String erro;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CadEvento.this);

            progressDialog = ProgressDialog.show(CadEvento.this, "Carregando...",
                    "Compartilhando seu evento, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                synchronized (this) {
                    salvarEvento();
                }
            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
                fg_criou = false;
                erro = e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (fg_criou) {
                if (tipoOperacao == 1) {
                    if (objConf.getPermiteAlarme() == 1) {
                        criarEventoCalendarioAndroid();
                    }
                }else {
                    if (!fg_mudanca)
                        Toast.makeText(CadEvento.this, "Não houve alteração", Toast.LENGTH_SHORT).show();
                }
                CadEvento.this.finish();
                Intent intent = new Intent(CadEvento.this, VisualizarEvento.class);
                intent.putExtra("codigoEvento", codigoEvento);
                startActivity(intent);
            } else {
                Toast.makeText(CadEvento.this, erro, Toast.LENGTH_SHORT).show();

            }

        }
    }

    public void onClickCriarEvento(View v) {
        if (util.verificaInternet(this)) {
            if (ValidarCampos()) {
                new salvarEvento().execute();
            }
        } else {
            Toast.makeText(CadEvento.this, R.string.sem_internet, Toast.LENGTH_SHORT).show();
        }
    }

    public void salvarEvento() {
        try {
            descarregarControles();
            String mMensagem = "";
            if (tipoOperacao == 2)
                mMensagem = objEvento.verificarMudancaEvento(objEvento, objEventoBkp, objUsuario.getNome());

            if (!mMensagem.equals("") || tipoOperacao == 1) {
                fg_mudanca = true;
                codigoEvento = objEvento.salvarEventoOnline(this, tipoOperacao, mMensagem);
            }

        } catch (Exception e) {
            fg_criou = false;
        }
    }

    public void descarregarControles() {
        objEvento.setTituloEvento(etTitulo.getText().toString());
        objEvento.setDescricao(etDescricao.getText().toString());
        objEvento.setEndereco(tvEndereco.getText().toString());
        if (rbPrivate.isChecked())
            objEvento.setEventoPrivado(1);
        else
            objEvento.setEventoPrivado(0);
        objEvento.setCodigoUsuarioInclusao(objUsuario.getCodigoUsuario());
        objEvento.setDataEvento(calendar.getTime());

        objEvento.setLatitude(nr_latitude);
        objEvento.setLongitude(nr_longitude);

        if (imgEvento != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imgRetorno.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            //objEvento.setImagemFotoCapa(byteArray);
            String imagemServidor = Base64.encodeToString(byteArray, 0);
            objEvento.setFotoCapa(imagemServidor);
        }
        if (tipoOperacao == 1)
            objEvento.setDataInclusao(new Date());
        else
            objEvento.setDataAlteracao(new Date());
    }

    public boolean ValidarCampos() {
        boolean fg = true;
        int valida = new Date().compareTo(calendar.getTime());

        if (etTitulo.getText().length() == 0) {
            Toast.makeText(this, "Necessário informar um Titulo", Toast.LENGTH_SHORT).show();
            fg = false;
        }
        if (tvEndereco.getText().length() == 0) {
            Toast.makeText(this, "Necessário informar o Endereço", Toast.LENGTH_SHORT).show();
            fg = false;
        }
        if (rgStatusEvento.getCheckedRadioButtonId() == 0) {
            Toast.makeText(this, "Necessário informar se é Público ou Privado", Toast.LENGTH_SHORT).show();
            fg = false;
        }
        if (valida > -1) {
            Toast.makeText(this, "A data do evento precisa ser maior que o dia de hoje", Toast.LENGTH_SHORT).show();
            fg = false;
        }
        return fg;
    }
    //endregion

    private class carregarEvento extends AsyncTask<Void,Integer,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CadEvento.this);
            progressDialog = ProgressDialog.show(CadEvento.this, "Carregando...",
                    "Por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this){
                try{
                    objEvento.carregarOnline(codigoEvento, getApplicationContext(), objUsuario.getCodigoUsuario());
                    objEventoBkp = objEvento.gerarBackup();
                }catch (Exception ex){
                    Log.i(TAG, ex.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            carregarControles();
            progressDialog.dismiss();
        }
    }

}
