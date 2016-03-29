package edu.byu.stringcheese.presenttime;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by dtaylor on 3/29/2016.
 */
public class ScollingTextView extends TextView {
    public ScollingTextView(Context context) {
        super(context);
    }

    public ScollingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScollingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScollingTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isSelected() {
        return true;
    }
}
