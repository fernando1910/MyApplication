package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import domain.Usuario;
import helpers.SQLiteHelper;

public class UsuarioDAO {
    private SQLiteDatabase db;

    private static final String TABELA_USUARIO = "tb_usuario";
    private SQLiteHelper dbH;

    private static final String cd_usuario = "cd_usuario";
    private static final String ds_nome = "ds_nome";
    private static final String ds_telefone = "ds_telefone";
    private static final String img_perfil = "img_perfil";
    private static final String ds_caminho_foto = "ds_caminho_foto";
    private static final String nr_codigo_valida_telefone = "nr_codigo_valida_telefone";

    private static final String [] colunas = {UsuarioDAO.cd_usuario, UsuarioDAO.ds_nome,UsuarioDAO.ds_telefone, UsuarioDAO.img_perfil, UsuarioDAO.ds_caminho_foto,UsuarioDAO.nr_codigo_valida_telefone};

    public UsuarioDAO(Context context) {
        dbH =  new SQLiteHelper(context);
        db = dbH.getWritableDatabase();
    }

    public void salvar(Usuario objUsuario ) {
        ContentValues valores = new ContentValues();
        valores.put(cd_usuario, objUsuario.getCodigoUsuario());
        valores.put(ds_nome, objUsuario.getNome());
        valores.put(ds_telefone, objUsuario.getTelefone());
        valores.put(img_perfil, objUsuario.getImagemPerfil());
        valores.put(ds_caminho_foto, objUsuario.getCaminhoFoto());
        valores.put(nr_codigo_valida_telefone,objUsuario.getCodigoVerificardor());
        db.insert(TABELA_USUARIO,null,valores);
    }

    public void atualizar(Usuario objUsuario) {
        ContentValues valores = new ContentValues();
        valores.put(cd_usuario, objUsuario.getCodigoUsuario());
        valores.put(ds_nome, objUsuario.getNome());
        valores.put(ds_telefone, objUsuario.getTelefone());
        valores.put(img_perfil, objUsuario.getImagemPerfil());
        valores.put(ds_caminho_foto, objUsuario.getCaminhoFoto());
        valores.put(nr_codigo_valida_telefone,objUsuario.getCodigoVerificardor());
        db.update(TABELA_USUARIO, valores, null,null);
    }

    public void AtualizarNome(String ds_nome){
        ContentValues values = new ContentValues();
        values.put(this.ds_nome,ds_nome);
        db.update(TABELA_USUARIO, values,null,null);
    }

    public void Deletar(Usuario objUsuario)
    {
        db.delete(TABELA_USUARIO, "cd_usuario = ?", new String[]{"cd_usuario" + ""});
    }

    public String listar()  {
        String teste = "teste";
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        Cursor c = db.query(true, TABELA_USUARIO, new String[]{cd_usuario},null, null, null,null,null,null);
        c.moveToFirst();
        if(c.getCount()  > 0 ) {
           do {
                teste = c.getString(1);
                Usuario objUsuario = new Usuario();
                objUsuario.setCodigoUsuario(c.getInt(c.getInt(0)));
                objUsuario.setNome(c.getString(1));
                objUsuario.setTelefone(c.getString(2));
                usuarios.add(objUsuario);

            }while (c.moveToNext());
        }

        return  teste;
    }

    public Usuario getUsuario()
    {
        Cursor c = db.rawQuery("SELECT * FROM tb_usuario",null);
        //Cursor c = db.query(true, TABELA_USUARIO, UsuarioDAO.colunas,null, null, null,null,null,null);
        c.moveToFirst();
        Usuario objUsuario = null;
        if (c.getCount() > 0)
        {
            objUsuario = new Usuario();

            objUsuario.setCodigoUsuario(c.getInt(0));
            objUsuario.setNome(c.getString(1));
            objUsuario.setTelefone(c.getString(2));
            objUsuario.setImagemPerfil(c.getBlob(3));
            objUsuario.setCaminhoFoto(c.getString(4));
            objUsuario.setCodigoVerificardor(c.getString(5));
        }
        return objUsuario;
    }

    public boolean VerificarPerfil()
    {
        boolean fg_existe_perfil = false;
        try {
            Cursor c = db.query(true,TABELA_USUARIO,UsuarioDAO.colunas,null,null, null,null,null,null);
            c.moveToFirst();
            if(c.getCount()  > 0 ) {
                fg_existe_perfil = true;
            }
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        return  fg_existe_perfil;
    }
}
