package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import domain.Configuracoes;
import helpers.SQLiteHelper;

public class ConfiguracoesDAO {

    private SQLiteDatabase db;
    private static final String TABELA = "tb_configuracoes";

    private static final String fg_permite_push = "fg_permite_push";
    private static final String fg_permite_alarme = "fg_permite_alarme";
    private static final String fg_notifica_comentario = "fg_notifica_comentario";
    private static final String fg_notifica_mudanca = "fg_notifica_mudanca";
    private static final String fg_telefone_visivel = "fg_telefone_visivel";
    private static final String ind_status_perfil = "ind_status_perfil";
    private static final String nr_alcance_km = "nr_alcance_km";
    private static final String fg_buscar_fotos_online = "fg_buscar_fotos_online";

    private static final String[] colunas = {ConfiguracoesDAO.fg_permite_push,
            ConfiguracoesDAO.fg_permite_alarme, ConfiguracoesDAO.fg_notifica_comentario,
            ConfiguracoesDAO.fg_notifica_mudanca, ConfiguracoesDAO.fg_telefone_visivel,
            ConfiguracoesDAO.ind_status_perfil, ConfiguracoesDAO.nr_alcance_km,
            ConfiguracoesDAO.fg_buscar_fotos_online
    };

    public ConfiguracoesDAO(Context context) {
        SQLiteHelper dbH = new SQLiteHelper(context);
        db = dbH.getWritableDatabase();
    }

    public void carregar(Configuracoes objConfig) {

        Cursor c = db.query(true, TABELA, ConfiguracoesDAO.colunas, null, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            objConfig.setPermitePush(c.getInt(0));
            objConfig.setPermiteAlarme(c.getInt(1));
            objConfig.setNotificaComentario(c.getInt(2));
            objConfig.setNotificaMudanca(c.getInt(3));
            objConfig.setTelefoneVisivel(c.getInt(4));
            objConfig.setStatusPerfil(c.getInt(5));
            objConfig.setAlcanceKm(c.getInt(6));
            objConfig.setBuscarFotosOnline(c.getInt(7));
        }
        c.close();
    }

    public void atualizar(Configuracoes objConfiguracoes) {
        ContentValues valores = new ContentValues();
        valores.put(fg_permite_push, objConfiguracoes.getPermitePush());
        valores.put(fg_permite_alarme, objConfiguracoes.getPermiteAlarme());
        valores.put(fg_notifica_comentario, objConfiguracoes.getNotificaComentario());
        valores.put(fg_notifica_mudanca, objConfiguracoes.getNotificaMudanca());
        valores.put(fg_telefone_visivel, objConfiguracoes.getTelefoneVisivel());
        valores.put(ind_status_perfil, objConfiguracoes.getStatusPerfil());
        valores.put(nr_alcance_km, objConfiguracoes.getAlcanceKm());
        valores.put(fg_buscar_fotos_online, objConfiguracoes.getBuscarFotosOnline());

        db.update(TABELA, valores, null, null);
    }

}
