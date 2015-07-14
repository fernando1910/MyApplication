package project.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.apache.http.HttpConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Fernando on 24/04/2015.
 */
public class UsuarioDAO {
    private SQLiteDatabase db;

    private static final String TABELA_USUARIO = "tb_usuario";
    private SQLiteHelper dbH;

    private static final String cd_usuario = "cd_usuario";
    private static final String ds_nome = "ds_nome";
    private static final String ds_telefone = "ds_telefone";
    private static final String img_perfil = "img_perfil";
    private static final String ds_caminho_foto = "ds_caminho_foto";

    private static final String [] colunas = {UsuarioDAO.cd_usuario, UsuarioDAO.ds_nome,UsuarioDAO.ds_telefone, UsuarioDAO.img_perfil, UsuarioDAO.ds_caminho_foto};

    public UsuarioDAO(Context context) {
        dbH =  new SQLiteHelper(context);
        db = dbH.getWritableDatabase();
    }

    public void Salvar(clsUsuario objUsuario ) {
        ContentValues valores = new ContentValues();
        valores.put(ds_nome, objUsuario.getNome());
        valores.put(ds_telefone, objUsuario.getTelefone());
        valores.put(img_perfil, objUsuario.getImagemPerfil());
        valores.put(ds_caminho_foto, objUsuario.getCaminhoFoto());
        db.insert(TABELA_USUARIO,null,valores);
    }

    public void Atualizar(clsUsuario objUsuario) {
        ContentValues valores = new ContentValues();
        valores.put(ds_nome, objUsuario.getNome());
        valores.put(ds_telefone, objUsuario.getTelefone());
        valores.put(img_perfil, objUsuario.getImagemPerfil());
        valores.put(ds_caminho_foto, objUsuario.getCaminhoFoto());
        db.update(TABELA_USUARIO, valores, " cd_usuario = ? ", new String[]{objUsuario.getCodigoUsuario() + ""});
    }

    public void Deletar(clsUsuario objUsuario)
    {
        db.delete(TABELA_USUARIO, "cd_usuario = ?", new String[]{"cd_usuario" + ""});
    }

    public String listar()  {
        String teste = "teste";
        ArrayList<clsUsuario> usuarios = new ArrayList<clsUsuario>();
        Cursor c = db.query(true, TABELA_USUARIO, new String[]{cd_usuario},null, null, null,null,null,null);
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
        Cursor c = db.rawQuery("SELECT * FROM tb_usuario",null);
        //Cursor c = db.query(true, TABELA_USUARIO, UsuarioDAO.colunas," cd_usuario = ? ", new String[]{id+""}, null,null,null,null);
        clsUsuario objUsuario = null;
        if (c.getCount() > 0)
        {
            objUsuario = new clsUsuario();
            c.moveToFirst();
            //objUsuario.setCodigoUsuario(c.getInt(0));
            objUsuario.setNome(c.getString(1));
            objUsuario.setTelefone(c.getString(2));
            objUsuario.setImagemPerfil(c.getBlob(3));
            objUsuario.setCaminhoFoto(c.getString(4));
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
            String ex = e.getMessage();
            ex = "";
        }
        return  fg_existe_perfil;
    }
}
