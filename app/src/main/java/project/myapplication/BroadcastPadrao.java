package project.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastPadrao extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        clsUsuario objUsuario = new clsUsuario();
        objUsuario.carregar(context);
        intent.putExtra("codigoUsuario", objUsuario.getCodigoUsuario());
        intent = new Intent("SERVICO_NOVIDADES");

        context.startService(intent);
    }
}
