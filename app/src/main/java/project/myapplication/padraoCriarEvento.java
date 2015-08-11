package project.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
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

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;


public class padraoCriarEvento extends ActionBarActivity {
    //region Variaveis
    private DateFormat formatDate  = DateFormat.getDateInstance();
    private DateFormat formatHour = DateFormat.getTimeInstance(DateFormat.SHORT);
    private Calendar calendar = Calendar.getInstance();
    private TextView tvData, tvHora, tvEndereco;
    private ImageButton btData, ibTimePicker, ibEndereco, ibFotoCapa;
    private EditText etTitulo, etDescricao;
    private RadioGroup rgStatusEvento;
    private clsUtil util;
    private RadioButton rbPublic,rbPrivate;
    private double nr_latitude, nr_longitude;
    private clsConfiguracoes objConf;
    private Uri imgEvento;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_criar_evento);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    //region Vincular variaveis com interface
        tvData = (TextView)findViewById(R.id.tvData);
        tvHora = (TextView)findViewById(R.id.tvHora);
        btData = (ImageButton)findViewById(R.id.btDataPicker);
        etTitulo = (EditText)findViewById(R.id.etTitulo);
        etDescricao = (EditText)findViewById(R.id.etDescricao);
        tvEndereco = (TextView)findViewById(R.id.tvEndereco);
        rgStatusEvento = (RadioGroup)findViewById(R.id.rgStatusEvento);
        ibTimePicker = (ImageButton)findViewById(R.id.ibTimePicker);
        rbPublic = (RadioButton)findViewById(R.id.rbPublic);
        rbPrivate = (RadioButton)findViewById(R.id.rbPrivate);
        ibEndereco = (ImageButton)findViewById(R.id.ibEndereco);
        ibFotoCapa = (ImageButton)findViewById(R.id.ibFotoCapa);
    //endregion

        atualizarData();
        atualizarHora();

        util = new clsUtil();
        objConf = new clsConfiguracoes();

        btData.setImageDrawable(util.retornarIcone(getResources().getDrawable(R.drawable.ic_calendar1),getResources()));
        ibTimePicker.setImageDrawable(util.retornarIcone(getResources().getDrawable(R.drawable.ic_clock), getResources()));
        ibEndereco.setImageDrawable(util.retornarIcone(getResources().getDrawable(R.drawable.ic_localizacao), getResources()));

        ibFotoCapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFoto();
            }
        });
    }

    public void selecionarFoto()
    {
        final CharSequence[] options = {"Tirar foto", "Escolher da Galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(padraoCriarEvento.this);
        builder.setTitle("Adcionar Foto");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Tirar foto"))
                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File diretorio = new File(Environment.getExternalStorageDirectory() + "/"
                            + getString(R.string.app_name));

                    diretorio = new File(diretorio.getPath() + "/Evento");

                    imgEvento = Uri.fromFile(new File(diretorio,
                            "img_evento_" + String.valueOf(util.RetornaDataHoraMinuto() + ".jpg")));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imgEvento);

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

    public void onClickCriarEvento(View v)
    {
        if(ValidarCampos())
        {
            if(SalvarEvento()) {
                ConfiguracoesDAO config_dao = new ConfiguracoesDAO(this.getApplicationContext());

                Toast.makeText(this, "O evento foi criado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, padraoMenu.class));
                objConf = config_dao.Carregar();
                if(objConf.getPermiteAlarme() == 1)
                {
                    criarEventoCalendarioAndroid();
                }
                this.finish();
            }
            else
            {
                Toast.makeText(this, "Algo deu errado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean SalvarEvento()
    {
        clsEvento objEvento = new clsEvento();
        try {
            if (ValidarCampos()) {
                clsUsuario objUsuario = new clsUsuario();
                objUsuario = objUsuario.SelecionarUsuario(this);

                objEvento.setTituloEvento(etTitulo.getText().toString());
                objEvento.setDescricao(etDescricao.getText().toString());
                objEvento.setEndereco(tvEndereco.getText().toString());
                if (rbPrivate.isChecked())
                    objEvento.setEventoPrivado(1);
                else
                    objEvento.setEventoPrivado(0);
                objEvento.setCodigoUsarioInclusao(objUsuario.getCodigoUsuario());

                objEvento.setDataEvento(calendar.getTime());
                objEvento.setLatitude(nr_latitude);
                objEvento.setLongitude(nr_longitude);

            }
            objEvento.gerarEventoJSON(objEvento, getString(R.string.padrao_evento));

            objEvento.InserirEvento(this.getApplicationContext(), objEvento);
            return true;
            }catch (Exception e)
            {
                Toast.makeText(this, "Erro: Evento não foi criado", Toast.LENGTH_SHORT).show();
                return false;
            }
    }

    public boolean ValidarCampos()
    {
        if(etTitulo.getText().length()==0)
        {
            Toast.makeText(this, "Necessário informar um Titulo", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(tvEndereco.getText().length()==0)
        {
            Toast.makeText(this, "Necessário informar o Endereço", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(rgStatusEvento.getCheckedRadioButtonId()==0)
        {
            Toast.makeText(this, "Necessário informar se é Público ou Privado", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }

    }
    public void atualizarData()
    {

        tvData.setText(formatDate.format(calendar.getTime()));
    }

    public void atualizarHora()
    {
        tvHora.setText(formatHour.format(calendar.getTime()));
    }


    public void setDate()
    {
        new DatePickerDialog(padraoCriarEvento.this,d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void setHour()
    {
        new TimePickerDialog(padraoCriarEvento.this,timePickerDialog,calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
    }

    TimePickerDialog.OnTimeSetListener timePickerDialog = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            atualizarHora();

        }
    };

    public void onClickMostrarDataPicker(View v)
    {
        setDate();
    }

    public void onClickShowTimerPicker(View v)
    {
        setHour();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()
    {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            atualizarData();

        }
    };
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_padrao_criar_evento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home)
        {
            this.finish();
            startActivity(new Intent(this,padraoMeusEventos.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(this,padraoMeusEventos.class));

    }

    public void chamarMapa(View view)
    {
        try {
            Bundle parameters = new Bundle();
            Intent intent = new Intent(getApplicationContext(), padraoPesquisarEndereco.class);
            startActivityForResult(intent, 1, parameters);
        }catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                String endereco;
                nr_latitude= data.getDoubleExtra("latitude",0);
                nr_longitude = data.getDoubleExtra("longitude",0);
                endereco = data.getStringExtra("endereco");
                tvEndereco.setText(endereco);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void criarEventoCalendarioAndroid()
    {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", calendar.getTimeInMillis());
        intent.putExtra("allDay", false);
        //intent.putExtra("rrule", "FREQ=DAILY");
        intent.putExtra("endTime", calendar.getTimeInMillis() + 60 * 60 *1000);
        intent.putExtra("title", etTitulo.getText().toString());
        startActivity(intent);
    }
}
