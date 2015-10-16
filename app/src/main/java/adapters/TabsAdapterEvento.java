package adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import project.myapplication.PainelEventosConvites;
import project.myapplication.PainelMeusEvento;

public class TabsAdapterEvento extends FragmentStatePagerAdapter {
    private Context mContext;
    private String[] mTitles = {"Convites", "Meus Eventos"};

    public TabsAdapterEvento(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new PainelEventosConvites();
                break;
            case 1:
                fragment = new PainelMeusEvento();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
