package project.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.EventoAdapter;
import domain.Evento;
import interfaces.RecyclerViewOnClickListenerHack;


public class PainelEvento extends Fragment implements RecyclerViewOnClickListenerHack {

    private Evento objEvento;
    private ProgressDialog progressDialog;
    private String jsonString;
    private RecyclerView rvEvento;
    private List<Evento> eventos;
    private EventoAdapter adapter = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_painel_evento, container, false);
        rvEvento = (RecyclerView) view.findViewById(R.id.rvEvento);
        rvEvento.setHasFixedSize(true);
        rvEvento.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvEvento.getLayoutManager();
                EventoAdapter adapter = (EventoAdapter) rvEvento.getAdapter();

                if (eventos.size() == linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1) {

                }
            }
        });



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvEvento.setLayoutManager(linearLayoutManager);
        new Carregar().execute();

        return view;
    }

    @Override
    public void onClickListener(View view, int position) {
        try {
            int codigoEvento = adapter.getCodigoEvento(position);
            Intent intent = new Intent(getActivity(), VisulizarEvento.class);
            intent.putExtra("codigoEvento", codigoEvento);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public class Carregar extends AsyncTask<Void,Integer,Void>
    {
        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog = ProgressDialog.show(getActivity(),"Carregando...",
                    "Carregando seus eventos, por favor aguarde...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                synchronized (this)
                {
                    objEvento = new Evento();
                    jsonString =  objEvento.carregarEventos(getString(R.string.padrao_evento),null);

                    eventos = new ArrayList<>();

                    try {
                        JSONArray jsonArray = new JSONArray(jsonString);
                        JSONObject jsonObject;

                        for (int i = 0 ; i < jsonArray.length(); i++)
                        {
                            jsonObject = new JSONObject(jsonArray.getString(i));
                            Evento evento = new Evento();
                            evento.setTituloEvento((jsonObject.getString("ds_titulo_evento")));
                            evento.setCodigoEvento((jsonObject.getInt("cd_evento")));
                            evento.setUrlFoto(getString(R.string.caminho_foto_capa_evento) + evento.getCodigoEvento() + ".png");
                            eventos.add(evento);
                            adapter = new EventoAdapter(getActivity(),eventos);
                            adapter.setRecyclerViewOnClickListenerHack(PainelEvento.this);



                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result)
        {
            progressDialog.dismiss();
            rvEvento.setAdapter(adapter);
        }

    }
}
