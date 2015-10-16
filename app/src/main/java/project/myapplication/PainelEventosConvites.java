package project.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import adapters.CustomListViewEvento;
import domain.Evento;


public class PainelEventosConvites extends Fragment {
    private ListView lvEventos;
    private CustomListViewEvento mAdapter;

    public static PainelEventosConvites newInstance() {
        return new PainelEventosConvites();
    }

    public PainelEventosConvites() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_painel_eventos_convites, container, false);

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
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }


}
