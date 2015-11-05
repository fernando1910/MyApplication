package project.myapplication;


import android.app.ProgressDialog;
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

import java.util.List;

import adapters.CustomListViewRanking;
import domain.Evento;
import domain.Util;


/**
 * A simple {@link Fragment} subclass.
 */
public class VisualizarTopConvidados extends Fragment {
    private final String TAG = "LOG";
    private ListView mListView;
    private List<Evento> mItems;
    private CustomListViewRanking mAdapter;
    private ProgressBar mProgressBar;
    private Evento objEvento;
    private Button btTentar;
    private TextView tvMensagem;


    public VisualizarTopConvidados() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visualizar_top_convidados, container, false);
        mListView = (ListView) view.findViewById(R.id.lvTopConvidados);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pbFooterLoading);
        tvMensagem = (TextView) view.findViewById(R.id.tvMensagem);
        btTentar = (Button) view.findViewById(R.id.btTentar);
        btTentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btTentar.setVisibility(View.INVISIBLE);
                tvMensagem.setVisibility(View.INVISIBLE);
                new listarEventos().execute();
            }
        });

        objEvento = new Evento();

        new listarEventos().execute();
        return view;
    }

    private class listarEventos extends AsyncTask<Void, Integer, Void> {
        private Util util;
        private boolean fg_conexao_internet;

        @Override
        protected void onPreExecute() {
            btTentar.setVisibility(View.INVISIBLE);
            tvMensagem.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this) {
                try {
                    util = new Util();
                    fg_conexao_internet = util.verificaInternet(VisualizarTopConvidados.this.getContext());
                    if (fg_conexao_internet) {
                        mItems = objEvento.selecionarTopConvidados(getContext());
                        mAdapter = new CustomListViewRanking(getContext(), mItems, 1);
                    }
                } catch (Exception ex) {
                    Log.i(TAG, ex.getMessage());

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.INVISIBLE);
            btTentar.setVisibility(View.INVISIBLE);
            tvMensagem.setVisibility(View.INVISIBLE);
            try {
                if (fg_conexao_internet) {
                    mListView.setAdapter(mAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(), VisualizarEvento.class);
                            intent.putExtra("codigoEvento", mAdapter.getCodigoEvento(position));
                            startActivity(intent);

                        }
                    });
                } else {
                    btTentar.setVisibility(View.VISIBLE);
                    tvMensagem.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
                Log.i(TAG, ex.getMessage());
            }

        }
    }


}
