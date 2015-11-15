package services;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import domain.Configuracoes;
import domain.PushMessage;

public class MyGcmListenerService extends GcmListenerService {
    private final String TAG = "ERRO";
    @Override
    public void onMessageReceived(String from, Bundle data) {
        boolean enviarMensagem = true;
        int ind_tipo_notifacao = Integer.MIN_VALUE;
        int cd_evento = Integer.MIN_VALUE;

        String title = data.getString("title");
        String message = data.getString("message");
        try{
            if (data.getString("cd_evento") != null){
                cd_evento = Integer.parseInt(data.getString("cd_evento"));
            }
        }catch (Exception ex){
            Log.i(TAG, ex.getMessage());
        }

        String mTipoNotificacao = data.getString("collapse_key");

        Configuracoes objConfig = new Configuracoes();
        objConfig.carregar(this);

        if (mTipoNotificacao != null) {
            if (mTipoNotificacao.equals("convite")) {
                ind_tipo_notifacao = 0;
            }

            if (mTipoNotificacao.equals("comentario")) {
                ind_tipo_notifacao = 1;
                if (objConfig.getNotificaComentario() == 0)
                    enviarMensagem = false;
            }

            if (mTipoNotificacao.equals("cancelamento")){
                ind_tipo_notifacao = 2;
            }

            if (mTipoNotificacao.equals("confirmacaoConvite")){
                ind_tipo_notifacao = 3;
            }

            if (mTipoNotificacao.equals("atualizacaoEvento")){
                ind_tipo_notifacao = 4;
            }
        }
        if (enviarMensagem) {
            PushMessage objPushMessage = new PushMessage(title, message, ind_tipo_notifacao, cd_evento);
            objPushMessage.enviarNotificao(this);
        }
    }

}
