package domain;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import project.myapplication.R;


public class Comentario {

    //region Variaveis

    private int cd_comentario;
    private String ds_usuario;
    private String ds_comentario;
    private Date dt_inclusao;
    private Date dt_alteracao;

    //endregion

    //region Propriedades

    public int getCodigoComentario() {
        return cd_comentario;
    }

    public void setCodigoComentario(int cd_comentario) {
        this.cd_comentario = cd_comentario;
    }

    public String getUsuarioComentario() {
        return ds_usuario;
    }

    public void setUsuarioComentario(String ds_usuario) {
        this.ds_usuario = ds_usuario;
    }

    public String getComentario() {
        return ds_comentario;
    }

    public void setComentario(String ds_comentario) {
        this.ds_comentario = ds_comentario;
    }

    public Date getDataInclusao() {
        return dt_inclusao;
    }

    public void setDataInclusao(Date dt_inclusao) {
        this.dt_inclusao = dt_inclusao;
    }

    public Date getDataAlteracao() {
        return dt_alteracao;
    }

    public void setDataAlteracao(Date dt_alteracao) {
        this.dt_alteracao = dt_alteracao;
    }


    //endregion


    //region Métodos

    public List<Comentario> selecionarComentariosOnline(Context context, int cd_evento) throws  Exception{
        List<Comentario> mComentarios = new ArrayList<>();
        Util util = new Util();
        JSONObject jsonObject = new JSONObject();
        String jsonString;
        jsonObject.put("cd_evento",cd_evento);

        jsonString = util.enviarServidor(context.getString(R.string.wsBlueDate),jsonObject.toString(),"selecionarComentarios");
        if (!jsonString.equals("")) {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = new JSONObject(jsonArray.getString(i));
                Comentario mComentario = new Comentario();
                mComentario.setUsuarioComentario(jsonObject.getString("ds_usuario"));
                mComentario.setComentario(jsonObject.getString("ds_comentario"));
                mComentarios.add(mComentario);
            }
        }
        return  mComentarios;
    }



    //endregion
}
