package project.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        ImageButton ibConvites = (ImageButton) view.findViewById(R.id.ibConvites);
        ImageButton ibAlteracoes = (ImageButton) view.findViewById(R.id.ibAlteracoes);
        ImageButton ibComentarios = (ImageButton) view.findViewById(R.id.ibComentarios);


        llComentarios.setOnClickListener(this);
        llAlteracoes.setOnClickListener(this);
        llConvites.setOnClickListener(this);
        ibConvites.setOnClickListener(this);
        ibAlteracoes.setOnClickListener(this);
        ibComentarios.setOnClickListener(this);
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
                case R.id.ibComentarios:
                    intent.putExtra("fg_comentario", true);
                    break;
                case R.id.llAlteracoes:
                    intent.putExtra("fg_alteracoes", true);
                    break;
                case R.id.ibAlteracoes:
                    intent.putExtra("fg_alteracoes", true);
                    break;
                case R.id.llConvites:
                    intent.putExtra("fg_convite", true);
                    break;
                case R.id.ibConvites:
                    intent.putExtra("fg_convite", true);
                    break;
            }
            getActivity().finish();
            startActivity(intent);
        }
        else
        {
            Toast.makeText(PainelNovidades.this.getContext(), R.string.sem_internet, Toast.LENGTH_SHORT).show();
        }
    }
}
