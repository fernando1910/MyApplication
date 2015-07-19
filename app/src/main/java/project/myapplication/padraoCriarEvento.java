package project.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class padraoCriarEvento extends ActionBarActivity {

    DateFormat formatDate  = DateFormat.getDateInstance();
    DateFormat formatHour = DateFormat.getTimeInstance(DateFormat.SHORT);
    Calendar calendar = Calendar.getInstance();
    TextView tvData, tvHora;
    ImageButton btData, ibTimePicker;
    EditText etTitulo, etDescricao, etEndereco;
    RadioGroup rgStatusEvento;
    clsUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_criar_evento);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvData = (TextView)findViewById(R.id.tvData);
        tvHora = (TextView)findViewById(R.id.tvHora);
        btData = (ImageButton)findViewById(R.id.btDataPicker);
        etTitulo = (EditText)findViewById(R.id.etTitulo);
        etDescricao = (EditText)findViewById(R.id.etDescricao);
        etEndereco = (EditText)findViewById(R.id.etEndereco);
        rgStatusEvento = (RadioGroup)findViewById(R.id.rgStatusEvento);
        ibTimePicker = (ImageButton)findViewById(R.id.ibTimePicker);
        atualizarData();
        atualizarHora();
        //Button btDataPicker = (Button)findViewById(R.id.btDataPicker);
        util = new clsUtil();
        btData.setImageDrawable(util.retornarIcone(getResources().getDrawable(R.drawable.ic_calendar1),getResources()));
        ibTimePicker.setImageDrawable(util.retornarIcone(getResources().getDrawable(R.drawable.ic_clock), getResources()));

    }

    public void onClickCriarEvento(View v)
    {
        if(ValidarCampos())
        {
            if(SalvarEvento()) {
                //clsUtil util = new clsUtil();
                //util.AtualizarStatus(getApplicationContext(), 3);

                Toast.makeText(this, "O evento foi criado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, padraoMenu.class));
            }
            else
            {
                Toast.makeText(this, "Melou", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean SalvarEvento()
    {
        boolean fg_criou_evento = false;
        clsEvento objEvento = new clsEvento();
        try {
            if (ValidarCampos()) {
                objEvento.setTituloEvento(etTitulo.getText().toString());
                objEvento.setDescricao(etDescricao.getText().toString());
                objEvento.setEndereco(etEndereco.getText().toString());
                objEvento.setEventoPrivado(rgStatusEvento.getCheckedRadioButtonId());

            }
            objEvento.gerarEventoJSON(objEvento);

            objEvento.InserirEvento(this.getApplicationContext(), objEvento);
            fg_criou_evento = true;
            return true;
            }catch (Exception e)
            {
                Toast.makeText(this, "Erro: Evento não foi criado", Toast.LENGTH_SHORT).show();
            }
        return fg_criou_evento;
    }

    public boolean ValidarCampos()
    {
        if(etTitulo.getText().length()==0)
        {
            Toast.makeText(this, "Necessário informar um Titulo", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(etEndereco.getText().length()==0)
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
}
