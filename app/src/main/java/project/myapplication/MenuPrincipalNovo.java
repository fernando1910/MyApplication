package project.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

import adapters.TabsAdapter;
import domain.Usuario;
import extras.RoundImage;
import extras.SlidingTabLayout;

public class MenuPrincipalNovo extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_menu_principal_novo);
        setProgressBarIndeterminateVisibility(Boolean.TRUE);

        Usuario objUsuario = new Usuario();
        objUsuario.carregar(this);
        Bitmap bitmap = BitmapFactory.decodeByteArray(objUsuario.getImagemPerfil(), 0, objUsuario.getImagemPerfil().length);
        RoundImage roundImage = new RoundImage(bitmap);

        try {
            mToolbar = (Toolbar) findViewById(R.id.tb_main);
            mToolbar.setTitle("Inicial");
            setSupportActionBar(mToolbar);
        } catch (Exception e) {
            Toast.makeText(MenuPrincipalNovo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        mViewPager = (ViewPager) findViewById(R.id.vp_tabs);
        mViewPager.setAdapter( new TabsAdapter(getSupportFragmentManager(), this, 0));
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));

        mSlidingTabLayout.setViewPager(mViewPager);

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
                        startActivity(new Intent(MenuPrincipalNovo.this,VisualizarPerfil.class));
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
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {

                        try {

                            mViewPager = (ViewPager) findViewById(R.id.vp_tabs);
                            mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), MenuPrincipalNovo.this, i));
                            mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
                            mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));

                            mSlidingTabLayout.setViewPager(mViewPager);


                            /*
                            Fragment mFragment = null;
                            switch (i) {
                                case 0:


                                    break;
                                case 1:
                                    mFragment = new PainelEvento();
                                    break;
                                case 2:
                                    mFragment = new PainelCalendario();

                                    break;
                                case 6:
                                    mFragment = new PainelConfiguracaoNovo();
                                    break;

                            }

                            //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                           // ft.replace(R.id.layoutConteudo, mFragment, "tagMain");
                            //ft.commit();
*/
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

    private final OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
            Toast.makeText(MenuPrincipalNovo.this, "onCheckedChanged: " + (b ? "true" : "false"), Toast.LENGTH_SHORT).show();
        }
    };


}
