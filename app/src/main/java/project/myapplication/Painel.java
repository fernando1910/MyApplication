package project.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import classes.Usuario;


/**
 * A simple {@link Fragment} subclass.
 */
public class Painel extends Fragment {

    private Usuario objUsuario;
    private ListView lvEventos;

    public Painel() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        try {
            objUsuario = new Usuario();
            objUsuario.carregar(getActivity().getApplicationContext());
        }catch (Exception e)
        {
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.title_home);
        view = inflater.inflate(R.layout.fragment_padrao_painel, container, false);
        lvEventos = (ListView) view.findViewById(R.id.lvEventos);

        return view ;
    }
}
