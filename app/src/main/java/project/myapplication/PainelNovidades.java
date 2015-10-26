package project.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class PainelNovidades extends Fragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_painel_novidades, container, false);
        LinearLayout llAlteracoes = (LinearLayout) view.findViewById(R.id.llAlteracoes);
        LinearLayout llComentarios = (LinearLayout) view.findViewById(R.id.llComentarios);
        LinearLayout llConvites = (LinearLayout) view.findViewById(R.id.llConvites);
        llComentarios.setOnClickListener(this);
        llAlteracoes.setOnClickListener(this);
        llConvites.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(),PainelEventosPadrao.class);
        switch (v.getId()){
            case R.id.llComentarios:
                intent.putExtra("fg_comentario", 1);
                break;
            case R.id.llAlteracoes:
                intent.putExtra("fg_comentario", 1);
                break;
            case R.id.llConvites:
                intent.putExtra("fg_convite", 1);
                break;
        }

        getActivity().finish();
        startActivity(intent);
    }
}
