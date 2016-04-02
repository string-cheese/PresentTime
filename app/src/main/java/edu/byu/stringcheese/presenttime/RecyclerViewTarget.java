package edu.byu.stringcheese.presenttime;

import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.amlcurran.showcaseview.targets.Target;

/**
 * Created by longl on 4/2/2016.
 */
public class RecyclerViewTarget implements Target {

    RecyclerView recyclerView;
    int id;

    public RecyclerViewTarget(RecyclerView recyclerView, int id) {
        this.recyclerView = recyclerView;
        this.id = id;
    }

    @Override
    public Point getPoint() {
        int[] location = new int[2];
        View childAt = recyclerView.getChildAt(0).findViewById(id);
        childAt.getLocationOnScreen(location);
        /*childAt.setTranslationX(location[0] + 21);
        childAt.setTranslationY(location[1] - 165);*/
        int x = location[0] + childAt.getWidth() / 2;
        int y = location[1] + childAt.getHeight() / 2;

        return new Point(x, y);
    }
}
