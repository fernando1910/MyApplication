package adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import project.myapplication.PainelNovidades;
import project.myapplication.VisualizarTopFestas;

public class TabsAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private String[] titles = {"INICIO", "TOP FESTAS"};



    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment mFragment = null;

            switch (position) {
                case 0:
                    mFragment = new PainelNovidades();
                    break;
                case 1:
                    mFragment = new VisualizarTopFestas();
                    break;
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
