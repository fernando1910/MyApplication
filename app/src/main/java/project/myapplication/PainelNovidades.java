package project.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class PainelNovidades extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_painel_novidades, container, false);
        LinearLayout llComentario = (LinearLayout) view.findViewById(R.id.llComentario);
        llComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),PainelEventosPadrao.class);
                intent.putExtra("fg_comentario", 1);
                getActivity().finish();
                startActivity(intent);

            }
        });

        return view;
    }


}
