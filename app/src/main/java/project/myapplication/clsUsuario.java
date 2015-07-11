package project.myapplication;

import android.content.Context;

/**
 * Created by Fernando on 24/04/2015.
 */
public class clsUsuario {
    private int cd_usuario;
    private String ds_nome;
    private String ds_telefone;
    private byte[] img_perfil;


    public String getNome() {
        return ds_nome;
    }

    public void setNome(String ds_nome) {
        this.ds_nome = ds_nome;
    }

    public String getTelefone() {
        return ds_telefone;
    }

    public void setTelefone(String ds_telefone) {
        this.ds_telefone = ds_telefone;
    }

    public int getCodigoUsuario() {
        return cd_usuario;
    }

    public void setCodigoUsuario(int cd_usuario) {
        this.cd_usuario = cd_usuario;
    }


    public byte[] getImagemPerfil() {
        return img_perfil;
    }

    public void setImagemPerfil(byte[] img_perfil) {
        this.img_perfil = img_perfil;
    }

    public void AtualizarUsuario(Context context, clsUsuario objUsuario) {
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        usuario_dao.Atualizar(objUsuario);
    }

    public clsUsuario SelecionarUsuario(Context context)
    {
        clsUsuario objUsuario = null;
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        objUsuario = usuario_dao.getUsuario(1);
        return objUsuario;
    }

    public void InserirUsuario(Context context, clsUsuario objUsuario) {
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        usuario_dao.Salvar(objUsuario);

    }


}
