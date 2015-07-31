package project.myapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class padraoPesquisarEndereco extends ActionBarActivity implements SearchView.OnQueryTextListener{

    private SearchView mSearchView;
    private clsUtil util;
    private ImageButton ibPesquisar;
    private EditText etLocalizacao;
    private GoogleMap mMap;
    private LatLng latLng;
    Address address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_pesquisar_endereco);

        setUpMapIfNeeded();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = supportMapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ibPesquisar = (ImageButton)findViewById(R.id.ibPesquisar);
        etLocalizacao = (EditText)findViewById(R.id.etLocalizacao);
        util = new clsUtil();
        ibPesquisar.setImageDrawable(util.retornarIcone(getResources().getDrawable(R.drawable.ic_pesquisar), getResources()));

        //ibPesquisar.setImageDrawable(util.retornarIcone(getResources().getDrawable(R.drawable.ic_localizacao), getResources()));





    }

    public void procurarEndereco(View view)
    {
        String stLocalizacao = etLocalizacao.getText().toString();

        List<Address> addressList;
        if (stLocalizacao.length() > 0)
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(stLocalizacao, 1);
                address = addressList.get(0);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Endereço não informado", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_padrao_pesquisar_endereco, menu);

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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

    public void retonar(View view)
    {
        if (address !=null) {
            Intent intent = new Intent();

            double latitude = address.getLatitude();
            double longitude = address.getLongitude();
            String endereco = address.getAddressLine(0) + " " + address.getAddressLine(1) + " " + address.getAddressLine(2);

            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.putExtra("endereco", endereco);

            setResult(RESULT_OK, intent);

            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Nao foi localizado nenhum endereço", Toast.LENGTH_LONG).show();
        }
    }


}
