package edu.byu.stringcheese.presenttime.main;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.firebase.client.Firebase;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseDrawer;
import com.github.amlcurran.showcaseview.ShowcaseView;

import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.RecyclerViewTarget;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Profile;
import edu.byu.stringcheese.presenttime.main.events.AddEventActivity;
import edu.byu.stringcheese.presenttime.main.friends.CircularImageView;

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
                                            ShowcaseView view = new ShowcaseView.Builder(MainActivity.this)
                                                    .setShowcaseDrawer(new FriendCardViewDrawer(recyclerView, getResources()))
                                                    .setStyle(R.style.CustomShowcaseTheme3)
                                                    .setTarget(new RecyclerViewTarget(recyclerView, R.id.friend_card))
                                                    .setContentTitle("Events")
                                                    .setContentText("Click here to view your friend's events")
                                                    .hideOnTouchOutside()
                                                    .build();
                                            view.show();
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
class FriendCardViewDrawer implements ShowcaseDrawer
{
    private int xoffset;
    private float height;
    private float width;
    private final float radius;
    private final Paint basicPaint;
    private final Paint eraserPaint;
    private int backgroundColor;

    public FriendCardViewDrawer(Resources resources) {
        this.radius = resources.getDimension(com.github.amlcurran.showcaseview.R.dimen.showcase_radius_material);
        this.eraserPaint = new Paint();
        this.eraserPaint.setColor(0xFFFFFF);
        this.eraserPaint.setAlpha(0);
        this.eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        this.eraserPaint.setAntiAlias(true);
        this.basicPaint = new Paint();
    }

    public FriendCardViewDrawer(RecyclerView view, Resources resources)
    {
        this(resources);
        CardView card = (CardView)view.getChildAt(0).findViewById(R.id.friend_card);
        CircularImageView imageView = (CircularImageView)view.getChildAt(0).findViewById(R.id.friend_image_circle);
        this.height = card.getHeight();
        this.width = card.getWidth()-imageView.getWidth();
        this.xoffset = imageView.getWidth()/2;
    }

    @Override
    public void setShowcaseColour(int color) {
        // no-op
    }

    @Override
    public void drawShowcase(Bitmap buffer, float x, float y, float scaleMultiplier) {
        Canvas bufferCanvas = new Canvas(buffer);
        //bufferCanvas.drawCircle(x, y, radius, eraserPaint);
        bufferCanvas.drawRect(x-width/2+xoffset,y-height/2,x+width/2+xoffset,y+height/2,eraserPaint);
    }

    @Override
    public int getShowcaseWidth() {
        //return (int) (radius * 2);
        return (int)width;
    }

    @Override
    public int getShowcaseHeight() {
        //return (int) (radius * 2);
        return (int)height;
    }

    @Override
    public float getBlockedRadius() {
        return radius;
    }

    @Override
    public void setBackgroundColour(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void erase(Bitmap bitmapBuffer) {
        bitmapBuffer.eraseColor(backgroundColor);
    }

    @Override
    public void drawToCanvas(Canvas canvas, Bitmap bitmapBuffer) {
        canvas.drawBitmap(bitmapBuffer, 0, 0, basicPaint);
    }
}