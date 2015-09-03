package project.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
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

import domain.Usuario;

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
        Painel fragment = (Painel) getSupportFragmentManager().findFragmentByTag("tagInicial");
        if (fragment == null){
            fragment = new Painel();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.rlConteudo, fragment,"tagInicial");
            fragmentTransaction.commit();

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
                                    Fragment fragment0 = (Painel) getSupportFragmentManager().findFragmentByTag("tagInicial");
                                    if (fragment0 == null) {
                                        fragment0 = new Painel();
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.rlConteudo, fragment0, "tagInicial");
                                        fragmentTransaction.commit();

                                    }

                                case 1:
                                    startActivity(new Intent(getApplicationContext(), PainelMeusEventos.class));
                                    break;
                                case 2:
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
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Eventos").withIcon(getResources().getDrawable(R.drawable.star)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Calendario").withIcon(getResources().getDrawable(R.drawable.calendar_today)));
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

    private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
            Toast.makeText(MenuPrincipalNovo.this, "onCheckedChanged: "+( b ? "true" : "false" ), Toast.LENGTH_SHORT).show();
        }
    };


}
