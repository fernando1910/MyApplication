package project.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import adapters.EventoAdapter;
import domain.Evento;
import interfaces.RecyclerViewOnClickListenerHack;


public class PainelMeusEvento extends Fragment implements RecyclerViewOnClickListenerHack {

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

    @Override
    public void onClickListener(View view, int position) {
       /* try {
            int codigoEvento = adapter.getCodigoEvento(position);
            Intent intent = new Intent(getActivity(), VisulizarEvento.class);
            intent.putExtra("codigoEvento", codigoEvento);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        */
    }
}
