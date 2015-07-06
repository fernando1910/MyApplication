package project.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Fernando on 24/04/2015.
 */
public class UsuarioDAO {
    private SQLiteDatabase db;
    private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS tb_usuario";
    private static final String[] SCRIPT_DATABASE_CREATE = new String[]{"CREATE TABLE tb_usuario (cd_usuario integer primary"
    + " key autoincrement, ds_nome text not null, ds_telefone text not null)" };

    private static final String NOME_BANCO = "fiesta_louca";
    private static final int VERSAO_BANCO = 1;
    private static final String TABELA_USUARIO = "tb_usuario";
    private SQLiteHelper dbH;

    private static final String cd_usuario = "cd_usuario";
    private static final String ds_nome = "ds_nome";
    private static final String ds_telefome = "ds_telefone";

    private static final String [] colunas = {UsuarioDAO.cd_usuario, UsuarioDAO.ds_nome,UsuarioDAO.ds_telefome};

    public UsuarioDAO(Context context)
    {
        dbH =  new SQLiteHelper(context,UsuarioDAO.NOME_BANCO, UsuarioDAO.VERSAO_BANCO, UsuarioDAO.SCRIPT_DATABASE_CREATE, UsuarioDAO.SCRIPT_DATABASE_DELETE);
        db = dbH.getWritableDatabase();
    }

    public void Salvar(clsUsuario objUsuario )
    {
        ContentValues valores = new ContentValues();
        valores.put("ds_nome", objUsuario.getNome());
        valores.put("ds_telefone", objUsuario.getTelefone());
        db.insert(TABELA_USUARIO,null,valores);
    }

    public void Atualizar(clsUsuario objUsuario)
    {
        ContentValues valores = new ContentValues();
        valores.put("ds_nome", objUsuario.getNome());
        valores.put("ds_telefone", objUsuario.getTelefone());
        db.update(TABELA_USUARIO, valores, " cd_usuario = ? ", new String[]{objUsuario.getCodigoUsuario() + ""});
    }

    public void Deletar(clsUsuario objUsuario)
    {
        db.delete(TABELA_USUARIO, "cd_usuario = ?", new String[]{"cd_usuario"+""});
    }

    public String listar()
    {
        String teste = "teste";
        ArrayList<clsUsuario> usuarios = new ArrayList<clsUsuario>();
        Cursor c = db.query(true, TABELA_USUARIO, UsuarioDAO.colunas,null, null, null,null,null,null);
        c.moveToFirst();
        if(c.getCount()  > 0 ) {
           do {
                teste = c.getString(1);
                clsUsuario objUsuario = new clsUsuario();
                objUsuario.setCodigoUsuario(c.getInt(c.getInt(0)));
                objUsuario.setNome(c.getString(1));
                objUsuario.setTelefone(c.getString(2));
                usuarios.add(objUsuario);

            }while (c.moveToNext());
        }

        return  teste;
    }

    public clsUsuario getUsuario(int id)
    {
        Cursor c = db.query(true, TABELA_USUARIO, UsuarioDAO.colunas," cd_usuario = ? ", new String[]{id+""}, null,null,null,null);
        clsUsuario objUsuario = null;
        if (c.getCount() > 0)
        {
            objUsuario = new clsUsuario();
            c.moveToFirst();
            objUsuario.setCodigoUsuario(c.getInt(0));
            objUsuario.setNome(c.getString(1));
            objUsuario.setTelefone(c.getString(2));
        }
        return objUsuario;
    }
}
