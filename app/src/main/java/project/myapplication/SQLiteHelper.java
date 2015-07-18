package project.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Fernando on 24/04/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String CATEGORIA = "banco";
    private String[] scriptSQL;
    private String[] scriptDelete;

    private static final String[] SCRIPT_DATABASE_DELETE =  new String[] {
            "DROP TABLE IF EXISTS tb_configuracoes",
            "DROP TABLE IF EXISTS tb_usuario",

    };
    private static final String[] SCRIPT_DATABASE_CREATE = new String[]{"" +
            " CREATE TABLE tb_configuracoes " +
            " (fg_permite_push INTEGER DEFAULT 0 , " +
            " fg_permite_alarme INTEGER DEFAULT 0, " +
            " fg_notifica_comentario INTEGER DEFAULT 1, " +
            " fg_notifica_mudanca INTEGER DEFAULT 1, " +
            " fg_telefone_visivel INTEGER DEFAULT 0 , " +
            " ind_status_perfil INTEGER NOT NULL)",

            "CREATE TABLE tb_usuario " +
                    "(cd_usuario integer primary key , " +
                    "ds_nome text not null, " +
                    "ds_telefone text not null, " +
                    "img_perfil blob," +
                    "ds_caminho_foto text)",

            "INSERT INTO tb_configuracoes (ind_status_perfil) values (0)"  };


    private static final String NOME_BANCO = "fiesta_louca";
    private static final int VERSAO_BANCO = 1;

    public SQLiteHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
        this.scriptSQL = SCRIPT_DATABASE_CREATE;
        this.scriptDelete = SCRIPT_DATABASE_DELETE;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(CATEGORIA," CRIANDO O BANCO SQLLITE " );
        int quantidadeScript = scriptSQL.length;
        for( int i = 0; i < quantidadeScript; i ++)
        {
            try {
                String sql = scriptSQL[i];
                Log.i(CATEGORIA, sql);
                db.execSQL(sql);
            }
            catch (Exception ex)
            {
                Log.i(CATEGORIA, ex.getMessage());
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(CATEGORIA, "Atualizando da versao " + oldVersion + " para a versão " + newVersion);
        int quantidadeScript = scriptDelete.length;
        for ( int i = 0; i < quantidadeScript; i ++)
        {
            Log.i(CATEGORIA,scriptDelete[i]);
            onCreate(db);
        }
    }
}
