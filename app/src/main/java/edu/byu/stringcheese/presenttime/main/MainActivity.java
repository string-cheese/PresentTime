package edu.byu.stringcheese.presenttime.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.firebase.client.Firebase;

import edu.byu.stringcheese.presenttime.BitmapUtils;
import edu.byu.stringcheese.presenttime.FragmentHolderActivity;
import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Profile;
import edu.byu.stringcheese.presenttime.main.dashboard.DashboardSectionFragment;
import edu.byu.stringcheese.presenttime.main.events.AddEventActivity;
import edu.byu.stringcheese.presenttime.main.events.EventsSectionFragment;
import edu.byu.stringcheese.presenttime.main.friends.CircularImageView;
import edu.byu.stringcheese.presenttime.main.friends.FriendsSectionFragment;

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private Profile myProfile;
    private boolean firstTime = true;
    private String[] sectionTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawerLinearLayout;

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        Fragment fragment;
        Bundle args = new Bundle();
        args.putString("clientProfileId",String.valueOf((this).getIntent().getStringExtra("clientProfileId")));
        switch(position)
        {
            case 0:
                fragment = new DashboardSectionFragment();
                break;
            case 1:
                fragment = new EventsSectionFragment();
                break;
            case 2:
                fragment = new FriendsSectionFragment();
                break;
            default:
                fragment = new DashboardSectionFragment();
                break;
        }
        // Create a new fragment and specify the planet to show based on position
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(sectionTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerLinearLayout);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLinearLayout);
        //menu.findItem()
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        if (getIntent().getStringExtra("clientProfileId") != null) {
            String id = getIntent().getStringExtra("clientProfileId");
            myProfile = DBAccess.getProfile(id);

            if (savedInstanceState == null) {
                sectionTitles = getResources().getStringArray(R.array.nav_drawer_items);
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawerLinearLayout = (LinearLayout) findViewById(R.id.leftDrawer);
                mDrawerList = (ListView) findViewById(R.id.left_drawer);
                mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                        R.string.drawer_open, R.string.drawer_close) {

                    /** Called when a drawer has settled in a completely closed state. */
                    public void onDrawerClosed(View view) {
                        super.onDrawerClosed(view);
                        getSupportActionBar().setTitle(mTitle);
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }

                    /** Called when a drawer has settled in a completely open state. */
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        getSupportActionBar().setTitle(myProfile.getName());
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }
                };

                // Set the drawer toggle as the DrawerListener
                mDrawerLayout.setDrawerListener(mDrawerToggle);

                // Set the adapter for the list view
                mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                        R.layout.drawer_list_item, sectionTitles));
                // Set the list's click listener
                mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

                //selectItem(0);
                DashboardSectionFragment fragment = new DashboardSectionFragment();
                Bundle args = new Bundle();
                args.putString("clientProfileId",String.valueOf((this).getIntent().getStringExtra("clientProfileId")));
                fragment.setArguments(args);

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
                setTitle(sectionTitles[0]);
                ((CircularImageView)findViewById(R.id.imgProfilePic)).setImageBitmap(BitmapUtils.decodeStringToBitmap(myProfile.getEncodedProfileImage()));
                ((CircularImageView)findViewById(R.id.imgProfilePic)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, FragmentHolderActivity.class);
                        intent.putExtra("eventOwnerId",String.valueOf(myProfile.getId()));
                        intent.putExtra("clientProfileId",String.valueOf(myProfile.getId()));
                        intent.putExtra("class","profile");
                        startActivity(intent);
                    }
                });
            }
        }

    }


    //add an item to this client's proflie
    public void addEvent(View view) {
        Log.d(TAG, "trying to add an item");
        Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
        intent.putExtra("clientProfileId", String.valueOf(myProfile.getId()));
        MainActivity.this.startActivity(intent);
        Log.d(TAG, "I started an activity");
        Log.d(TAG, intent.toString());
    }
}
