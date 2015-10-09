package project.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.CustomListViewAdapter;
import domain.Configuracoes;

public class PainelConfiguracao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setContentView(R.layout.activity_painel_configuracao);

            ListView lv = (ListView) findViewById(R.id.lvConfiguracoes);
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


            CustomListViewAdapter adapter = new CustomListViewAdapter(this, items);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent;
                    switch (position) {
                        case 4:
                            intent = new Intent(PainelConfiguracao.this, Sobre.class);
                            startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(PainelConfiguracao.this, VisualizarPerfil.class);
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(PainelConfiguracao.this, CadConfiguracao.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(PainelConfiguracao.this, CadContato.class);
                            startActivity(intent);
                            break;
                        case 0:
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.padrao_ajuda)));
                            startActivity(intent);
                            break;

                    }

                }
            });

        }catch (Exception e){
            Toast.makeText(PainelConfiguracao.this, "Erro", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
