package project.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import domain.Util;

public class PesquisarEvento extends AppCompatActivity implements LocationListener {
    private Util util;
    private GoogleMap mMap;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_eventos);

        setUpMapIfNeeded();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = supportMapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        util = new Util();


        if (util.verificaGPS(getApplicationContext())) {
            try {
                latLng = util.retornaLocalizacao(getApplicationContext(), this);
                addCircle();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("GPS Desligado! \nDeseja ligar o GPS ?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 1);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();

            Toast.makeText(this, "GPS desligado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_pesquisar_eventos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,CadConfiguracao.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        //super.onBackPressed();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        addCircle();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void addCircle() {

        Circle mCircle = mMap.addCircle(
                new CircleOptions()
                        .center(latLng)
                        .radius(1000)
                        .strokeColor(Color.parseColor("#66CCFF"))
                        .fillColor(Color.parseColor("#2066CCFF"))

                        .strokeWidth(2)

        );
    }
}
