package domain;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.UsuarioDAO;
import helpers.HttpConnection;
import project.myapplication.R;

public class Usuario {

    //region Váriaveis
    private int cd_usuario;
    private String ds_nome;
    private String ds_telefone;
    private byte[] img_perfil;
    private String ds_caminho_foto;
    private String nr_ddi;
    private String nr_codigo_valida_telefone;
    private String ds_token;
    private String ds_foto_perfil;


    private static final String TAG = "USUARIO";
    //endregion

    //region propriedades
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

    public String getCaminhoFoto() {
        return ds_caminho_foto;
    }

    public void setCaminhoFoto(String ds_caminho_foto) {
        this.ds_caminho_foto = ds_caminho_foto;
    }

    public String getDDI() {
        return nr_ddi;
    }

    public void setDDI(String nr_ddi) {
        this.nr_ddi = nr_ddi;
    }

    public String getCodigoVerificardor() {
        return nr_codigo_valida_telefone;
    }

    public void setCodigoVerificardor(String nr_codigo_valida_telefone) {
        this.nr_codigo_valida_telefone = nr_codigo_valida_telefone;
    }

    public String getToken() {
        return ds_token;
    }

    public void setToken(String ds_token) {
        this.ds_token = ds_token;
    }

    public String getFotoPerfil() {
        return ds_foto_perfil;
    }

    public void setFotoPerfil(String ds_foto_perfil) {
        this.ds_foto_perfil = ds_foto_perfil;
    }

    //endregion

    //region Métodos
    public void atualizar(Context context) {
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        usuario_dao.atualizar(this);
    }

    public void atualizarNome(Context context, String ds_nome) {
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        usuario_dao.atualizarNome(ds_nome);
    }

    public void atualizarFoto(Context context, byte[] img_perfil) {
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        String r = usuario_dao.atualizarFotoPerfil(img_perfil);
        r = "";
    }

    public Usuario selecionarUsuario(Context context){
        Usuario objUsuario;
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        objUsuario = usuario_dao.getUsuario();
        return objUsuario;
    }

    public void carregar(Context context){
        Usuario objUsuario;
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        objUsuario = usuario_dao.getUsuario();
        if(objUsuario != null) {
            this.cd_usuario = objUsuario.getCodigoUsuario();
            this.ds_nome = objUsuario.getNome();
            this.ds_telefone = objUsuario.getTelefone();
            this.ds_token = objUsuario.getToken();
            this.img_perfil = objUsuario.getImagemPerfil();
        }

    }

    public void salvar(Context context) {
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        usuario_dao.salvar(this);

    }

    public String gerarUsuarioJSON() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonObject.put("ds_nome", this.getNome());
            jsonObject.put("ds_telefone", this.getTelefone());
            jsonObject.put("nr_ddi", this.getDDI());
            jsonObject.put("img_perfil", this.getImagemPerfil());
            jsonObject.put("nr_codigo_valida_telefone", this.getCodigoVerificardor());
            jsonObject.put("ds_foto_perfil", this.getFotoPerfil());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public boolean salvarPerfilOnline(Context context){
        try {

            final String caminhoServidor = context.getResources().getString(R.string.padrao_atualiza_usuario);
            final String jsonString = gerarUsuarioJSON();
            final String[] resposta = new String[1];
            try {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        resposta[0] = HttpConnection.getSetDataWeb(caminhoServidor, "send-json", jsonString);
                    }
                });
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (Integer.parseInt(resposta[0]) != 0) {
                this.cd_usuario = Integer.parseInt(resposta[0]);
                this.atualizar(context);

            } else {
                return false;
            }
        }catch (Exception e){
            Log.i(TAG,e.getMessage());
            return false;
        }

        return true;
    }

    //endregion
}
