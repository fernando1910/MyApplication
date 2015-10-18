package services;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import domain.Configuracoes;
import domain.PushMessage;

public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        boolean enviarMensagem = true;
        String title = data.getString("title");
        String message = data.getString("message");
        String mTipoNotificacao = data.getString("collapse_key");

        Configuracoes objConfig = new Configuracoes();
        objConfig.carregar(this);

        if (mTipoNotificacao != null) {
            if (mTipoNotificacao.equals("comentario")) {
                if (objConfig.getNotificaComentario() == 0)
                    enviarMensagem = false;
            }
        }
        if (enviarMensagem) {
            PushMessage objPushMessage = new PushMessage(title, message);
            objPushMessage.enviarNotificao(this);
        }
    }

}
