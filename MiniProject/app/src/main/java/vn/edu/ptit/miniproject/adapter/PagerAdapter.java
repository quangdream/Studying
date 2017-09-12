package vn.edu.ptit.miniproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import vn.edu.ptit.miniproject.fragment.FavoriteFragment;
import vn.edu.ptit.miniproject.fragment.NewsFragment;
import vn.edu.ptit.miniproject.fragment.SavedFragment;

/**
 * Created by QuangPC on 7/22/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    private NewsFragment newsFragment = new NewsFragment();
    private SavedFragment savedFragment = new SavedFragment();
    private FavoriteFragment favoriteFragment = new FavoriteFragment();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = newsFragment;
                break;
            case 1:
                fragment = savedFragment;
                break;
            case 2:
                fragment = favoriteFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "TIN TỨC";
            case 1:
                return "ĐÃ LƯU";
            case 2:
                return "YÊU THÍCH";
        }
        return super.getPageTitle(position);
    }

    public NewsFragment getNewsFragment() {
        return newsFragment;
    }

    public SavedFragment getSavedFragment() {
        return savedFragment;
    }

    public FavoriteFragment getFavoriteFragment() {
        return favoriteFragment;
    }
}
