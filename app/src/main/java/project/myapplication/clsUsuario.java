package project.myapplication;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fernando on 24/04/2015.
 */
public class clsUsuario {
    private int cd_usuario;
    private String ds_nome;
    private String ds_telefone;
    private byte[] img_perfil;
    private String ds_caminho_foto;


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

    public String getCaminhoFoto() {
        return ds_caminho_foto;
    }

    public void setCaminhoFoto(String ds_caminho_foto) {
        this.ds_caminho_foto = ds_caminho_foto;
    }

    public void gerarUsuarioJSON(clsUsuario objUsuario) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try{
            jsonObject.put("ds_nome", objUsuario.getNome());
            jsonObject.put("ds_telefone", objUsuario.getTelefone());

        } catch (JSONException e) {
            e.printStackTrace();
        }

         enviarUsuarioServidor(jsonObject.toString());

    }

    public String enviarUsuarioServidor(final String data)
    {
        final String[] resposta = new String[1];
        new Thread(){
            public void run(){
                resposta[0] =  project.myapplication.HttpConnection.getSetDataWeb("http://www.fiesta1.hol.es/process.php", "send-json",data);

            }
        }.start();
        return resposta[0];
    }


}
