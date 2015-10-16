package services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import java.io.IOException;

import domain.Usuario;
import domain.Util;
import project.myapplication.R;

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "LOG";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean status = preferences.getBoolean("status",false);

        synchronized (TAG){
            InstanceID instanceID = InstanceID.getInstance(this);
            try {
                if (!status) {

                    String token = instanceID.getToken(
                            getResources().getString(R.string.clound_messaging_key),
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                            null);
                    preferences.edit().putBoolean("status", token != null && token.trim().length() > 0).apply();
                    sendRegistrationId(token);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRegistrationId( String token){
        if (!token.equals("")) {
            String path = getResources().getString(R.string.wsBlueDate);
            Util util = new Util();
            Usuario objUsuario = new Usuario();
            boolean flagErro;

            objUsuario.carregar(this);
            objUsuario.setToken(token);
            objUsuario.atualizar(this);
            final String[] mResposta = new String[1];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ds_token", token);
                jsonObject.put("cd_usuario", objUsuario.getCodigoUsuario());
                mResposta[0] = util.enviarServidor(path, jsonObject.toString(), "atualizarToken");

                if (Integer.parseInt(mResposta[0]) > 0)
                    flagErro = false;
                else
                    flagErro = true;

            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
                flagErro = true;
            }
            if (flagErro)
                objUsuario.setTokenPendente(1);
            else
                objUsuario.setTokenPendente(0);

            objUsuario.atualizar(this);

        }
    }
}
