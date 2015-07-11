package project.myapplication;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Fernando on 07/06/2015.
 */
public class clsUtil {

    public String RetornaDataHoraMinuto()
    {
        String DataHora;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        DataHora=  dateFormat.format(calendar.getTime());
        return DataHora;
    }

    public void AtualizarStatus(Context context, int ind_status_perfil)
    {
        clsConfiguracoes objConfig;
        ConfiguracoesDAO config_dao = new ConfiguracoesDAO(context);
        objConfig = config_dao.Carregar();
        objConfig.setStatusPerfil(ind_status_perfil);
        config_dao.Atualizar(objConfig);
    }

    public clsUtil() {
    }

}
