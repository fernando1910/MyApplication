package project.myapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by Danilo on 28/07/2015.
 */
public class clsSms {
    String Categoria = "Erro haha";
    public void enviarSms(Context context,String destino,String mensagem){
        try{
            SmsManager smsManager = SmsManager.getDefault();
            PendingIntent pIntent = PendingIntent.getBroadcast(context,0,new Intent(),0);
            smsManager.sendTextMessage(destino,null,mensagem,pIntent,null);
        }catch (Exception e){
            Log.e(Categoria,"Erro ao Enviar SMS"+ e.getMessage(),e);
        }

    }

}

