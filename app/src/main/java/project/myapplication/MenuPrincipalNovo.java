package project.myapplication;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import classes.Usuario;

public class MenuPrincipalNovo extends AppCompatActivity {
    private Toolbar mToolbar;
    private Drawer.Result navigationDrawerLeft;
    private Drawer.Result navigationDrawerRight;
    private AccountHeader.Result headerNavigationLeft;
    private Usuario objUsuario;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_novo);

        objUsuario = new Usuario();
        objUsuario.carregar(this);

        try {
            mToolbar = (Toolbar) findViewById(R.id.tb_main);
            mToolbar.setTitle("Inicial");
            // ATENÇÃO ACERTAR LOGO
            //mToolbar.setLogo(R.drawable.ic_calendar1);
            setSupportActionBar(mToolbar);
        }catch (Exception e){
            Toast.makeText(MenuPrincipalNovo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        headerNavigationLeft = new AccountHeader ()
                .withActivity(this)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(false)
                .withHeaderBackground(R.drawable.batman)
                .addProfiles(
                        new ProfileDrawerItem().withName(objUsuario.getNome()).withEmail("danilo.santos@gmail.com.br").withIcon(getResources().getDrawable(R.drawable.rosto))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                        return false;
                    }
                })
                .build();

        navigationDrawerLeft  = new Drawer()
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
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        try {
                            switch (i) {
                                case 0:
                                    Painel fragment0 = (Painel) getSupportFragmentManager().findFragmentByTag("tagInicial");
                                    if (fragment0 == null) {
                                        fragment0 = new Painel();
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.rlConteudo, fragment0, "tagInicial");
                                        fragmentTransaction.commit();
                                    }
                                    break;

                                case 1:
                                    PainelEvento fragment1 = (PainelEvento) getSupportFragmentManager().findFragmentByTag("tagEvento");
                                    if (fragment1 == null) {
                                        fragment1 = new PainelEvento();
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.rlConteudo, fragment1, "tagEvento");
                                        fragmentTransaction.commit();
                                    }
                                    break;
                                case 2:
                                    //startActivity(new Intent(getApplicationContext(), PainelMeusEventos.class));
                                case 6:
                                    PainelConfiguracaoNovo fragment2 = (PainelConfiguracaoNovo) getSupportFragmentManager().findFragmentByTag("tagConfig");
                                    if (fragment2 == null) {
                                        fragment2 = new PainelConfiguracaoNovo();
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.rlConteudo, fragment2, "tagConfig");
                                        fragmentTransaction.commit();

                                    }
                                    break;


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
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Eventos").withIcon(getResources().getDrawable(android.R.drawable.btn_star)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Configurações").withIcon(getResources().getDrawable(R.drawable.settings)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Eventos").withIcon(getResources().getDrawable(R.drawable.star)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Calendario").withIcon(getResources().getDrawable(R.drawable.calendar_today)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Pesquisar").withIcon(getResources().getDrawable(R.drawable.magnify)));
        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Configurações"));
        navigationDrawerLeft.addItem(new SwitchDrawerItem().withName("Notificação").withChecked(true).withOnCheckedChangeListener(mOnCheckedChangeListener).withIcon(R.drawable.bell));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Mais opções").withIcon(getResources().getDrawable(R.drawable.settings)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_principal_novo, menu);
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
            startActivity(new Intent(this,CadEvento.class));
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


}