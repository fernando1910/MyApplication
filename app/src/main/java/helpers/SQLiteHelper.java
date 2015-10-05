package helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String CATEGORIA = "banco";
    private String[] scriptSQL;
    private String[] scriptDelete;

    private static final String[] SCRIPT_DATABASE_DELETE =  new String[] {
            "DROP TABLE IF EXISTS tb_configuracoes",
            "DROP TABLE IF EXISTS tb_usuario",
            "DROP TABLE IF EXISTS tb_contato ",
            "DROP TABLE IF EXISTS tb_evento"

    };
    private static final String[] SCRIPT_DATABASE_CREATE = new String[]{"" +
            " CREATE TABLE tb_configuracoes " +
            " (fg_permite_push TINYINT DEFAULT 0 , " +
            " fg_permite_alarme TINYINT DEFAULT 0, " +
            " fg_notifica_comentario TINYINT DEFAULT 1, " +
            " fg_notifica_mudanca TINYINT DEFAULT 1, " +
            " fg_telefone_visivel TINYINT DEFAULT 0 , " +
            " ind_status_perfil TINYINT NOT NULL)",

            "CREATE TABLE tb_usuario " +
                    "(cd_usuario integer primary key , " +
                    "ds_nome text, " +
                    "ds_telefone text not null, " +
                    "img_perfil blob," +
                    "ds_caminho_foto text," +
                    "nr_codigo_valida_telefone text, " +
                    "ds_token text, " +
                    "fg_token_pendente TINYINT DEFAULT 1)",

            "CREATE TABLE tb_contato" +
                    "(cd_contato INTEGER, ds_contato TEXT, img_contato BLOB )",

            "INSERT INTO tb_configuracoes (ind_status_perfil) values (0)",

            "CREATE TABLE tb_evento " +
                    "( cd_evento INTEGER," +
                    "ds_titulo_evento TEXT," +
                    "ds_descricao TEXT ," +
                    "nr_latitude DOUBLE," +
                    "nr_longitude DOUBLE ," +
                    "cd_usuario_inclusao INTEGER," +
                    "dt_evento DATETIME," +
                    "dt_inclusao DATETIME," +
                    "dt_alteracao DATETIME ," +
                    "fg_evento_privado INTEGER," +
                    "ds_endereco TEXT, " +
                    "ds_caminho_foto_capa TEXT," +
                    "img_foto_capa BLOB, " +
                    "ind_classificacao FLOAT )"

    };


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
        for (String aScriptSQL : scriptSQL) {
            try {
                String sql = aScriptSQL;
                Log.i(CATEGORIA, sql);
                db.execSQL(sql);
            } catch (Exception ex) {
                Log.i(CATEGORIA, ex.getMessage());
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(CATEGORIA, "Atualizando da versao " + oldVersion + " para a versão " + newVersion);
        for (String aScriptDelete : scriptDelete) {
            Log.i(CATEGORIA, aScriptDelete);
            onCreate(db);
        }
    }
}
