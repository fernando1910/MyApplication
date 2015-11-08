package database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import domain.Evento;
import domain.Util;
import helpers.SQLiteHelper;

public class EventoDAO {
    private SQLiteDatabase db;
    private Context context;

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
    private static String ind_classificacao = "ind_classificacao";
    private static String fg_cancelado = "fg_cancelado";

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
            EventoDAO.ds_caminho_foto_capa,
            EventoDAO.ind_classificacao,
            EventoDAO.fg_cancelado
    };

    //endregion


    public EventoDAO(Context context) {
        SQLiteHelper dbH = new SQLiteHelper(context);
        db = dbH.getWritableDatabase();
    }

    public String salvar(Evento objEvento){
        Util util = new Util();
        ContentValues values = new ContentValues();
        values.put("cd_evento", objEvento.getCodigoEvento());
        values.put("ds_titulo_evento", objEvento.getTituloEvento());
        values.put("ds_descricao", objEvento.getDescricao());
        values.put("nr_latitude", objEvento.getLatitude());
        values.put("nr_longitude", objEvento.getLongitude());
        values.put("cd_usuario_inclusao", objEvento.getCodigoUsuarioInclusao());
        values.put("dt_evento", util.formatarSalvarBanco(objEvento.getDataEvento()));
        values.put("dt_inclusao", util.formatarSalvarBanco(objEvento.getDataInclusao()));
        values.put("fg_evento_privado", objEvento.getEventoPrivado());
        values.put("ds_endereco", objEvento.getEndereco());
        values.put("ds_caminho_foto_capa", objEvento.getCaminhoFotoCapa());
        db.insert(TABELA, null, values);
        SQLiteStatement mSqLiteStatement = db.compileStatement("SELECT changes()");
        return mSqLiteStatement.simpleQueryForString();
    }

    public Evento selecionar(int codigoEvento, Evento objEvento){
        Util util = new Util();
        Cursor c = db.rawQuery("SELECT * FROM tb_evento WHERE cd_evento = " + codigoEvento, null);
        c.moveToFirst();
        if (c.getCount() > 0){
            objEvento.setCodigoEvento(codigoEvento);
            objEvento.setTituloEvento(c.getString(1));
            objEvento.setDescricao(c.getString(2));
            objEvento.setLatitude(c.getDouble(3));
            objEvento.setLongitude(c.getDouble(4));
            objEvento.setCodigoUsuarioInclusao(c.getInt(5));
            objEvento.setDataEvento(util.formataSelecionaBanco(c.getString(6)));
            objEvento.setDataInclusao(util.formataSelecionaBanco(c.getString(7)));
            objEvento.setEventoPrivado(c.getInt(9));
            objEvento.setEndereco(c.getString(10));
            objEvento.setCaminhoFotoCapa(c.getString(11));
            objEvento.setImagemFotoCapa(c.getBlob(12));
            objEvento.setClassificacao(c.getFloat(13));
            objEvento.setCancelado(c.getInt(14));
            //objEvento.setDataEvento(c.);
        }
        c.close();
        return objEvento;
    }

    public void deletarTudo()
    {
        db.delete(TABELA, null, null);
    }

    public List<Evento> selecionarTodosEventos()
    {
        List<Evento> mEventos = new ArrayList<>();
        Util util = new Util();
        Cursor mCursor = db.rawQuery("SELECT * FROM tb_evento WHERE fg_cancelado = 0",null);
        mCursor.moveToFirst();
        if (mCursor.getCount() > 0) {
            do {
                Evento objEvento = new Evento();
                int valida = new Date().compareTo(util.converterData(mCursor.getString(6)));
                objEvento.setCodigoEvento(mCursor.getInt(0));
                objEvento.setTituloEvento(mCursor.getString(1));
                objEvento.setDescricao(mCursor.getString(2));
                objEvento.setLatitude(mCursor.getDouble(3));
                objEvento.setLongitude(mCursor.getDouble(4));
                objEvento.setCodigoUsuarioInclusao(mCursor.getInt(5));
                objEvento.setDataEvento(util.formataSelecionaBanco(mCursor.getString(6)));
                objEvento.setDataInclusao(util.formataSelecionaBanco(mCursor.getString(7)));
//                /objEvento.setDataAlteracao(util.formataData(mCursor.getString(8)));
                objEvento.setEventoPrivado(mCursor.getInt(9));
                objEvento.setEndereco(mCursor.getString(10));
                objEvento.setCaminhoFotoCapa(mCursor.getString(11));
                objEvento.setImagemFotoCapa(mCursor.getBlob(12));
                objEvento.setClassificacao(mCursor.getFloat(13));
                objEvento.setCancelado(mCursor.getInt(14));
                if(valida != 1)
                    mEventos.add(objEvento);
            }while (mCursor.moveToNext());
        }
        mCursor.close();
        return mEventos;
    }

    public String atualizar(Evento objEvento) {

        ContentValues values = new ContentValues();
        values.put(cd_evento, objEvento.getCodigoEvento());
        values.put(ds_titulo_evento, objEvento.getTituloEvento());
        values.put(ds_descricao, objEvento.getDescricao());
        values.put(nr_latitude, objEvento.getLatitude());
        values.put(nr_longitude, objEvento.getLongitude());
        values.put(cd_usuario_inclusao, objEvento.getCodigoUsuarioInclusao());
        values.put(dt_evento, objEvento.getDataEvento().toString());
        values.put(dt_inclusao, objEvento.getDataInclusao().toString());
        values.put(fg_evento_privado, objEvento.getEventoPrivado());
        values.put(ds_caminho_foto_capa, objEvento.getCaminhoFotoCapa());
        values.put(ds_endereco, objEvento.getEndereco());
        values.put(ind_classificacao, objEvento.getClassificacao());
        values.put(fg_cancelado, objEvento.getCancelado());
        db.update(TABELA, values, "cd_evento = ?", new String[]{String.valueOf(objEvento.getCodigoEvento())});
        SQLiteStatement mSqLiteStatement = db.compileStatement("SELECT CHANGES()");
        return mSqLiteStatement.toString();
    }

    public List<Evento>selecionarEventoPorData(Date dt_evento){
        List<Evento> mEventos = new ArrayList<>();
        Cursor mCursor = db.query(TABELA,EventoDAO.colunas,"dt_evento", new String[]{dt_evento.toString()},null,null,EventoDAO.dt_evento);
        mCursor.moveToFirst();
        if (mCursor.getCount() > 0){
            do {
                Evento objEvento = new Evento();
                objEvento.setCodigoEvento(mCursor.getInt(0));
                objEvento.setTituloEvento(mCursor.getString(1));
                objEvento.setDescricao(mCursor.getString(2));
                objEvento.setLatitude(mCursor.getDouble(3));
                objEvento.setLongitude(mCursor.getDouble(4));
                objEvento.setCodigoUsuarioInclusao(mCursor.getInt(5));
                //objEvento.setDataEvento(util.formataData(mCursor.getString(6)));
                //objEvento.setDataInclusao(util.formataData(mCursor.getString(7)));
//                /objEvento.setDataAlteracao(util.formataData(mCursor.getString(8)));
                objEvento.setEventoPrivado(mCursor.getInt(9));
                objEvento.setEndereco(mCursor.getString(10));
                objEvento.setCaminhoFotoCapa(mCursor.getString(11));
                objEvento.setImagemFotoCapa(mCursor.getBlob(12));
                objEvento.setClassificacao(mCursor.getFloat(13));
                objEvento.setCancelado(mCursor.getInt(14));
                mEventos.add(objEvento);

            }while (mCursor.moveToNext());
        }

        mCursor.close();

        return mEventos;
    }
}
