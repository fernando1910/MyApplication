package project.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import adapters.CustomListViewEvento;
import domain.Evento;
import domain.Usuario;
import domain.Util;


public class PainelEventosConvites extends Fragment {
    private final String TAG = "ERRO";
    private ListView lvEventos;
    private CustomListViewEvento mAdapter;
    private Evento objEvento;
    private ProgressBar mProgressBar;
    private Usuario objUsuario;
    private Button btTentar;
    private TextView tvMensagem;
    private Util util;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_painel_eventos_convites, container, false);
        try {
            lvEventos = (ListView) view.findViewById(R.id.lvEventos);
            mProgressBar = (ProgressBar) view.findViewById(R.id.pbFooterLoading);
            btTentar = (Button) view.findViewById(R.id.btTentar);
            tvMensagem = (TextView) view.findViewById(R.id.tvMensagem);
            objEvento = new Evento();
            objUsuario = new Usuario();
            util = new Util();
            objUsuario.carregar(PainelEventosConvites.this.getActivity());

            new carregarEventos().execute();
        }catch (Exception ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private class carregarEventos extends AsyncTask<Void,Integer,Void>{
        private boolean fg_conexao_internet;
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            btTentar.setVisibility(View.INVISIBLE);
            tvMensagem.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this){

                fg_conexao_internet = util.verificaInternet(getContext());
                if (fg_conexao_internet) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("cd_usuario", String.valueOf(objUsuario.getCodigoUsuario()));
                        List<Evento> mEventos = objEvento.selecionarEventosOnline(getContext(), "selecionarTodosEventos", jsonObject.toString());
                        mAdapter = new CustomListViewEvento(getContext(), mEventos);
                    } catch (Exception ex) {
                        Log.i(TAG, ex.getMessage());
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.setVisibility(View.GONE);
            if (fg_conexao_internet) {
                if (mAdapter != null) {
                    if (mAdapter.getCount() > 0) {
                        lvEventos.setAdapter(mAdapter);
                        lvEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                int codigoEvento = mAdapter.getCodigoEvento(position);
                                Intent intent = new Intent(getContext(), VisualizarEvento.class);
                                intent.putExtra("codigoEvento", codigoEvento);
                                startActivity(intent);

                            }
                        });
                    } else {
                        btTentar.setVisibility(View.VISIBLE);
                        tvMensagem.setVisibility(View.VISIBLE);
                        btTentar.setText("Criar");
                        tvMensagem.setText("Não há nenhum evento criado. \nCrie um novo evento");
                        btTentar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(PainelEventosConvites.this.getContext(), CadEvento.class));
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(PainelEventosConvites.this.getContext(), "Erro ao carregar seus convites", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
            else{
                btTentar.setVisibility(View.VISIBLE);
                tvMensagem.setVisibility(View.VISIBLE);
                btTentar.setText("Tentar");
                tvMensagem.setText("Sem conexão");
                btTentar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new carregarEventos().execute();
                    }
                });
            }
        }
    }


}
