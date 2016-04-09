package edu.byu.stringcheese.presenttime.main.events.info.item;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * This is a custom layout that prevents scrolling. We might want it in the future.
 */
public class CustomGridLayoutManager extends GridLayoutManager {
    public CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout){
        super(context, spanCount, orientation, reverseLayout);
    }

    // it will always pass false to RecyclerView when calling "canScrollVertically()" method.
    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
