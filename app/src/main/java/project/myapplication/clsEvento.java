package project.myapplication;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class clsEvento {
    private int cd_evento;
    private String ds_titulo_evento;
    private String ds_descricao;
    private int cd_usario_inclusao;
    Date dt_evento;
    Date dt_inclusao;
    Date dt_alteracao;
    public int fg_evento_privado;
    private String ds_endereco;

    clsUtil util;

    protected final String caminhoSevidor = "";


    public int getCodigoEvento() {
        return cd_evento;
    }

    public void setCodigoEvento(int cd_evento) {
        this.cd_evento = cd_evento;
    }

    public String getTituloEvento() {
        return ds_titulo_evento;
    }

    public void setTituloEvento(String ds_titulo_evento) {
        this.ds_titulo_evento = ds_titulo_evento;
    }

    public String getDescricao() {
        return ds_descricao;
    }

    public void setDescricao(String ds_descricao) {
        this.ds_descricao = ds_descricao;
    }

    public int getCodigoUsarioInclusao() {
        return cd_usario_inclusao;
    }

    public void setCodigoUsarioInclusao(int cd_usario_inclusao) {
        this.cd_usario_inclusao = cd_usario_inclusao;
    }

    public Date getDataEvento() {
        return dt_evento;
    }

    public void setDataEvento(Date dt_evento) {
        this.dt_evento = dt_evento;
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

    public int getEventoPrivado() {
        return fg_evento_privado;
    }

    public void setEventoPrivado(int fg_evento_privado) {
        this.fg_evento_privado = fg_evento_privado;
    }

    public String getEndereco() {
        return ds_endereco;
    }

    public void setEndereco(String ds_endereco) {
        this.ds_endereco = ds_endereco;
    }

    public void gerarEventoJSON(clsEvento objEvento, String url) throws InterruptedException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String dataEvento = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(objEvento.getDataEvento());


        try {
            jsonObject.put("ds_titulo_evento", objEvento.getTituloEvento());
            jsonObject.put("ds_descricao", objEvento.getDescricao());
            jsonObject.put("cd_usario_inclusao", objEvento.getCodigoUsarioInclusao());
            jsonObject.put("dt_evento", dataEvento);
            jsonObject.put("fg_evento_privado",objEvento.getEventoPrivado() );
            jsonObject.put("ds_endereco", objEvento.getEndereco());


        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        enviarEventoServidor(url, jsonObject.toString());
    }

        public void InserirEvento(Context context, clsEvento objEvento) {
            //Criar Evento DAO
        }

    public String enviarEventoServidor(final String url,final String data) throws InterruptedException {
        final String[] resposta = new String[1];
        Thread thread = new Thread(){
            public void run(){
                resposta[0] =  project.myapplication.HttpConnection.getSetDataWeb(url, "send-json",data);

            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resposta[0];
    }

}



