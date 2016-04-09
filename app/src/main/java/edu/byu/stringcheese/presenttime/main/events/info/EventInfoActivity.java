package edu.byu.stringcheese.presenttime.main.events.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import edu.byu.stringcheese.presenttime.BitmapUtils;
import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Event;
import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Item;
import edu.byu.stringcheese.presenttime.main.events.info.item.CustomGridLayoutManager;
import edu.byu.stringcheese.presenttime.main.events.info.item.ItemRVAdapter;
import edu.byu.stringcheese.presenttime.main.events.info.item.ItemSearchActivity;
import edu.byu.stringcheese.presenttime.main.events.info.item.SpacesItemDecoration;

public class EventInfoActivity extends AppCompatActivity implements Observer {
    public Event event;
    private boolean owner;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.addObserver(this);
        setContentView(R.layout.activity_event_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if(getIntent().getStringExtra("eventOwnerId") != null && getIntent().getStringExtra("clientProfileId") != null)
        {

            recyclerView = (RecyclerView) findViewById(R.id.event_info_rv);
            registerForContextMenu(recyclerView);
            if (getIntent().hasExtra("eventOwnerId") &&
                    getIntent().getStringExtra("eventOwnerId").equals(getIntent().getStringExtra("clientProfileId")))
            {
                owner = true;
                initializeOwnerViews();
            } else
            {
                owner = false;
                initializeFriendViews();
            }
            event = DBAccess.getProfile(Integer.parseInt(getIntent().getStringExtra("eventOwnerId"))).getEvents().get(Integer.parseInt(getIntent().getStringExtra("eventId")));
            setTitle(event.getName());
            ImageView eventImage = (ImageView) findViewById(R.id.event_info_image);
            eventImage.setImageBitmap(BitmapUtils.decodeStringToBitmap(event.getEncodedImage()));


            int numberOfColumns = 2;
            //Check your orientation in your OnCreate
            if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
                numberOfColumns = 3;
            }
            CustomGridLayoutManager man = new CustomGridLayoutManager(recyclerView.getContext(), numberOfColumns, GridLayoutManager.VERTICAL, false);
            this.recyclerView.setLayoutManager(man);
            int spacingInPixels = Math.round(-10 * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
            recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
            initializeAdapter();
        }
        else
        {
            Snackbar.make(this.getCurrentFocus(), "Something is wrong, this doesn't exist", Snackbar.LENGTH_LONG)
                    .setAction("Go Back", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
        }
        TextView title = (TextView)findViewById(R.id.event_title_text_view);
        title.setText(event.getName());
        TextView address = (TextView) findViewById(R.id.address_text_view);
        address.setText(event.getLocation());


      
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Delete Item") {
            event.removeItem(((ItemRVAdapter)recyclerView.getAdapter()).getItemPosition());
            Toast.makeText(this, "Item Removed", Toast.LENGTH_SHORT).show();
        }
        else if (item.getTitle() == "Action 2") {
            Toast.makeText(this, "Action 2 invoked", Toast.LENGTH_SHORT).show();
        }
        else if (item.getTitle() == "Action 3") {
            Toast.makeText(this, "Action 3 invoked", Toast.LENGTH_SHORT).show();
        }
        else {
            return false;
        }
        return true;
    }
    private RecyclerView recyclerView;

    private void initializeAdapter(){
        ItemRVAdapter adapter = new ItemRVAdapter(this,owner,event.getItems());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void update(Observable observable, Object data) {

        if(recyclerView.getAdapter() != null)
        {
            if(recyclerView.getAdapter() != null)
            {
                ArrayList<Item> items = DBAccess.getItems(event);
                if(items!=null) {
                    ((ItemRVAdapter) recyclerView.getAdapter()).updateEventsShown(items);
                    recyclerView.invalidate();
                }
            }
        }
    }


    private void initializeFriendViews() {
        //FLOATING ACTION BUTTON
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.event_photo_fab);
        fab.setVisibility(View.INVISIBLE);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.add_item_fab);
        fab2.setVisibility(View.INVISIBLE);

    }

    private void initializeOwnerViews() {

        LinearLayout donateFundsLayout = (LinearLayout) findViewById(R.id.donate_button_layout);
        ((ViewGroup)donateFundsLayout.getParent()).removeView(donateFundsLayout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.event_photo_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"This should allow you to take a picture or get a photo from phone",Snackbar.LENGTH_LONG).show();
            }
        });
        FloatingActionButton addItemButton = (FloatingActionButton)findViewById(R.id.add_item_fab);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventInfoActivity.this, ItemSearchActivity.class);
                intent.putExtra("eventId", String.valueOf(event.getId()));
                intent.putExtra("clientProfileId", String.valueOf(event.getProfileId()));
                startActivity(intent);
            }
        });

    }
}
