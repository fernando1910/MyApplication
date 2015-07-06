package project.myapplication;


import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;


public class MainActivity extends Activity {

    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;
    Button btAvancar;
    clsConfiguracoes objConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager)findViewById(R.id.myviewpager);
        myPagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(myPagerAdapter);

        btAvancar = (Button)findViewById(R.id.btAvancar);
        btAvancar.setVisibility(View.INVISIBLE);

        objConfig = null;
        ConfiguracoesDAO config_dao = new ConfiguracoesDAO(this.getApplicationContext());
        objConfig = config_dao.Carregar();

        switch (objConfig.getStatusPerfil())
        {
            case 1:
                startActivity(new Intent(this,padraoBoasVindas.class));
                break;
            case 2:
                startActivity(new Intent(this,padraoLogin.class));
                break;
            case 3:
                startActivity(new Intent(this,padraoMenu.class));
                break;
        }

    }

    /*public void onClick_Confirmar(View v)
    {
        startActivity(new Intent(this,padraoMenu.class));

    }

    public void onClick_TirarFoto(View v)
    {
        startActivity(new Intent(this,padraoLogin.class));
    }
    */
    private class MyPagerAdapter extends PagerAdapter{

        int NumberOfPages = 6;

        int[] res = {
                R.drawable.t01,
                R.drawable.t02,
                R.drawable.t03,
                R.drawable.t04,
                R.drawable.t05,
                R.drawable.t06
        };
        int[] backgroundcolor = {
                0xFF101010,
                0xFF202020,
                0xFF303030,
                0xFF404040,
                0xFF505050};

        @Override
        public int getCount() {
            return NumberOfPages;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            TextView textView = new TextView(MainActivity.this);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(30);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            //textView.setText(String.valueOf(position));

            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(res[position]);
            LayoutParams imageParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(imageParams);


            LinearLayout layout = new LinearLayout(MainActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            //layout.setBackgroundColor(backgroundcolor[position]);
            layout.setLayoutParams(layoutParams);
            layout.addView(textView);
            layout.addView(imageView);

            /* EVENTO PARA CLICAR NA VIEW
            final int page = position;
            layout.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,
                            "Page " + page + " clicked",
                            Toast.LENGTH_LONG).show();
                }});
            */

            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position == 3)
            {
                btAvancar.setVisibility(View.VISIBLE);
            }

            container.removeView((LinearLayout) object);
        }

    }

    public void onClick_Avancar(View v)
    {
        ConfiguracoesDAO config_dao = new ConfiguracoesDAO(this.getApplicationContext());
        objConfig = config_dao.Carregar();
        objConfig.setStatusPerfil(1);
        config_dao.Atualizar(objConfig);
        startActivity(new Intent(this,padraoBoasVindas.class));
    }

}
