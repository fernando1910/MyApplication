package project.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
class clsUtil {

    public String RetornaDataHoraMinuto()
    {
        String DataHora;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        DataHora=  dateFormat.format(calendar.getTime());
        return DataHora;
    }

    public Date formataData(String date)
    {
        Date dataConvertida = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        try{
            dataConvertida = simpleDateFormat.parse(date);
        }catch (ParseException e)
        {
            e.printStackTrace();
        }
        return  dataConvertida;
    }


    public Drawable retornarIcone(Drawable drawable, Resources resources)
    {

        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        drawable = new BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap,100,100,true));
        return drawable;

    }

    public String formatarDataBanco(Date data)
    {
        String dataFinal;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dataFinal = dateFormat.format(data);
        return dataFinal;
    }

    public String formatarDataTela(Date data)
    {
        String dataFinal;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dataFinal = dateFormat.format(data);
        return dataFinal;
    }


    public String formatarStringDataBanco(String data)
    {
        String dataFinal = null;
        data = data.replace("/","-");
        String[] dataTemp = data.split("//-");

        int i = 0;

        while (i> dataTemp.length)
        {
            dataFinal = dataFinal + dataTemp[i];
            i++;
        }

        return   dataFinal;
    }

    public String enviarServidor(final String url,final String data, final String comando) throws InterruptedException {
        final String[] resposta = new String[1];
        Thread thread = new Thread(){
            public void run(){
                resposta[0] =  project.myapplication.HttpConnection.getSetDataWeb(url,comando ,data);

            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resposta[0];
    }

    public void ligarGPS(Context context) // NAO FUNCIONA
    {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")) { //gps desligado
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }

    }

    public LatLng retornaLocalizacao(Context context, LocationListener locationListener) {
        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;
        boolean podePegarLocalizacao = false;
        Location location = null;
        final long MIN_TIME_BW_UPDATES = 1000 * 60;
        final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        LatLng latLng = null;
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                podePegarLocalizacao = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            60000,
                            10, locationListener
                    );
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                60000,
                                10, locationListener);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latLng = new LatLng(location.getLatitude(), location.getLongitude());

                            }
                        }
                    }
                }


            }


        } catch (Exception ex) {
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();

        }
        return latLng;

    }

    public boolean verificaGPS(Context context)
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public boolean verificaInternet(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public clsUtil() {
    }

}
