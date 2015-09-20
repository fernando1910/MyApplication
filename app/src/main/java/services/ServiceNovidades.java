package services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import domain.Usuario;
import domain.Util;
import project.myapplication.R;


public class ServiceNovidades extends Service {
    public List<Worker> threads = new ArrayList<Worker>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Worker w = new Worker(startId);
        w.start();
        threads.add(w);
        return (START_REDELIVER_INTENT); //RETORNDA DADOS DA INTENT
        //return (START_NOT_STICKY); NAO STARTAR SERVICO
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < threads.size(); i ++)
        {
            threads.get(i).ativo = false;
        }
    }

    class Worker extends Thread{
        private int startId;
        private Util util;
        private boolean ativo = true;
        private Usuario objUsuario;

        public Worker(int startId)
        {
            util = new Util();
            this.startId = startId;
        }

        public void run() {
            try
            {
                if (ativo) {
                    objUsuario = new Usuario();
                    objUsuario.carregar(getApplicationContext());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cd_usuario", objUsuario.getCodigoUsuario());

                    String JSONString;
                    JSONString =  util.enviarServidor(getResources().getString(R.string.wsBlueDate) , jsonObject.toString(), "buscarNovidades");

                    ativo = false;
                }
            }
            catch (Exception e)
            {

            }
            stopSelf(startId);
        }

    }
}
