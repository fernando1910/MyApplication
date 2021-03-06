package domain;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.UsuarioDAO;
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
    private int fg_token_pendente;


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

    public String getFotoPerfilServidor() {
        return ds_foto_perfil;
    }

    public void setFotoPerfilServidor(String ds_foto_perfil) {
        this.ds_foto_perfil = ds_foto_perfil;
    }

    public int getTokenPendente() {
        return fg_token_pendente;
    }

    public void setTokenPendente(int fg_token_pendente) {
        this.fg_token_pendente = fg_token_pendente;
    }

    //endregion

    //region Métodos
    public void atualizar(Context context) {
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        usuario_dao.atualizar(this);
    }

    public boolean atualizarNome(Context context) throws Exception {
        Util util = new Util();
        String jsonString = gerarUsuarioJSON();
        String mResposnta = util.enviarServidor(context.getString(R.string.wsBlueDate), jsonString, "atualizarNome");
        if (Integer.parseInt(mResposnta) > 0) {
            UsuarioDAO usuario_dao = new UsuarioDAO(context);
            usuario_dao.atualizarNome(this.ds_nome);
            return true;
        } else {
            return false;
        }
    }

    public boolean atualizarFoto(Context context, byte[] img_perfil, Util util, int cd_usuario) throws Exception {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("ds_foto_perfil", Base64.encodeToString(img_perfil, 0));
        jsonObject.put("cd_usuario", cd_usuario);
        String mRespota = util.enviarServidor(context.getString(R.string.wsBlueDate), jsonObject.toString(), "atualizarFotoPerfil");

        return Integer.parseInt(mRespota) > 0;

    }

    public void carregar(Context context) {
        Usuario objUsuario = new Usuario();
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        objUsuario = usuario_dao.getUsuario();
        if (objUsuario != null) {
            this.cd_usuario = objUsuario.getCodigoUsuario();
            this.ds_nome = objUsuario.getNome();
            this.ds_telefone = objUsuario.getTelefone();
            this.ds_token = objUsuario.getToken();
            this.img_perfil = objUsuario.getImagemPerfil();
            this.ds_caminho_foto = Environment.getExternalStorageDirectory() + "/"
                    + context.getString(R.string.app_name) + "/Perfil/" + String.valueOf(this.cd_usuario) + ".png";
        }

    }

    public void salvar(Context context) {
        UsuarioDAO usuario_dao = new UsuarioDAO(context);
        usuario_dao.salvar(this);

    }

    public String gerarUsuarioJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cd_usuario", this.getCodigoUsuario());
            jsonObject.put("ds_nome", this.getNome());
            jsonObject.put("ds_telefone", this.getTelefone());
            jsonObject.put("nr_ddi", this.getDDI());
            jsonObject.put("img_perfil", this.getImagemPerfil());
            jsonObject.put("nr_codigo_valida_telefone", this.getCodigoVerificardor());
            jsonObject.put("ds_foto_perfil", this.getFotoPerfilServidor());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public boolean salvarPerfilOnline(Context context) throws Exception {
        boolean mRetorno = true;
        Util util = new Util();
        if (util.checarServico(context)) {
            final String caminhoServidor = context.getResources().getString(R.string.wsBlueDate);
            final String jsonString = gerarUsuarioJSON();
            final String[] resposta = new String[1];
            resposta[0] = util.enviarServidor(caminhoServidor, jsonString, "inserirUsuario");
            if (Integer.parseInt(resposta[0]) != 0) {
                this.cd_usuario = Integer.parseInt(resposta[0]);
                if (this.img_perfil != null) {
                    if (util.salvarFoto(this.img_perfil, "Perfil", context, resposta[0])) ;
                    {
                        this.ds_foto_perfil = null;
                        this.img_perfil = null;
                    }
                }
                this.atualizar(context);
            } else {
                mRetorno = false;
            }
        } else {
            mRetorno = false;
        }
        return mRetorno;

    }

    //endregion
}
