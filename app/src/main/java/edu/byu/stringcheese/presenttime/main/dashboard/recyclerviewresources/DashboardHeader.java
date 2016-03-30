package edu.byu.stringcheese.presenttime.main.dashboard.recyclerviewresources;

import android.util.Log;

/**
 * Created by liukaichi on 3/24/2016.
 */
public class DashboardHeader extends AbstractDashboardItem {


    private static final String TAG = "DashboardHeader";
    public String label;

    public DashboardHeader(String label) {
        this.label = label;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

    @Override
    public String getName() {
        Log.e(TAG, "Bad request of AbstractDashboardItem made!");
        return "";
    }

    @Override
    public String getDateAsString() {
        Log.e(TAG, "Bad request of AbstractDashboardItem made!");
        return "";
    }

    @Override
    public String getEncodedImage() {
        Log.e(TAG, "Bad request of AbstractDashboardItem made!");
        return "";
    }

    @Override
    public int getProfileId() {
        Log.e(TAG, "Bad request of AbstractDashboardItem made!");
        return 0;
    }

    @Override
    public int getId() {
        Log.e(TAG, "Bad request of AbstractDashboardItem made!");
        return 0;
    }

    public String getLabel() {
        return label;
    }
}
