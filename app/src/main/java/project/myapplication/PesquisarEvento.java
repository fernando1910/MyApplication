package project.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import domain.Configuracoes;
import domain.Evento;
import domain.Util;

public class PesquisarEvento extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    private Util util;
    private GoogleMap mMap;
    private LatLng latLng;
    private boolean mStatusGPS;
    private ProgressDialog mProgressDialog;
    private String mQuery;
    private Circle mapCircle;
    private Configuracoes objConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_eventos);


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        util = new Util();
        objConfig = new Configuracoes();
        objConfig.carregar(this);
    }

    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMyLocationButtonClickListener(
                new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        validarGPS();
                        return false;
                    }
                }
        );
        validarGPS();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_pesquisar_eventos, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        try {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint("Pesquisar");

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, CadConfiguracao.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        //super.onBackPressed();
    }

    @Override
    public void onLocationChanged(Location location) {

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

    public void addCircle(LatLng latLng) {
        if (mapCircle != null)
            mapCircle.remove();

        CircleOptions circle;
        circle =  new CircleOptions()
                .center(latLng)
                .radius(objConfig.getAlcanceKm() * 1000)
                .strokeColor(Color.parseColor("#66CCFF"))
                .fillColor(Color.parseColor("#2066CCFF"))
                .strokeWidth(2);

        mapCircle  = mMap.addCircle(circle);

    }

    public void mostrarMensagemGPSDesligado() {
        mStatusGPS = false;
        new AlertDialog.Builder(this)
                .setMessage("GPS Desligado! \nDeseja ligar o GPS ?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    public boolean validarGPS() {
        boolean fg_retorno;
        try {
            mStatusGPS = util.verificaGPS(getApplicationContext());
            if (mStatusGPS) {
                fg_retorno = true;
                latLng = util.retornaLocalizacao(getApplicationContext(), this);
                if (latLng != null)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                else
                    fg_retorno = false;

            } else {
                mostrarMensagemGPSDesligado();
                fg_retorno = false;
            }
        } catch (Exception e) {
            fg_retorno  = false;
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return fg_retorno;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //validarGPS();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        try {
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                mQuery = intent.getStringExtra(SearchManager.QUERY);
                new pesquisarEventosPorNome().execute();

            }
        } catch (Exception ex) {
            Toast.makeText(PesquisarEvento.this, "Houve um problema na pesquisa de eventos", Toast.LENGTH_SHORT).show();
        }
    }

    private class pesquisarEventosProximos extends AsyncTask<Void, Integer, Void> {
        private List<Evento> mListaEventos;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(PesquisarEvento.this);
            mProgressDialog = ProgressDialog.show(PesquisarEvento.this, "Carregando...",
                    "Procurando..., por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
                synchronized (this) {
                    try {
                        Evento objEvento = new Evento();
                        mListaEventos = new ArrayList<>();
                        mListaEventos = objEvento.pesquisarEventosProximos(getApplicationContext(),latLng.latitude,latLng.longitude, objConfig.getAlcanceKm());

                    } catch (Exception ex) {
                        mProgressDialog.dismiss();
                    }
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressDialog.dismiss();
            if (mListaEventos.size() > 0){
                for (int i = 0; i < mListaEventos.size() ; i++){
                    LatLng local =  new LatLng(mListaEventos.get(i).getLatitude(),mListaEventos.get(i).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(local).title(mListaEventos.get(i).getTituloEvento()));
                }
            }
        }
    }

    public void pesquisar(View view){
        if (validarGPS()) {
            addCircle(latLng);
            new pesquisarEventosProximos().execute();
        }
    }

    public class pesquisarEventosPorNome extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(PesquisarEvento.this);
            mProgressDialog = ProgressDialog.show(PesquisarEvento.this, "Carregando...",
                    "Procurando...,\n por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (mQuery != null) {
                    Evento objEvento = new Evento();
                    objEvento.pesquisarEventosOnline(getApplicationContext(), mQuery);
                }
            } catch (Exception ex) {
                mProgressDialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressDialog.dismiss();
        }
    }

}
