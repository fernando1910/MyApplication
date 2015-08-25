package services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import classes.Usuario;

public class BroadcastPadrao extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Usuario objUsuario = new Usuario();
        objUsuario.carregar(context);
        intent.putExtra("codigoUsuario", objUsuario.getCodigoUsuario());
        intent = new Intent("SERVICO_NOVIDADES");

        context.startService(intent);
    }
}
