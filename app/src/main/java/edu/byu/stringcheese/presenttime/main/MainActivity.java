package edu.byu.stringcheese.presenttime.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawerLinearLayout;
    private String[] sectionTitles;

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                else
                    mDrawerLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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
                        getSupportActionBar().setTitle("Present-Time");
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }
                };
                mDrawerToggle.setDrawerIndicatorEnabled(true);

                // Set the drawer toggle as the DrawerListener
                mDrawerLayout.addDrawerListener(mDrawerToggle);
                // Set the adapter for the list view
                mDrawerList.setAdapter(new NavigationDrawerListAdapter(this, sectionTitles));
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
                ((TextView)findViewById(R.id.nav_drawer_profile_name)).setText(myProfile.getName());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
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
    class NavigationDrawerListAdapter extends BaseAdapter {

        private final LayoutInflater inflater;
        Context context;
        String[] data;
        int[] drawables;

        public NavigationDrawerListAdapter(Context context, String[] data) {
            this.context = context;
            this.data = data;
            inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            drawables = new int[]{R.drawable.dashboard,R.drawable.events,R.drawable.friends};
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.row, null);
            TextView text = (TextView) vi.findViewById(R.id.option_text);
            text.setText(data[position]);
            ImageView image = (ImageView) vi.findViewById(R.id.option_image);
            image.setImageResource(drawables[position]);
            return vi;
        }
    }
}

