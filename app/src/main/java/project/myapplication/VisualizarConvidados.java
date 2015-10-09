package project.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class VisualizarConvidados extends AppCompatActivity {
    private final String TAG = "LOG";
    private ListView mListView;
    private int codigoEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setContentView(R.layout.activity_visualizar_convidados);
            mListView = (ListView) findViewById(R.id.lvConvidados);
            Bundle parameters = getIntent().getExtras();
            if(parameters != null) {
                codigoEvento = parameters.getInt("codigoEvento");
            }

        }catch (Exception ex){
            Log.i(TAG, ex.getMessage());
            Toast.makeText(VisualizarConvidados.this, "Erro", Toast.LENGTH_SHORT).show();
        }
        finally {
            this.finish();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
