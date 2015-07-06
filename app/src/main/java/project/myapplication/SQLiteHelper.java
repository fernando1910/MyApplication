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
    private String scriptDelete;


    public SQLiteHelper(Context context, String name,  int version, String [] sqlCreate, String sqlDelete) {
        super(context, name, null, version);
        this.scriptSQL = sqlCreate;
        this.scriptDelete = sqlDelete;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(CATEGORIA," CRIANDO O BANCO SQLLITE " );
        int quantidadeScript = scriptSQL.length;
        for( int i = 0; i < quantidadeScript; i ++)
        {
            String sql = scriptSQL[i];
            Log.i(CATEGORIA,sql);
            db.execSQL(sql);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(CATEGORIA,"Atualizando da versao " + oldVersion +" para a versão " + newVersion);
        Log.i(CATEGORIA,scriptDelete);
        onCreate(db);
    }
}
