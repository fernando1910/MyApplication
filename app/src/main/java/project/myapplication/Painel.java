package project.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import adapters.TabsAdapter;
import domain.Usuario;
import extras.SlidingTabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class Painel extends Fragment {

    private Usuario objUsuario;
    private ListView lvEventos;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Painel newInstance() {
        Painel mFragment = new Painel();
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;


        getActivity().setTitle(R.string.title_home);
        view = inflater.inflate(R.layout.fragment_padrao_painel, container, false);


        mViewPager = (ViewPager) view.findViewById(R.id.vp_tabs);
        mViewPager.setAdapter( new TabsAdapter(getActivity().getSupportFragmentManager(), getActivity().getApplicationContext()));
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.stl_tabs);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));

        mSlidingTabLayout.setViewPager(mViewPager);

        return view ;

    }
}
