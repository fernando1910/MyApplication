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

import java.util.Date;
import java.util.List;

import adapters.CustomListViewEvento;
import domain.Evento;
import domain.Usuario;
import domain.Util;


public class PainelMeusEvento extends Fragment {
    private final String TAG = "Erro";
    private CustomListViewEvento mAdapter;
    private Evento objEvento;
    private ListView lvEventos;
    private ProgressBar mProgressBar;
    private Util util;
    private Button btTentar;
    private TextView tvMensagem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_painel_evento, container, false);
        ((MenuPrincipalNovo) getActivity()).setTitleActionBar("Meus Eventos");


        String TAG = "LOG";
        try {
            lvEventos = (ListView) view.findViewById(R.id.lvEventos);
            mProgressBar = (ProgressBar) view.findViewById(R.id.pbFooterLoading);
            btTentar = (Button) view.findViewById(R.id.btTentar);
            tvMensagem = (TextView) view.findViewById(R.id.tvMensagem);

            objEvento = new Evento();
            util = new Util();
            new carregarEventos().execute();

        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
        }

        return view;
    }

    private class carregarEventos extends AsyncTask<Void, Integer, Void> {
        private boolean fg_conexao_internet;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            btTentar.setVisibility(View.INVISIBLE);
            tvMensagem.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this) {

                fg_conexao_internet = util.verificaInternet(getContext());
                if (fg_conexao_internet) {
                    try {
                        Usuario objUsuario = new Usuario();
                        objUsuario.carregar(getContext());
                        List<Evento> mEventos = objEvento.selecionarMeusEventos(getContext(), util.formatarDataBanco(new Date()), objUsuario.getCodigoUsuario());
                        if (mEventos.size() != 0)
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
            try {
                if (fg_conexao_internet) {
                    if (mAdapter != null) {
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
                                startActivity(new Intent(PainelMeusEvento.this.getContext(), CadEvento.class));
                            }
                        });
                    }
                } else {
                    btTentar.setVisibility(View.VISIBLE);
                    tvMensagem.setVisibility(View.VISIBLE);
                    btTentar.setText(R.string.string_tentar);
                    tvMensagem.setText(R.string.sem_internet);
                    btTentar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new carregarEventos().execute();
                        }
                    });
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
                btTentar.setVisibility(View.VISIBLE);
                tvMensagem.setVisibility(View.VISIBLE);
                tvMensagem.setText(R.string.erro_padrao);
                btTentar.setText(R.string.string_tentar);
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
