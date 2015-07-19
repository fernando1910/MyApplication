package project.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class padraoConfiguracao extends ActionBarActivity {

    @Override
     public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(this,padraoMenu.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padrao_config);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       ListView lv = (ListView) findViewById(R.id.listView);
        List<ListViewItem> items = new ArrayList<ListViewItem>();
        items.add(new ListViewItem()
        {{
                          t = android.R.drawable.ic_menu_help   ;
                          Title = getString(R.string.item_config_ajuda);
                          //SubTitle = "Subtitle 1";

                      }}
        );

        items.add(new ListViewItem()
        {{
                         t = android.R.drawable.ic_menu_agenda;
                         Title = "Contatos";
                      }

                  }

        );

        items.add(new ListViewItem()
                  {{
                          t = android.R.drawable.ic_popup_reminder;
                          Title = getString(R.string.item_config_notifica);
                          //    SubTitle = "Subtitle 3";

                      }}
        );

        items.add(new ListViewItem()
                  {{
                          t = R.drawable.ic_perfil;
                          Title = getString(R.string.item_config_perfil);
                          //    SubTitle = "Subtitle 2";

                      }}
        );




        items.add(new ListViewItem()
                  {{
                          t = R.drawable.ic_about;
                          Title = getString(R.string.item_config_sobre);
                          //SubTitle = "Subtitle 1";

                      }}
        );


        CustomListViewAdapter adapter = new CustomListViewAdapter(this,items);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 4:
                        intent = new Intent(padraoConfiguracao.this,padraoSobre.class);
                        finish();
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(padraoConfiguracao.this,padraoPerfil.class);
                        finish();
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(padraoConfiguracao.this,padraoNotificacao.class);
                        finish();
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(padraoConfiguracao.this,padraoContatos.class);
                        finish();
                        startActivity(intent);
                        break;
                    case 0:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.padrao_ajuda)));
                        startActivity(intent);
                        break;

                }


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to  the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_padrao_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            this.finish();
            startActivity(new Intent(this,padraoMenu.class));
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    static class ListViewItem
    {
        public int t;
        public String Title;
        //public String SubTitle;
    }

}
