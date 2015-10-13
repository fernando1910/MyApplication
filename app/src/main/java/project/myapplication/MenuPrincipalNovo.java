package project.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Date;

import domain.Configuracoes;
import domain.Usuario;
import extras.RoundImage;

public class MenuPrincipalNovo extends AppCompatActivity {
    private final String TAG = "LOG";
    private Toolbar mToolbar;

    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setContentView(R.layout.activity_menu_principal_novo);
            setProgressBarIndeterminateVisibility(Boolean.TRUE);

            Usuario objUsuario = new Usuario();
            objUsuario.carregar(this);
            Bitmap bitmap = null;
            if (objUsuario.getImagemPerfil() != null)
                bitmap = BitmapFactory.decodeByteArray(objUsuario.getImagemPerfil(), 0, objUsuario.getImagemPerfil().length);
            else
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);

            RoundImage roundImage = new RoundImage(bitmap);

            try {
                mToolbar = (Toolbar) findViewById(R.id.tb_main);
                mToolbar.setTitle("Inicial");
                setSupportActionBar(mToolbar);
            } catch (Exception e) {
                Toast.makeText(MenuPrincipalNovo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.layoutConteudo, Painel.newInstance(), "tagMain");
            ft.commit();


            AccountHeader.Result headerNavigationLeft = new AccountHeader()
                    .withActivity(this)
                    .withCompactStyle(false)
                    .withSavedInstance(savedInstanceState)
                    .withThreeSmallProfileImages(false)
                    .withHeaderBackground(R.drawable.batman)
                    .addProfiles(
                            new ProfileDrawerItem().withName(objUsuario.getNome()).withIcon(roundImage)
                    )
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                            startActivity(new Intent(MenuPrincipalNovo.this, VisualizarPerfil.class));
                            return false;
                        }
                    })
                    .build();

            Drawer.Result navigationDrawerLeft = new Drawer()
                    .withActivity(this)
                    .withToolbar(mToolbar)
                    .withDisplayBelowToolbar(true)
                    .withActionBarDrawerToggleAnimated(true)
                    .withDrawerGravity(Gravity.LEFT)
                    .withSavedInstance(savedInstanceState)
                    .withSelectedItem(0)
                    .withAccountHeader(headerNavigationLeft)
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l, IDrawerItem iDrawerItem) {

                            try {
                                Fragment mFragment = null;
                                switch (i) {
                                    case 0:
                                        mFragment = Painel.newInstance();
                                        setTitleActionBar("Início");
                                        break;
                                    case 1:
                                        mFragment = new PainelTodosEventos();
                                        setTitleActionBar("Convites");
                                        break;
                                    case 2:
                                        CaldroidFragment mFragmentCalendar = new CaldroidFragment();

                                        final CaldroidListener mCaldroidListener = new CaldroidListener() {
                                            @Override
                                            public void onSelectDate(Date date, View view) {
                                                Intent intent = new Intent(MenuPrincipalNovo.this, PainelEventosPadrao.class);
                                                intent.putExtra("mDataCalendario", date.toString());
                                                startActivity(intent);
                                            }
                                        };
                                        mFragmentCalendar.setCaldroidListener(mCaldroidListener);
                                        mFragment = mFragmentCalendar;
                                        setTitleActionBar("Calendário");
                                        break;
                                    case 3:
                                        startActivity(new Intent(MenuPrincipalNovo.this, PesquisarEvento.class));
                                        break;
                                    case 6:
                                        startActivity(new Intent(MenuPrincipalNovo.this, PainelConfiguracao.class));
                                        break;
                                }

                                if (mFragment != null) {
                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.layoutConteudo, mFragment, "tagMain");
                                    ft.commit();
                                }


                            } catch (Exception e) {
                                Toast.makeText(MenuPrincipalNovo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    })
                    .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                            return false;
                        }
                    })
                    .build();

            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Inicial").withIcon(getResources().getDrawable(R.drawable.home)));
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Eventos").withIcon(getResources().getDrawable(R.drawable.star)));
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Calendario").withIcon(getResources().getDrawable(R.drawable.calendar_today)));
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Pesquisar").withIcon(getResources().getDrawable(R.drawable.magnify)));
            navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Configurações"));
            navigationDrawerLeft.addItem(new SwitchDrawerItem().withName("Notificação").withChecked(true).withOnCheckedChangeListener(mOnCheckedChangeListener).withIcon(R.drawable.bell));
            navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Mais opções").withIcon(getResources().getDrawable(R.drawable.settings)));
        } catch (Exception ex) {
            Toast.makeText(MenuPrincipalNovo.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_principal_novo, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        try {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint("Pesquisar");

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (item.getItemId() == R.id.action_example) {
            startActivity(new Intent(this, CadEvento.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        hendleSearch(intent);
    }

    public void hendleSearch(Intent intent) {
        if (Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())) {
            String mQuery = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    private final OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
            Configuracoes objConfig = new Configuracoes();
            objConfig.carregar(getApplicationContext());
            objConfig.setPermitePush((b ? 1 : 0));
            objConfig.atualizar(getApplicationContext());
        }
    };

    public void   setTitleActionBar(String mTitle){
        try {
            getSupportActionBar().setTitle(mTitle);
        }catch (Exception ex){
            Log.i(TAG, ex.getMessage());
        }
    }

}
