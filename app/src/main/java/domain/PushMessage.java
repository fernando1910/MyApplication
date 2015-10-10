package domain;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import project.myapplication.R;

public class PushMessage {
    private String title;
    private String message;

    public PushMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void enviarNotificao(Context mContext){
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.star)
                        .setContentTitle(title)
                        .setContentText(message);

        Notification mNotification = mBuilder.build();
        mNotificationManager.notify(R.drawable.star,mNotification);

        try{
            Uri mSom = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone mRingtone = RingtoneManager.getRingtone(mContext, mSom);
            mRingtone.play();
        }catch (Exception e){

        }

    }
}
