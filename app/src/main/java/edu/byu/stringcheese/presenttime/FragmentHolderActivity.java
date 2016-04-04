package edu.byu.stringcheese.presenttime;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import edu.byu.stringcheese.presenttime.main.events.EventsSectionFragment;

public class FragmentHolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);
        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey("class") && bundle.containsKey("clientProfileId") && bundle.containsKey("eventOwnerId"))
        {
            String myClass = bundle.getString("class");
            String eventOwnerId = bundle.getString("eventOwnerId");
            String profileId = bundle.getString("clientProfileId");
            Bundle args = new Bundle();
            args.putString("eventOwnerId", eventOwnerId);
            args.putString("clientProfileId", profileId);
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            if(myClass.equals("profile"))
            {
                ProfileSectionFragment profileFragment = new ProfileSectionFragment();
                profileFragment.setArguments(args);
                t.add(R.id.fragment_holder, profileFragment, "profileFragment");
                t.commit();
            }
            else if(myClass.equals("events"))
            {
                EventsSectionFragment eventsFragment = new EventsSectionFragment();
                eventsFragment.setArguments(args);
                t.add(R.id.fragment_holder, eventsFragment, "eventsFragment");
                t.commit();
            }
        }
    }

}
