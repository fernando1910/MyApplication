package project.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import adapters.CustomListViewEvento;
import domain.Evento;


public class PainelMeusEvento extends Fragment  {
    private CustomListViewEvento mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_painel_evento, container, false);
        getActivity().setTitle("Meus Eventos");

        ListView lvEventos;
        String TAG = "LOG";
        try {
            lvEventos = (ListView) view.findViewById(R.id.lvEventos);
            Evento objEvento = new Evento();
            List<Evento> mEventos = objEvento.selecionarTodosEventosLocal(getContext());
            mAdapter = new CustomListViewEvento(getContext(), mEventos);
            lvEventos.setAdapter(mAdapter);
            lvEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int codigoEvento = mAdapter.getCodigoEvento(position);
                    Intent intent = new Intent(getContext(),VisualizarEvento.class);
                    intent.putExtra("codigoEvento", codigoEvento);
                    startActivity(intent);

                }
            });

        }catch (Exception ex){
            Log.i(TAG, ex.getMessage());
        }

        return view;
    }

}
