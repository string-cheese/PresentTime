package edu.byu.stringcheese.presenttime;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CardViewActivity extends Activity {

    TextView eventName;
    TextView eventDate;
    ImageView eventPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cardview_activity);
        eventName = (TextView)findViewById(R.id.event_name);
        eventDate = (TextView)findViewById(R.id.event_date);
        eventPhoto = (ImageView)findViewById(R.id.event_photo);

        eventName.setText("Sam's 25th Birthday!");
        eventDate.setText("December 17th, 2016");
        eventPhoto.setImageResource(R.drawable.balloon);
    }
}