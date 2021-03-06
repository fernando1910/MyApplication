package domain;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import database.EventoDAO;
import project.myapplication.R;

public class Evento {

    //region Variaveis

    private int cd_evento;
    private String ds_titulo_evento;
    private String ds_descricao;
    private int cd_usuario_inclusao;
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
    private byte[] img_foto_capa;
    private float ind_classificacao;
    private int fg_cancelado;
    private int fg_participa;
    private int nr_convidados;
    private int nr_comentarios;

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

    public int getCodigoUsuarioInclusao() {
        return cd_usuario_inclusao;
    }

    public void setCodigoUsuarioInclusao(int cd_usuario_inclusao) {
        this.cd_usuario_inclusao = cd_usuario_inclusao;
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

    public byte[] getImagemFotoCapa() {
        return img_foto_capa;
    }

    public void setImagemFotoCapa(byte[] img_foto_capa) {
        this.img_foto_capa = img_foto_capa;
    }

    public float getClassificacao() {
        return ind_classificacao;
    }

    public void setClassificacao(float vlr_classificacao) {
        this.ind_classificacao = vlr_classificacao;
    }

    public int getCancelado() {
        return fg_cancelado;
    }

    public void setCancelado(int fg_cancelado) {
        this.fg_cancelado = fg_cancelado;
    }

    public int getParticipa() {
        return fg_participa;
    }

    public void setParticipa(int fg_participa) {
        this.fg_participa = fg_participa;
    }

    public void setConvidados(int nr_convidados) {
        this.nr_convidados = nr_convidados;
    }

    public int getConvidados() {
        return this.nr_convidados;
    }

    public int getComentarios() {
        return nr_comentarios;
    }

    public void setComentarios(int nr_comentarios) {
        this.nr_comentarios = nr_comentarios;
    }

    //endregion

    //region Metodos comunicação com servidor Online

    public int salvarEventoOnline(Context context, int tipoOperacao, String ds_mensagem) throws Exception {

        Util util = new Util();
        final String caminhoServidor = context.getResources().getString(R.string.wsBlueDate);
        final JSONObject jsonObject = gerarEventoJSON(util);
        String mResposta;

        if (tipoOperacao == 1) {
            mResposta = util.enviarServidor(caminhoServidor, jsonObject.toString(), "inserirEvento");
            if (Integer.parseInt(mResposta) != 0) {
                this.cd_evento = Integer.parseInt(mResposta);
                if (getImagemFotoCapa() != null)
                    util.salvarFoto(getImagemFotoCapa(), "Evento", context, mResposta);
            } else {
                throw new Exception("Não salvou online");
            }
        } else {
            jsonObject.put("ds_mensagem", ds_mensagem);
            mResposta = util.enviarServidor(caminhoServidor, jsonObject.toString(), "atualizarEvento");
            if (Integer.parseInt(mResposta) <= 0)
                throw new Exception("Não salvou online");
        }
        return this.cd_evento;

    }

    public String enviarComentario(int codigoEvento, int codigoUsuario, String comentario, String url) throws InterruptedException, JSONException {
        JSONObject jsonObject = new JSONObject();
        Util util = new Util();
        jsonObject.put("cd_evento", codigoEvento);
        jsonObject.put("cd_usuario", codigoUsuario);
        jsonObject.put("ds_comentario", comentario);

        return util.enviarServidor(url, jsonObject.toString(), "comentarEvento");
    }

    public JSONObject gerarEventoJSON(Util util) throws InterruptedException {
        JSONObject jsonObject = new JSONObject();
        try {
            if (this.getCodigoEvento() != Integer.MIN_VALUE)
                jsonObject.put("cd_evento", this.getCodigoEvento());
            jsonObject.put("ds_titulo_evento", this.getTituloEvento());
            jsonObject.put("ds_descricao", this.getDescricao());
            jsonObject.put("cd_usuario_inclusao", this.getCodigoUsuarioInclusao());
            jsonObject.put("dt_evento", util.formatarStringDataBanco(this.getDataEvento()));
            jsonObject.put("fg_evento_privado", this.getEventoPrivado());
            jsonObject.put("ds_endereco", this.getEndereco());
            jsonObject.put("nr_latitude", this.getLatitude());
            jsonObject.put("nr_longitude", this.getLongitude());
            jsonObject.put("ds_foto_capa", this.getFotoCapa());

            if (this.getDataInclusao() != null) {
                jsonObject.put("dt_inclusao", util.formatarStringDataBanco(this.getDataInclusao()));
            }
            if (this.getDataAlteracao() != null) {
                jsonObject.put("dt_alteracao", util.formatarStringDataBanco(this.getDataAlteracao()));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public String salvarEventoLocal(Context context) {
        EventoDAO eventoDAO = new EventoDAO(context);
        return eventoDAO.salvar(this);
    }

    public void carregarOnline(int cd_evento, Context context, int cd_usuario) throws Exception {
        Util util = new Util();
        JSONObject jsonObjectEnviar = new JSONObject();

        jsonObjectEnviar.put("cd_evento", cd_evento);
        jsonObjectEnviar.put("cd_usuario", cd_usuario);
        String jsonString = util.enviarServidor(context.getString(R.string.wsBlueDate), jsonObjectEnviar.toString(), "selecionarEvento");
        JSONArray jsonArray = new JSONArray(jsonString);
        JSONObject jsonObject = new JSONObject(jsonArray.getString(0));


        this.setTituloEvento(jsonObject.getString("ds_titulo_evento"));
        this.setDescricao(jsonObject.getString("ds_descricao"));
        this.setEndereco(jsonObject.getString("ds_endereco"));
        this.setDataEvento(util.formataSelecionaBanco(jsonObject.getString("dt_evento")));
        this.setEventoPrivado(Integer.parseInt(jsonObject.getString("fg_evento_privado")));
        this.setLatitude(Double.parseDouble(jsonObject.getString("nr_latitude")));
        this.setLongitude(Double.parseDouble(jsonObject.getString("nr_longitude")));
        this.setParticipa(Integer.parseInt(jsonObject.getString("fg_participa")));
        this.setClassificacao(Float.parseFloat(jsonObject.getString("ind_classificacao")));
        this.setCodigoUsuarioInclusao(jsonObject.getInt("cd_usuario_inclusao"));
        this.setCodigoEvento(cd_evento);
    }

    public void carregarLocal(int codigoEvento, Context context) {
        EventoDAO eventoDAO = new EventoDAO(context);
        eventoDAO.selecionar(codigoEvento, this);
    }

    public List<Evento> selecionarTodosEventosLocal(Context context) {
        EventoDAO eventoDAO = new EventoDAO(context);
        return eventoDAO.selecionarTodosEventos();
    }

    public List<Evento> selecionarEventosPorData(Context context, String dt_evento_string, int cd_usuario) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dt_evento", dt_evento_string);
        jsonObject.put("cd_usuario", cd_usuario);
        return selecionarEventosOnline(context, "selecionarEventosPorData", jsonObject.toString());
    }

    public List<Evento> selecionarTopFestas(Context context) throws InterruptedException, JSONException, NullPointerException {
        return selecionarEventosOnline(context, "selecionarTopFestas", null);
    }

    public List<Evento> selecionarTopConvidados(Context context) throws Exception {
        return selecionarEventosOnline(context, "selecionarTopConvidados", null);
    }

    public List<Evento> selecionarTopComentarios(Context context) throws Exception {
        return selecionarEventosOnline(context, "selecionarTopComentarios", null);
    }

    public boolean classificarEvento(Context context, int codigoEvento, float classificacaoEvento, int codigoUsuario) throws Exception {
        JSONObject jsonObject = new JSONObject();
        Util util = new Util();

        jsonObject.put("cd_evento", String.valueOf(codigoEvento));
        jsonObject.put("ind_classificacao", String.valueOf(classificacaoEvento));
        jsonObject.put("cd_usuario", String.valueOf(codigoUsuario));
        String mResposta = util.enviarServidor(context.getString(R.string.wsBlueDate), jsonObject.toString(), "classificarEvento");
        return Integer.parseInt(mResposta) > 1;
    }

    public List<Evento> selecionarEventosOnline(Context context, String comando, String parametro) throws InterruptedException, JSONException {
        List<Evento> mEventos = new ArrayList<>();
        Util util = new Util();
        String jsonString = util.enviarServidor(context.getString(R.string.wsBlueDate), parametro, comando);
        JSONArray jsonArrayResultado = new JSONArray(jsonString);
        JSONObject jsonObjectResultado;

        for (int i = 0; i < jsonArrayResultado.length(); i++) {
            jsonObjectResultado = new JSONObject(jsonArrayResultado.getString(i));
            Evento mEvento = new Evento();
            mEvento.setCodigoEvento(Integer.parseInt(jsonObjectResultado.getString("cd_evento")));
            mEvento.setTituloEvento(jsonObjectResultado.getString("ds_titulo_evento"));
            mEvento.setLatitude(jsonObjectResultado.getDouble("nr_latitude"));
            mEvento.setLongitude(jsonObjectResultado.getDouble("nr_longitude"));
            mEvento.setEventoPrivado(jsonObjectResultado.getInt("fg_evento_privado"));
            mEvento.setEndereco(jsonObjectResultado.getString("ds_endereco"));
            if (jsonObjectResultado.has("ind_classificacao"))
                mEvento.setClassificacao(Float.parseFloat(jsonObjectResultado.getString("ind_classificacao")));
            if (jsonObjectResultado.has("nr_convidados"))
                mEvento.setConvidados(jsonObjectResultado.getInt("nr_convidados"));
            if (jsonObjectResultado.has("nr_comentarios"))
                mEvento.setComentarios(jsonObjectResultado.getInt("nr_comentarios"));

            mEventos.add(mEvento);
        }
        return mEventos;
    }

    public List<Evento> pesquisarEventosProximos(Context context, double nr_latitude, double nr_longitude, int nr_distancia) throws JSONException, InterruptedException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nr_latitude", nr_latitude);
        jsonObject.put("nr_longitude", nr_longitude);
        jsonObject.put("nr_distancia", nr_distancia);
        return selecionarEventosOnline(context, "buscarEventosProximos", jsonObject.toString());
    }

    public List<Evento> pesquisarEventosOnline(Context context, String query) throws JSONException, InterruptedException {
        return selecionarEventosOnline(context, "pesquisarEvento", query);
    }

    public boolean cancelar(Context context, int codigoEvento) throws Exception {
        JSONObject jsonObject = new JSONObject();
        Util util = new Util();
        String mURL = context.getString(R.string.wsBlueDate);
        jsonObject.put("cd_evento", String.valueOf(codigoEvento));
        String mResposta = util.enviarServidor(mURL, jsonObject.toString(), "cancelarEvento");
        return Integer.parseInt(mResposta) > 0;
    }

    public List<Evento> buscarConvites(Context context, String data, int codigoUsuario) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("codigoUsuario", codigoUsuario);
        jsonObject.put("data", data);
        return selecionarEventosOnline(context, "buscarConvites", jsonObject.toString());
    }

    public boolean participar(Context context, int cd_usuario, int cd_evento, String ds_nome) throws Exception {
        boolean fg_retorno = false;
        String mResposta;
        Util util = new Util();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cd_usuario", cd_usuario);
        jsonObject.put("cd_evento", cd_evento);
        jsonObject.put("ds_nome", ds_nome);
        mResposta = util.enviarServidor(context.getString(R.string.wsBlueDate), jsonObject.toString(), "participar");
        if (Integer.parseInt(mResposta) > 0)
            fg_retorno = true;

        return fg_retorno;
    }

    public List<Evento> selecionarMeusEventos(Context context, String dt_evento, int cd_usuario) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dt_evento", dt_evento);
        jsonObject.put("cd_usuario", cd_usuario);
        return selecionarEventosOnline(context, "selecionarMeusEventos", jsonObject.toString());

    }

