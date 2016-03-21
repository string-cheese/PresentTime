package edu.byu.stringcheese.presenttime;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class FriendInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_friend_info);
        //Create a frame layout, so we can add fragments to it.
        FrameLayout layout = new FrameLayout(this);
        layout.setId(R.id.friend_info_id);

        //Set the frame layout as the view container for this activity.
        setContentView(layout);

        //Create and add a fragment to frame layout created above.
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        EventsSectionFragment myFragment = new EventsSectionFragment();
        t.add(layout.getId(), myFragment, "myFirstFragment");
        t.commit();
    }

}
