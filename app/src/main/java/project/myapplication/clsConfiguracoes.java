package project.myapplication;

/**
 * Created by Fernando on 07/06/2015.
 */
public class clsConfiguracoes {
    private int fg_permite_push;
    private int fg_permite_alarme;
    private int fg_notifica_comentario;
    private int fg_notifica_mudanca;
    private int fg_telefone_visivel;
    private int ind_status_perfil;

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

    //TESTE

}
