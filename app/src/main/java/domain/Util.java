package domain;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import helpers.HttpConnection;
import project.myapplication.BoasVindas;
import project.myapplication.CadPerfil;
import project.myapplication.CadTelefone;
import project.myapplication.MainActivity;
import project.myapplication.MenuPrincipalNovo;
import project.myapplication.R;
import project.myapplication.ValidarTelefone;

public class Util {

    public String formatarSalvarBanco(Date data) {
        String dataFinal;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm:ss" , Locale.getDefault());
        dataFinal = dateFormat.format(data);
        return dataFinal;
    }

    public Date formataSelecionaBanco(String date) {
        Date dataConvertida = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm" , Locale.getDefault());
        try {
            dataConvertida = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dataConvertida;
    }

    public Date converterData(String date) {
        Date dataConvertida = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm:ss", Locale.getDefault());
        try {
            dataConvertida = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dataConvertida;
    }


    public Drawable retornarIcone(Drawable drawable, Resources resources) {

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        drawable = new BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 100, 100, true));
        return drawable;

    }

    public String formatarDataBanco(Date data) {
        String dataFinal;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dataFinal = dateFormat.format(data);
        return dataFinal;
    }

    public String formatarDataTela(Date data) {
        String dataFinal;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dataFinal = dateFormat.format(data);
        return dataFinal;
    }

    public String formatarHoraTela(Date data) {
        String dataFinal;
        DateFormat dateFormat = new SimpleDateFormat("H:mm", Locale.getDefault());
        dataFinal = dateFormat.format(data);
        return dataFinal;
    }




    public String formatarStringDataBanco(Date data) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(data);
    }

    public String enviarServidor(final String url, final String data, final String comando) throws InterruptedException {
        final String[] resposta = new String[1];
        Thread thread = new Thread() {
            public void run() {
                resposta[0] = HttpConnection.getSetDataWeb(url, comando, data);
            }
        };
        thread.start();
        thread.join();
        return resposta[0];
    }

    public void ligarGPS(Context context) // NAO FUNCIONA111
    {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //gps desligado
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }

    }

    public LatLng retornaLocalizacao(Context context, LocationListener locationListener) throws SecurityException{
        boolean isGPSEnabled;
        boolean isNetworkEnabled;

        Location location = null;
        final long MIN_TIME_BW_UPDATES = 1000 * 60;
        final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        LatLng latLng = null;
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.i("ERRO", "DESLIGADO");
            } else {


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
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();

        }
        return latLng;

    }

    public boolean verificaGPS(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public boolean verificaInternet(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        return i != null && i.isConnected() && i.isAvailable();
    }

    public Util() {
    }

    public String gerarCodigo() {
        Random gerador = new Random();
        return String.valueOf(gerador.nextInt(100000) + 100000);
    }

    public boolean checarServico(Context context) {
        int resultado = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        return resultado == ConnectionResult.SUCCESS;
    }

    public void validarTela(Context context, int ind_tela) {
        Configuracoes objConfig = new Configuracoes();
        objConfig.carregar(context);
        int ind_status = objConfig.getStatusPerfil();
        if (ind_status != ind_tela) {
            Intent intent = null;
            switch (ind_status) {
                case 0:
                    intent = new Intent(context, MainActivity.class);
                    break;
                case 1:
                    intent = new Intent(context, BoasVindas.class);
                    break;
                case 2:
                    intent = new Intent(context, CadTelefone.class);
                    break;
                case 3:
                    intent = new Intent(context, ValidarTelefone.class);
                    break;
                case 4:
                    intent = new Intent(context, CadPerfil.class);
                    break;
                case 5:
                    intent = new Intent(context, MenuPrincipalNovo.class);
                    break;
            }
            if (intent != null) {
                context.startActivity(intent);
            }
        }
    }

    public String retiraEspecial(String mEntrada) {
        return mEntrada.replace(".", "").replace("+", "").replace("-", "");
    }

    public boolean salvarFoto(byte[] mImagem, String mTipoFoto, Context mContext, String mCodigo) {
        try {
            boolean mRetorno;
            File mFile = new File(
                    Environment.getExternalStorageDirectory() + "/" +  // Diretorno
                            mContext.getString(R.string.app_name) + "/" +  // Nome do APP
                            mTipoFoto + "/" +  // Pasta
                            mCodigo + ".png"); // Código

            if (mFile.exists()) {
                Log.i("", String.valueOf(mFile.delete()));
            }

            mRetorno = mFile.createNewFile();
            FileOutputStream mFileOutputStream = new FileOutputStream(mFile);
            mFileOutputStream.write(mImagem);
            mFileOutputStream.close();
            return mRetorno;
        } catch (Exception ex) {
            return false;
        }
    }

    public void gravarLogErro(Context context, int cd_usuario, String ds_tela, String ds_erro) {
        String mURL = context.getString(R.string.wsBlueDate);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cd_usuario", cd_usuario);
            jsonObject.put("ds_tela", ds_tela);
            jsonObject.put("ds_erro", ds_erro);
            this.enviarServidor(mURL, jsonObject.toString(), "gravarLogErro");
        } catch (Exception e) {
            Log.i("ERRO", e.getMessage());
        }
    }

}
