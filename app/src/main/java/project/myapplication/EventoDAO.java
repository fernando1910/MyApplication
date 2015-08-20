package project.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class EventoDAO {
    private SQLiteDatabase db;
    private SQLiteHelper dbH;

    private static final String TABELA = "tb_evento";

    //region colunas
    private static String cd_evento = "cd_evento";
    private static String ds_titulo_evento = "ds_titulo_evento";
    private static String ds_descricao = "ds_descricao";
    private static String nr_latitude = "nr_latitude";
    private static String nr_longitude = "nr_longitude";
    private static String cd_usuario_inclusao = "cd_usuario_inclusao";
    private static String dt_evento ="dt_evento";
    private static String dt_inclusao = "dt_inclusao";
    private static String dt_alteracao ="dt_alteracao";
    private static String fg_evento_privado = "fg_evento_privado";
    private static String ds_endereco ="ds_endereco";
    private static String ds_caminho_foto_capa ="ds_caminho_foto_capa";

    public static final String colunas[] = {
            EventoDAO.cd_evento,
            EventoDAO.ds_titulo_evento,
            EventoDAO.ds_descricao,
            EventoDAO.nr_latitude,
            EventoDAO.nr_longitude,
            EventoDAO.cd_usuario_inclusao,
            EventoDAO.dt_evento,
            EventoDAO.dt_inclusao,
            EventoDAO.dt_alteracao,
            EventoDAO.fg_evento_privado,
            EventoDAO.ds_endereco,
            EventoDAO.ds_caminho_foto_capa
    };

    //endregion


    public EventoDAO(Context context) {
        dbH =  new SQLiteHelper(context);
        db = dbH.getWritableDatabase();
    }

    public void salvar(clsEvento objEvento){
        ContentValues values = new ContentValues();
        values.put("cd_evento", objEvento.getCodigoEvento());
        values.put("ds_titulo_evento", objEvento.getTituloEvento());
        values.put("ds_descricao", objEvento.getDescricao());
        values.put("nr_latitude", objEvento.getLatitude());
        values.put("nr_longitude", objEvento.getLongitude());
        values.put("cd_usuario_inclusao", objEvento.getCodigoUsarioInclusao());
        values.put("dt_evento", objEvento.getDataEvento().toString());
        values.put("dt_inclusao", objEvento.getDataInclusao().toString());
        values.put("dt_alteracao", objEvento.getDataAlteracao().toString());
        values.put("fg_evento_privado", objEvento.getEventoPrivado());
        values.put("ds_endereco", objEvento.getEndereco());
        values.put("ds_caminho_foto_capa", objEvento.getCaminhoFotoCapa());
        db.insert(TABELA, null,values);
    }

    public void deletarTudo()
    {
        db.delete(TABELA,null,null);
    }
}
