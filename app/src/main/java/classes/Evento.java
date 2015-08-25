package classes;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import database.EventoDAO;
import helpers.HttpConnection;

public class Evento {

    //region Variaveis

        private int cd_evento;
        private String ds_titulo_evento;
        private String ds_descricao;
        private int cd_usario_inclusao;
        private Date dt_evento;
        private Date dt_inclusao;
        private Date dt_alteracao;
        private int fg_evento_privado;
        private String ds_endereco;
        private double nr_latitude;
        private double nr_longitude;
        private Util util;
        private String ds_caminho_foto_capa;
        private String ds_foto_capa;
        private String ds_nome_arquivo_foto;
        protected final String caminhoSevidor = "";
        private String ds_url_foto;

    //endregion

    //region Propriedades de acesso

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

        public double getLatitude() {
            return nr_latitude;
        }

        public void setLatitude(double nr_latitude) {
            this.nr_latitude = nr_latitude;
        }

        public double getLongitude() {
            return nr_longitude;
        }

        public void setLongitude(double nr_longitude) {
            this.nr_longitude = nr_longitude;
        }

        public String getCaminhoFotoCapa() {
            return ds_caminho_foto_capa;
        }

        public void setCaminhoFotoCapa(String ds_caminho_foto_capa) {
            this.ds_caminho_foto_capa = ds_caminho_foto_capa;
        }

        public String getFotoCapa() {
            return ds_foto_capa;
        }

        public void setFotoCapa(String ds_caminho_foto_capa) {
            this.ds_foto_capa = ds_caminho_foto_capa;
        }

        public String getNomeArquivoFoto() {
            return ds_nome_arquivo_foto;
        }

        public void setNomeArquivoFoto(String ds_nome_arquivo_foto) {
            this.ds_nome_arquivo_foto = ds_nome_arquivo_foto;
        }

        public String getUrlFoto() {
            return ds_url_foto;
        }

        public void setUrlFoto(String ds_url_foto) {
            this.ds_url_foto = ds_url_foto;
        }

    //endregion

    //region Metodos comunicação com servidor Online

    public void enviarComentario(int codigoEvento, int codigoUsuario, String comentario, String url ) throws InterruptedException {
        JSONObject jsonObject = new JSONObject();
        Util util = new Util();
        try{
            jsonObject.put("cd_evento", codigoEvento);
            jsonObject.put("cd_usuario", codigoUsuario);
            jsonObject.put("ds_comentario", comentario);

        }catch (JSONException e)
        {

        }

        util.enviarServidor(url,jsonObject.toString(),"send-json");
    }

    public void gerarEventoJSON(Evento objEvento, String url) throws InterruptedException {
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
            jsonObject.put("nr_latitude", objEvento.getLatitude());
            jsonObject.put("nr_longitude", objEvento.getLongitude());
            jsonObject.put("ds_foto_capa", objEvento.getFotoCapa());


        } catch (Exception e) {
            e.printStackTrace();
        }

        enviarEventoServidor(url, jsonObject.toString(),"send-json" );
    }

        public void salvarEvento(Context context, Evento objEvento) {
            EventoDAO eventoDAO = new EventoDAO(context);
            eventoDAO.salvar(objEvento);
        }

    public String enviarEventoServidor(final String url,final String data, final String comando) throws InterruptedException {
        final String[] resposta = new String[1];
        Thread thread = new Thread(){
            public void run(){
                resposta[0] =  HttpConnection.getSetDataWeb(url, comando, data);

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

    public String carregarEventos(String url, String data) throws InterruptedException {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("cd_evento", data);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return enviarEventoServidor(url,jsonObject.toString(),null);
    }


    public String carregarEventoUnico(String url, String data) throws InterruptedException {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("cd_evento", data);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return enviarEventoServidor(url,jsonObject.toString(),"get-json");
    }


    public Evento carregar(String codigoEvento, String url)
    {
        Evento objEvento = new Evento();
        Util util = new Util();
        JSONObject jsonObject;

        String jsonString;
        try {
            jsonString = carregarEventoUnico(url, codigoEvento);
            JSONArray jsonArray = new JSONArray(jsonString);
            jsonObject = new JSONObject(jsonArray.getString(0));

            objEvento.setCodigoEvento(Integer.parseInt(codigoEvento));
            objEvento.setTituloEvento(jsonObject.getString("ds_titulo_evento"));
            objEvento.setDescricao(jsonObject.getString("ds_descricao"));
            objEvento.setEndereco(jsonObject.getString("ds_endereco"));
            objEvento.setDataEvento(util.formataData(jsonObject.getString("dt_evento")));
            objEvento.setEventoPrivado(Integer.parseInt(jsonObject.getString("fg_evento_privado")));
            objEvento.setLatitude(Double.parseDouble(jsonObject.getString("nr_latitude")));
            objEvento.setLongitude(Double.parseDouble(jsonObject.getString("nr_longitude")));




        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }

        return objEvento;
    }

    //endregion

}



