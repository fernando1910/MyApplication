package project.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import adapters.CustomListViewEndereco;
import domain.Endereco;
import domain.Util;


public class PesquisarEndereco extends ActionBarActivity implements LocationListener {

    private SearchView mSearchView;
    private Util util;
    private GoogleMap mMap;
    private LatLng latLng;
    private Address address;
    private ProgressBar mProgressBar;
    private TextView tvInstrucao;
    private ListView lvEndereco;


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

        tvInstrucao = (TextView)findViewById(R.id.tvInstrucao);
        lvEndereco =  (ListView)findViewById(R.id.lvEndereco);
        mProgressBar = (ProgressBar)findViewById(R.id.pbFooterLoading);



        util = new Util();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density = getResources().getDisplayMetrics().density;
        int height = (int) (displayMetrics.heightPixels / density);

        try {
            ViewGroup.LayoutParams params = supportMapFragment.getView().getLayoutParams();
            params.height = height ;
            supportMapFragment.getView().setLayoutParams(params);
        }catch (NullPointerException e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            if(util.verificaGPS(getApplicationContext()))
            {
                try {
                    latLng = util.retornaLocalizacao(getApplicationContext(), this);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                }
                catch (Exception e)
                {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-233251,-463810),15.0f));

            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_padrao_pesquisar_endereco, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        try{

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint("Pesquisar");

        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        hendleSearch(intent);
    }

    public void hendleSearch( Intent intent)
    {
        if (Intent.ACTION_SEARCH.equalsIgnoreCase( intent.getAction() ))
        {
            String stLocalizacao = intent.getStringExtra( SearchManager.QUERY );

            List<Address> addressList;
            if (stLocalizacao.length() > 0)
            {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(stLocalizacao, 1);
                    if(addressList.size() > 0) {
                        address = addressList.get(0);
                        latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                        lvEndereco.setVisibility(View.INVISIBLE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        tvInstrucao.setVisibility(View.INVISIBLE);

                        new carregarLocais().execute();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Endereço não encontrado", Toast.LENGTH_LONG).show();
                    }

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public String locaisProximos(Address address ) {

        final String GOOGLE_KEY = getString(R.string.google_map_key_browser);
        String url = "https://maps.googleapis.com/maps/api/place/search/json?location="+address.getLatitude()+","+ address.getLongitude()+"&radius=250&sensor=true&key=" + GOOGLE_KEY;


        StringBuffer buffer_string = new StringBuffer(url);
        final String[] replyString = {""};
        final HttpClient httpclient = new DefaultHttpClient();
        final HttpGet httpget = new HttpGet(buffer_string.toString());

        Thread thread = new Thread(){
            public void run(){
                try {
                    HttpResponse response = httpclient.execute(httpget);
                    InputStream is = response.getEntity().getContent();
                    // buffer input stream the result
                    BufferedInputStream bis = new BufferedInputStream(is);

                    ByteArrayBuffer baf = new ByteArrayBuffer(20);

                    int current = 0;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }
                    replyString[0] = new String(baf.toByteArray());

                } catch (Exception e) {
                    replyString[0] = e.getMessage();

                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println(replyString[0]);
        return replyString[0].trim();

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

    public class carregarLocais extends AsyncTask<Void,Intent,Void>
    {
        List<Endereco> enderecos;
        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this)
            {
                try{

                    JSONObject jsonObject = new JSONObject(locaisProximos(address));
                    if (jsonObject.has("results")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                         enderecos = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Endereco endereco = new Endereco();
                            endereco.setNome(jsonArray.getJSONObject(i).optString("name"));
                            endereco.setEndereco(jsonArray.getJSONObject(i).optString("vicinity"));
                            endereco.setUrlIcon(jsonArray.getJSONObject(i).optString("icon"));
                            enderecos.add(endereco);
                        }
                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final CustomListViewEndereco arrayAdapter = new CustomListViewEndereco(PesquisarEndereco.this,enderecos);
            lvEndereco.setAdapter(arrayAdapter);
            lvEndereco.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params  = lvEndereco.getLayoutParams();
            //params = 300;
            lvEndereco.setLayoutParams(params);
            lvEndereco.requestLayout();


            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
