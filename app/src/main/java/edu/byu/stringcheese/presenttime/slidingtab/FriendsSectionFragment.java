package edu.byu.stringcheese.presenttime.slidingtab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.byu.stringcheese.presenttime.R;

/**
 * Created by dtaylor on 3/20/2016.
 */
public class FriendsSectionFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends_section_fragment, container, false);
        /*TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
        dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));*/
        return rootView;
    }
}
