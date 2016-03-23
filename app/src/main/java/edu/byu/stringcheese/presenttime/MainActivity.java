package edu.byu.stringcheese.presenttime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ViewAnimator;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.Map;

import edu.byu.stringcheese.presenttime.database.Event;
import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Item;
import edu.byu.stringcheese.presenttime.database.Profile;
import edu.byu.stringcheese.presenttime.database.Utils;

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static Profile myProfile;

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLogging();
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        if (getIntent().getStringExtra("email") != null && getIntent().getStringExtra("name") != null) {
            String email = getIntent().getStringExtra("email");
            String name = getIntent().getStringExtra("name");
            while(!FirebaseDatabase.hasInstance()){
                try {
                    Log.d(TAG, "waiting for database...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(FirebaseDatabase.hasInstance())
            {
                myProfile = Utils.getProfileByEmail(email);
                if (myProfile == null) {
                    myProfile = FirebaseDatabase.getInstance().addProfile(name, email);
                }
            }
        }
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {
        Log.i(TAG, "Ready");
    }


    //add an item to this client's proflie
    public void addEvent(View view) {
        Log.d(TAG, "trying to add an item");
        Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
        intent.putExtra("profileId", MainActivity.myProfile.getId());
        MainActivity.this.startActivity(intent);
        Log.d(TAG, "I started an activity");
        Log.d(TAG, intent.toString());
    }
}