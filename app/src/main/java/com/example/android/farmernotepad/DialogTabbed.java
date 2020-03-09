package com.example.android.farmernotepad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

public class DialogTabbed extends DialogFragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentColor fragmentColor;
    FragmentSort fragmentSort;
    FragmentView fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.dialog_tabbed_short_menu,container,false);
        fragmentColor = new FragmentColor();
        fragmentSort = new FragmentSort();
        fragmentView = new FragmentView();

        tabLayout = (TabLayout) rootview.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) rootview.findViewById(R.id.viewPager);

        CustomAdapter adapter = new CustomAdapter(getChildFragmentManager());
        adapter.addFragment("COLOR", fragmentColor);
        adapter.addFragment("SORT", fragmentSort);
        adapter.addFragment("VIEW", fragmentView);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return rootview;
    }
}