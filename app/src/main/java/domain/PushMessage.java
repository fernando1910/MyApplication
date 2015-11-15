package domain;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import project.myapplication.CadComentario;
import project.myapplication.R;
import project.myapplication.VisualizarConvidados;
import project.myapplication.VisualizarEvento;

public class PushMessage {
    private String title;
    private String message;
    private int ind_tipo_notifacao = Integer.MIN_VALUE;
    private int cd_evento = Integer.MIN_VALUE;
    // 0 - Convites
    // 1 - Comentários
    // 2 - Cancelamento
    // 3 - Confirmação convite
    // 4 - Atualizacao Evento

    public PushMessage(String title, String message, int ind_tipo_notifacao, int cd_evento) {
        this.title = title;
        this.message = message;
        this.ind_tipo_notifacao = ind_tipo_notifacao;
        this.cd_evento = cd_evento;
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

    public void enviarNotificao(Context mContext) {
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setTicker(message)
                        .setSmallIcon(R.drawable.ic_padrao)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true);

        Intent intent = null;
        switch (ind_tipo_notifacao) {
            case 0:
                intent = new Intent(mContext, VisualizarEvento.class);
                intent.putExtra("codigoEvento", cd_evento);
                break;

            case 1:
                intent = new Intent(mContext, CadComentario.class);
                intent.putExtra("codigoEvento", cd_evento);
                break;
            case 2:
                intent = new Intent(mContext, VisualizarEvento.class);
                intent.putExtra("codigoEvento", cd_evento);
                break;
            case 3:
                intent = new Intent(mContext, VisualizarConvidados.class);
                intent.putExtra("codigoEvento", cd_evento);
                break;
            case 4:
                intent = new Intent(mContext, VisualizarEvento.class);
                intent.putExtra("codigoEvento", cd_evento);
                break;

        }

        if (intent != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
        }

        mBuilder.setVibrate(new long[]{400, 400, 400, 400});

        Uri mSom = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(mSom);

        Notification mNotification = mBuilder.build();
        mNotificationManager.notify(R.drawable.ic_padrao, mNotification);

    }
}
