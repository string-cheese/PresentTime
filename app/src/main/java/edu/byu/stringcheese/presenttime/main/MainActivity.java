package edu.byu.stringcheese.presenttime.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.firebase.client.Firebase;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;

import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.RecyclerViewTarget;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Profile;
import edu.byu.stringcheese.presenttime.main.events.AddEventActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        if (getIntent().getStringExtra("profileId") != null) {
            String id = getIntent().getStringExtra("profileId");
            myProfile = DBAccess.getProfile(id);

            if (savedInstanceState == null) {

                final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(),
                        MainActivity.this));
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (state == ViewPager.SCROLL_STATE_IDLE && viewPager.getCurrentItem() == 2) {

                            final RecyclerView recyclerView = (RecyclerView) ((MainFragmentPagerAdapter) viewPager.getAdapter()).getItem(viewPager.getCurrentItem()).getView().findViewById(R.id.friends_rv);
                            new ShowcaseView.Builder(MainActivity.this)
                                    .withMaterialShowcase()
                                    .setStyle(R.style.CustomShowcaseTheme2)
                                    .setTarget(new RecyclerViewTarget(recyclerView, R.id.friend_image_circle))
                                    .setContentTitle("Profile")
                                    .setContentText("Click here to view your friends profile.")
                                    .hideOnTouchOutside()
                                    .setShowcaseEventListener(new OnShowcaseEventListener() {
                                        @Override
                                        public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                            new ShowcaseView.Builder(MainActivity.this)
                                                    .withMaterialShowcase()
                                                    .setStyle(R.style.CustomShowcaseTheme3)
                                                    .setTarget(new RecyclerViewTarget(recyclerView, R.id.friend_card))
                                                    .setContentTitle("Events")
                                                    .setContentText("Click here to view your friend's events")
                                                    .hideOnTouchOutside()
                                                    .build().show();
                                        }

                                        @Override
                                        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                                        }

                                        @Override
                                        public void onShowcaseViewShow(ShowcaseView showcaseView) {

                                        }

                                        @Override
                                        public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                                        }
                                    }).build().show();
                        }
                    }
                });

                // Give the TabLayout the ViewPager
                TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
                tabLayout.setupWithViewPager(viewPager);
            }
        }

    }


    //add an item to this client's proflie
    public void addEvent(View view) {
        Log.d(TAG, "trying to add an item");
        Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
        intent.putExtra("profileId", String.valueOf(myProfile.getId()));
        MainActivity.this.startActivity(intent);
        Log.d(TAG, "I started an activity");
        Log.d(TAG, intent.toString());
    }
}