    public String verificarMudancaEvento(Evento objEvento, Evento objEventoBkp, String ds_nome) {
        boolean fg_primeiro = true;
        String mMensagem = "";
        if (!objEvento.getTituloEvento().equals(objEventoBkp.getTituloEvento())) {
            mMensagem = "Alterou o titulo";
            fg_primeiro = false;
        }
        if (!objEvento.getDescricao().equals(objEventoBkp.getDescricao())) {
            if (fg_primeiro) {
                mMensagem = "Alterou a descrição";
                fg_primeiro = false;
            } else {
                mMensagem += ", descrição";
            }
        }

        if (objEvento.getDataEvento().equals(objEventoBkp.getDataEvento())) {
            if (fg_primeiro) {
                mMensagem = "Alterou a data";
                fg_primeiro = false;
            } else {
                mMensagem += ", data";
            }
        }

        if (objEvento.getEndereco().equals(objEventoBkp.getEndereco())) {
            if (fg_primeiro) {
                mMensagem = "Alterou o endereço";
            } else {
                mMensagem += ", endereço";
            }
        }

        if (!fg_primeiro)
            mMensagem = ds_nome + " " + mMensagem;

        return mMensagem;

    }

    public Evento gerarBackup() {
        Evento objEventoBkp = new Evento();
        objEventoBkp.setTituloEvento(this.getTituloEvento());
        objEventoBkp.setDescricao(this.getDescricao());
        objEventoBkp.setEndereco(this.getEndereco());
        objEventoBkp.setDataEvento(this.getDataEvento());
        return objEventoBkp;
    }

    public List<Evento> buscarAlteracoes(Context context, int cd_usuario) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cd_usuario", cd_usuario);
        return selecionarEventosOnline(context, "buscarAlterecoes", jsonObject.toString());
    }

    public List<Evento> buscarNovosComentario(Context context, int cd_usuario) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cd_usuario", cd_usuario);
        return selecionarEventosOnline(context, "buscarNovosComentario", jsonObject.toString());
    }
    //endregion
}