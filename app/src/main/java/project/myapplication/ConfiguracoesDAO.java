package project.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Fernando on 07/06/2015.
 */
public class ConfiguracoesDAO {

        private SQLiteDatabase db;
        private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS tb_configuracoes";
        private static final String[] SCRIPT_DATABASE_CREATE = new String[]{"" +
                " CREATE TABLE tb_configuracoes " +
                " (fg_permite_push BINARY DEFAULT 0 , " +
                " fg_permite_alarme BINARY DEFAULT 0, " +
                " fg_notifica_comentario BINARY DEFAULT 1, " +
                " fg_notifica_mudanca BINARY DEFAULT 1, " +
                " fg_telefone_visivel BINARY DEFAULT 0 , " +
                " ind_status_perfil INTEGER NOT NULL)",

                "INSERT INTO tb_configuracoes (ind_status_perfil) values (0)"  };


        private static final String NOME_BANCO = "fiesta_louca";
        private static final int VERSAO_BANCO = 1;
        private static final String TABELA = "tb_configuracoes";
        private SQLiteHelper dbH;

        private static final String fg_permite_push = "fg_permite_push";
        private static final String fg_permite_alarme = "fg_permite_alarme";
        private static final String fg_notifica_comentario = "fg_notifica_comentario";
        private static final String fg_notifica_mudanca = "fg_notifica_mudanca";
        private static final String fg_telefone_visivel = "fg_telefone_visivel";
        private static final String ind_status_perfil = "ind_status_perfil";

        private static final String [] colunas = {ConfiguracoesDAO.fg_permite_push,
                ConfiguracoesDAO.fg_permite_alarme,ConfiguracoesDAO.fg_notifica_comentario ,
                ConfiguracoesDAO.fg_notifica_mudanca, ConfiguracoesDAO.fg_telefone_visivel,
                ConfiguracoesDAO.ind_status_perfil
        };

        public ConfiguracoesDAO(Context context){
            dbH =  new SQLiteHelper(context,ConfiguracoesDAO.NOME_BANCO, ConfiguracoesDAO.VERSAO_BANCO, ConfiguracoesDAO.SCRIPT_DATABASE_CREATE, ConfiguracoesDAO.SCRIPT_DATABASE_DELETE);
            db = dbH.getWritableDatabase();
        }

    public clsConfiguracoes Carregar()
    {
        clsConfiguracoes objConfig = null;
        Cursor c = db.query(true, TABELA, ConfiguracoesDAO.colunas,null,null, null,null,null,null);
        if (c.getCount() > 0)
        {
            objConfig = new clsConfiguracoes();
            c.moveToFirst();
            objConfig.setStatusPerfil(c.getInt(5));
        }

        return objConfig;
    }

    public void Atualizar(clsConfiguracoes objConfiguracoes)
    {
        ContentValues valores = new ContentValues();
        valores.put("ind_status_perfil", objConfiguracoes.getStatusPerfil());

        db.update(TABELA, valores, null, null);
    }

    }
