package project.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import adapters.TabsAdapterEvento;
import extras.SlidingTabLayout;

public class PainelTodosEventos extends Fragment {

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_painel_todos_eventos, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.vp_tabs);
        mViewPager.setAdapter( new TabsAdapterEvento(getActivity().getSupportFragmentManager(), getActivity().getApplicationContext()));
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.stl_tabs);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));

        mSlidingTabLayout.setViewPager(mViewPager);
        return view;
    }

}
