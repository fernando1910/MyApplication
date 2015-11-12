package domain;

import android.content.Context;

import org.json.JSONObject;

import database.ConfiguracoesDAO;
import project.myapplication.R;

public class Configuracoes {

    //region variáveis
    private int fg_permite_push;
    private int fg_permite_alarme;
    private int fg_notifica_comentario;
    private int fg_notifica_mudanca;
    private int fg_telefone_visivel;
    private int ind_status_perfil;
    private int fg_buscar_fotos_online;
    private int nr_alcance_km;
    //endregion

    //region propriedades
    public int getStatusPerfil() {
        return ind_status_perfil;
    }

    public void setStatusPerfil(int ind_status_perfil) {
        this.ind_status_perfil = ind_status_perfil;
    }

    public int getPermitePush() {
        return  fg_permite_push;
    }

    public void setPermitePush(int fg_permite_push) {
        this.fg_permite_push = fg_permite_push;
    }

    public int getPermiteAlarme() {
        return fg_permite_alarme;
    }

    public void setPermiteAlarme(int fg_permite_alarme) {
        this.fg_permite_alarme = fg_permite_alarme;
    }

    public int getNotificaComentario() {
        return fg_notifica_comentario;
    }

    public void setNotificaComentario(int fg_notifica_comentario) {
        this.fg_notifica_comentario = fg_notifica_comentario;
    }

    public int getNotificaMudanca() {
        return fg_notifica_mudanca;
    }

    public void setNotificaMudanca(int fg_notifica_mudanca) {
        this.fg_notifica_mudanca = fg_notifica_mudanca;
    }

    public int getTelefoneVisivel() {
        return fg_telefone_visivel;
    }

    public void setTelefoneVisivel(int fg_telefone_visivel) {
        this.fg_telefone_visivel = fg_telefone_visivel;
    }

    public int getAlcanceKm() {
        return nr_alcance_km;
    }

    public void setAlcanceKm(int nr_alcance_km) {
        this.nr_alcance_km = nr_alcance_km;
    }

    public void setBuscarFotosOnline(int fg_buscar_fotos_online){
        this.fg_buscar_fotos_online = fg_buscar_fotos_online;
    }

    public int getBuscarFotosOnline(){
        return fg_buscar_fotos_online;
    }



    //endregion

    //region métodos
    public void atualizarStatus(Context context , int ind_status_perfil){
        ConfiguracoesDAO configuracoesDAO = new ConfiguracoesDAO(context);
        configuracoesDAO.carregar(this);
        this.setStatusPerfil(ind_status_perfil);
        configuracoesDAO.atualizar(this);
    }

    public void carregar(Context context){
        ConfiguracoesDAO configuracoesDAO = new ConfiguracoesDAO(context);
        configuracoesDAO.carregar(this);
    }

    public void atualizar(Context context){
        ConfiguracoesDAO configuracoesDAO = new ConfiguracoesDAO(context);
        configuracoesDAO.atualizar(this);
    }

    public void atualizarTelefoneVisivel(Context context, int cd_usuario, int fg_telefone_visivel) throws  Exception{
        Util util = new Util();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cd_usuario", cd_usuario);
        jsonObject.put("fg_telefone_visivel", fg_telefone_visivel);

        util.enviarServidor(
                context.getString(R.string.wsBlueDate),
                jsonObject.toString(),
                "atualizarTelefoneVisivel"
        );
    }

    //endregion

    public static class MenuConfiguracao
    {
        public int codigo;
        public String Title;
        //public String SubTitle;

        public int value()
        {
            return codigo;
        }
    }

}
