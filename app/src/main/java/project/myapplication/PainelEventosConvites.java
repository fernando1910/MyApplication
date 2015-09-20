package project.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PainelEventosConvites extends Fragment {
    public static PainelEventosConvites newInstance() {
        PainelEventosConvites fragment = new PainelEventosConvites();
        return fragment;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_painel_eventos_convites, container, false);
    }


}
