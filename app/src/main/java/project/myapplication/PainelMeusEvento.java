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
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import adapters.CustomListViewEvento;
import domain.Evento;


public class PainelMeusEvento extends Fragment  {
    private final String TAG  ="Erro";
    private CustomListViewEvento mAdapter;
    private Evento objEvento;
    private ListView lvEventos;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_painel_evento, container, false);
        getActivity().setTitle("Meus Eventos");


        String TAG = "LOG";
        try {
            lvEventos = (ListView) view.findViewById(R.id.lvEventos);
            mProgressBar = (ProgressBar) view.findViewById(R.id.pbFooterLoading);
            objEvento = new Evento();
            new carregarEventos().execute();

        }catch (Exception ex){
            Log.i(TAG, ex.getMessage());
        }

        return view;
    }

    private class carregarEventos extends AsyncTask<Void,Integer,Void>{

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this){
                List<Evento> mEventos = objEvento.selecionarTodosEventosLocal(getContext());
                mAdapter = new CustomListViewEvento(getContext(), mEventos);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.setVisibility(View.GONE);
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

        }
    }

}
