package services;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import domain.PushMessage;

public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String message = data.getString("message");
        PushMessage objPushMessage = new PushMessage(title,message);
        objPushMessage.enviarNotificao(this);

}
