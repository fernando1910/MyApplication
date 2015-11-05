package project.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import domain.Util;


public class PainelNovidades extends Fragment implements View.OnClickListener {
    private Util util;
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
        util = new Util();
        return view;
    }


    @Override
    public void onClick(View v) {
        if (util.verificaInternet(getContext())) {
            Intent intent = new Intent(getContext(), PainelEventosPadrao.class);
            switch (v.getId()) {
                case R.id.llComentarios:
                    intent.putExtra("fg_comentario", true);
                    break;
                case R.id.llAlteracoes:
                    intent.putExtra("fg_alteracoes", true);
                    break;
                case R.id.llConvites:
                    intent.putExtra("fg_convite", true);
                    break;
            }
            getActivity().finish();
            startActivity(intent);
        }
        else
        {
            Toast.makeText(PainelNovidades.this.getContext(), "Sem Internet", Toast.LENGTH_SHORT).show();

        }
    }
}
