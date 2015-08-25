package services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import classes.Usuario;
import classes.Util;
import project.myapplication.R;

public class RegistrationIntentService extends IntentService {
    private static final String LOG = "LOG";

    public RegistrationIntentService() {
        super(LOG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean status = preferences.getBoolean("status",false);

        synchronized (LOG){
            InstanceID instanceID = InstanceID.getInstance(this);
            try {

                    String token = instanceID.getToken(
                            getResources().getString(R.string.clound_messaging_key),
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                            null);
                    preferences.edit().putBoolean("status", token != null && token.trim().length() > 0 ).apply();
                    sendRegistrationId(token);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendRegistrationId( String token){
        String path = getResources().getString(R.string.padrao_push_message);
        Util util = new Util();
        Usuario objUsuario = new Usuario();
        objUsuario.carregar(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ds_token", token);
            jsonObject.put("cd_usuario", objUsuario.getCodigoUsuario());
            util.enviarServidor(path,jsonObject.toString(),"send-user");

        } catch (InterruptedException e ) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
