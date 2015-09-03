package project.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapters.CustomListViewAdapter;
import domain.Configuracoes;


public class PainelConfiguracaoNovo extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paineil_configuracao_novo, container, false);
        ListView lv = (ListView) view.findViewById(R.id.lvConfiguracoes);
        List<Configuracoes.MenuConfiguracao> items = new ArrayList<>();
        items.add(new Configuracoes.MenuConfiguracao() {{
                      codigo = android.R.drawable.ic_menu_help;
                      Title = getString(R.string.item_config_ajuda);
                      //SubTitle = "Subtitle 1";

                  }}
        );

        items.add(new Configuracoes.MenuConfiguracao() {
                      {
                          codigo = android.R.drawable.ic_menu_agenda;
                          Title = "Contatos";
                      }
                  }
        );

        items.add(new Configuracoes.MenuConfiguracao() {{
                      codigo = android.R.drawable.ic_popup_reminder;
                      Title = getString(R.string.item_config_notifica);
                      //    SubTitle = "Subtitle 3";
                  }}
        );

        items.add(new Configuracoes.MenuConfiguracao() {{
                      codigo = R.drawable.ic_perfil;
                      Title = getString(R.string.item_config_perfil);
                      //    SubTitle = "Subtitle 2";
                  }}
        );


        items.add(new Configuracoes.MenuConfiguracao() {{
                      codigo = R.drawable.ic_about;
                      Title = getString(R.string.item_config_sobre);
                      //SubTitle = "Subtitle 1";
                  }}
        );


        CustomListViewAdapter adapter = new CustomListViewAdapter(getActivity(), items);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 4:
                        intent = new Intent(getActivity(), Sobre.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), VisualizarPerfil.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), CadConfiguracao.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), CadContato.class);
                        startActivity(intent);
                        break;
                    case 0:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.padrao_ajuda)));
                        startActivity(intent);
                        break;

                }

            }
        });


        return view;
    }

}
