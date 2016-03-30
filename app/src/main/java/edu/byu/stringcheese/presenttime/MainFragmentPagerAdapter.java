package edu.byu.stringcheese.presenttime;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.Locale;

/**
 * Created by longl on 3/30/2016.
 */
class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 4;
    private Context context;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    /**
     * Get fragment corresponding to a specific position. This will be used to populate the
     * contents of the {@link ViewPager}.
     *
     * @param position Position to fetch fragment for.
     * @return Fragment for specified position.
     */

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putString("profileId",String.valueOf(((MainActivity)context).getIntent().getStringExtra("profileId")));
        switch (position) {
            case 0:
                fragment = new DashboardSectionFragment();
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new EventsSectionFragment();
                fragment.setArguments(bundle);
                break;
            case 2:
                fragment = new FriendsSectionFragment();
                fragment.setArguments(bundle);
                break;
            case 3:
                fragment = new ProfileSectionFragment();
                fragment.setArguments(bundle);
                break;
            default:
                fragment = new DashboardSectionFragment();
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }
    /**
     * Get number of pages the {@link ViewPager} should render.
     *
     * @return Number of fragments to be rendered as pages.
     */
    @Override
    public int getCount() {
        return NUM_PAGES;
    }
    /**
     * Get title for each of the pages. This will be displayed on each of the tabs.
     *
     * @param position Page to fetch title for.
     * @return Title for specified page.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return context.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return context.getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return context.getString(R.string.title_section3).toUpperCase(l);
            case 3:
                return context.getString(R.string.title_section4).toUpperCase(l);
            default:
                return context.getString(R.string.title_section1).toUpperCase(l);
        }
    }
}
