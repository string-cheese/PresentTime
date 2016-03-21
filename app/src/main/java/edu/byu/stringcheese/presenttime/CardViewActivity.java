package edu.byu.stringcheese.presenttime;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CardViewActivity extends Fragment {

    TextView eventName;
    TextView eventDate;
    ImageView eventPhoto;
    /** The CardView widget. */
    //@VisibleForTesting
    CardView mCardView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.cardview_activity, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.cv);
        eventName = (TextView)view.findViewById(R.id.event_name);
        eventDate = (TextView)view.findViewById(R.id.event_date);
        eventPhoto = (ImageView)view.findViewById(R.id.event_photo);

        eventName.setText("Sam's 25th Birthday!");
        eventDate.setText("December 17th, 2016");
        eventPhoto.setImageResource(R.drawable.balloon);
    }
}