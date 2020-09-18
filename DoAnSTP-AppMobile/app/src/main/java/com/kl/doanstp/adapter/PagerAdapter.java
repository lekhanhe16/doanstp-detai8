package com.kl.doanstp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kl.doanstp.view.AwayFragment;
import com.kl.doanstp.view.HostFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    int numbOfTabs;
    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numbOfTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HostFragment();
            case 1:
                return new AwayFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Cac tran da tao";
            case 1:
                return "Cac tran duoc moi";
            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        return numbOfTabs;
    }
}
