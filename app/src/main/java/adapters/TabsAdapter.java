package adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import project.myapplication.PainelEvento;
import project.myapplication.PainelNovidades;

public class TabsAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String[] titles = {"INICIO" };
    private int positionMenu;


    public TabsAdapter(FragmentManager fm, Context context, int position) {
        super(fm);
        mContext = context;
        positionMenu = position;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment mFragment = null;

        if (positionMenu == 0) {
            switch (position) {
                case 0:
                    mFragment = new PainelNovidades();
                    break;
               /* case 1:
                    mFragment = new VisualizarTopFestas();
                    break;

                    */
            }
        }
        else if (positionMenu ==1){

            switch (position) {
                case 0:
                    mFragment = new PainelEvento();
                    break;

            }
        }

        return mFragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
