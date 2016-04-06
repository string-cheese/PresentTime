package edu.byu.stringcheese.presenttime;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import edu.byu.stringcheese.presenttime.database.DBAccess;
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
                this.setTitle(DBAccess.getProfile(eventOwnerId).getName()+"'s Profile");
                ProfileSectionFragment profileFragment = new ProfileSectionFragment();
                profileFragment.setArguments(args);
                t.add(R.id.fragment_holder, profileFragment, "profileFragment");
                t.commit();
            }
            else if(myClass.equals("events"))
            {
                this.setTitle(DBAccess.getProfile(eventOwnerId).getName()+"'s Events");
                EventsSectionFragment eventsFragment = new EventsSectionFragment();
                eventsFragment.setArguments(args);
                t.add(R.id.fragment_holder, eventsFragment, "eventsFragment");
                t.commit();
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        /*Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);*/
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

}
