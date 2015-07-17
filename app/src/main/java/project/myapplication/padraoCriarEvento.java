package project.myapplication;

import android.app.DatePickerDialog;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;


public class padraoCriarEvento extends ActionBarActivity {

    DateFormat format  = DateFormat.getDateInstance();
    Calendar calendar = Calendar.getInstance();
    TextView tvData;
    ImageButton btData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_criar_evento);
        tvData = (TextView)findViewById(R.id.tvData);
        btData = (ImageButton)findViewById(R.id.btDataPicker);
        atualizarData();
        //Button btDataPicker = (Button)findViewById(R.id.btDataPicker);

    }

    public void atualizarData()
    {

        tvData.setText(format.format(calendar.getTime()));
    }


    public void setDate()
    {
        new DatePickerDialog(padraoCriarEvento.this,d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void onClickMostrarDataPicker(View v)
    {
        //Toast.makeText(this, "Example action.", Toast.LENGTH_SHORT).show();
        setDate();
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

        return super.onOptionsItemSelected(item);
    }
}
