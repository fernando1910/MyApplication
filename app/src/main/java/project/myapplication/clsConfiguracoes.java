package project.myapplication;

/**
 * Created by Fernando on 07/06/2015.
 */
public class clsConfiguracoes {
    private boolean fg_permite_push;
    private boolean fg_permite_alarme;
    private boolean fg_notifica_comentario;
    private boolean fg_notifica_mudanca;
    private boolean fg_telefone_visivel;
    private int ind_status_perfil;

    public int getStatusPerfil() {
        return ind_status_perfil;
    }

    public void setStatusPerfil(int ind_status_perfil) {
        this.ind_status_perfil = ind_status_perfil;
    }

    public boolean getPermitePush() {
        return fg_permite_push;
    }

    public void setPermitePush(boolean fg_permite_push) {
        this.fg_permite_push = fg_permite_push;
    }

    public boolean getPermiteAlarme() {
        return fg_permite_alarme;
    }

    public void setPermiteAlarme(boolean fg_permite_alarme) {
        this.fg_permite_alarme = fg_permite_alarme;
    }

    public boolean getNotificaComentario() {
        return fg_notifica_comentario;
    }

    public void setNotificaComentario(boolean fg_notifica_comentario) {
        this.fg_notifica_comentario = fg_notifica_comentario;
    }

    public boolean getNotificaMudanca() {
        return fg_notifica_mudanca;
    }

    public void setNotificaMudanca(boolean fg_notifica_mudanca) {
        this.fg_notifica_mudanca = fg_notifica_mudanca;
    }

    public boolean getTelefoneVisivel() {
        return fg_telefone_visivel;
    }

    public void setTelefoneVisivel(boolean fg_telefone_visivel) {
        this.fg_telefone_visivel = fg_telefone_visivel;
    }

    //TESTE

}
