package edu.byu.stringcheese.presenttime.main.friends.info;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.main.events.EventsSectionFragment;

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
        if(getIntent().getStringExtra("profileId") != null)
        {
            //Create and add a fragment to frame layout created above.
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            EventsSectionFragment myFragment = new EventsSectionFragment();
            Bundle args = new Bundle();
            args.putString("profileId", getIntent().getStringExtra("profileId"));
            args.putString("eventOwnerId", getIntent().getStringExtra("eventOwnerId"));
            myFragment.setArguments(args);
            t.add(layout.getId(), myFragment, "myFirstFragment");
            t.commit();
        }
        else
        {
            Snackbar.make(this.getCurrentFocus(), "Something is wrong, this doesn't exist", Snackbar.LENGTH_LONG).show();
        }

    }

}
