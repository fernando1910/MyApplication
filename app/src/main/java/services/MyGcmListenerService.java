package services;


import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import de.greenrobot.event.EventBus;
import domain.PushMessage;

public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String message = data.getString("message");

        EventBus.getDefault().post( new PushMessage(title,message));
    }
}
