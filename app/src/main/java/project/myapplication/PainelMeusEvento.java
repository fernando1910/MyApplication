package project.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import domain.Evento;


public class PainelMeusEvento extends Fragment  {

    private Evento objEvento;
    private ProgressDialog progressDialog;
    private String jsonString;
    private List<Evento> eventos;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_painel_evento, container, false);
        objEvento = new Evento();
/*
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
        eventos = objEvento.selecionarTodosEventosLocal(getContext());
        for (Evento objEvento: eventos) {
            objEvento.setUrlFoto(getActivity().getString(R.string.caminho_foto_capa_evento) + objEvento.getCodigoEvento() +".png");
        }
        adapter =  new EventoAdapter(getActivity(), eventos);
        adapter.setRecyclerViewOnClickListenerHack(PainelMeusEvento.this);
        rvEvento.setAdapter(adapter);
*/
        return view;
    }

}
