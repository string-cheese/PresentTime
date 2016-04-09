package edu.byu.stringcheese.presenttime.main.events.info;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.byu.stringcheese.presenttime.BitmapUtils;
import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Event;
import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Item;
import edu.byu.stringcheese.presenttime.main.events.info.item.ItemInfoActivity;
import edu.byu.stringcheese.presenttime.main.events.info.item.ItemSearchActivity;

public class EventInfoActivity extends AppCompatActivity implements Observer {
    public Event event;
    private int itemPosition;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /*setSupportActionBar(toolbar);*/
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
            event.removeItem(itemPosition);
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
        ItemRVAdapter adapter = new ItemRVAdapter(event.getItems());
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


    class ItemRVAdapter extends RecyclerView.Adapter<ItemRVAdapter.ItemViewHolder> {

        public List<Item> itemsShown;

        ItemRVAdapter(List<Item> items){
            this.itemsShown = items;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_event_item, viewGroup, false);//$$$
            ItemViewHolder itemViewHolder = new ItemViewHolder(v);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            itemViewHolder.itemName.setText(itemsShown.get(i).getName());
            itemViewHolder.itemPrice.setText("$" + String.valueOf(itemsShown.get(i).getCost()));
            //itemViewHolder.itemLocation.setText(itemsShown.get(i).getStore());
            itemViewHolder.itemImage.setImageDrawable(new BitmapDrawable(getResources(), BitmapUtils.decodeStringToBitmap(itemsShown.get(i).getEncodedImage())));
            itemViewHolder.currentItem = i;
            itemViewHolder.itemId = String.valueOf(itemsShown.get(i).getId());
            itemViewHolder.eventOwnerId = String.valueOf(itemsShown.get(i).getProfileId());
            itemViewHolder.eventId = String.valueOf(itemsShown.get(i).getEventId());
        }

        @Override
        public int getItemCount() {
            return itemsShown.size();
        }

        public void updateEventsShown(ArrayList<Item> items) {
            this.itemsShown.clear();
            this.itemsShown.addAll(items);
            notifyDataSetChanged();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

            CardView item_cv;
            TextView itemName;
            TextView itemPrice;
            //TextView itemLocation;
            ImageView itemImage;
            public int currentItem;
            public String itemId;
            public String eventId;
            public String eventOwnerId;

            ItemViewHolder(final View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(EventInfoActivity.this, ItemInfoActivity.class);
                        intent.putExtra("itemId", String.valueOf(itemId));
                        intent.putExtra("eventId", String.valueOf(eventId));
                        intent.putExtra("eventOwnerId", String.valueOf(eventOwnerId));
                        intent.putExtra("clientProfileId", getIntent().getStringExtra("clientProfileId"));
                        EventInfoActivity.this.startActivity(intent);
                    }
                });
                if(owner) {
                    itemView.setOnCreateContextMenuListener(this);
                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            itemPosition = currentItem;
                            return false;
                        }
                    });
                }
                item_cv = (CardView)itemView.findViewById(R.id.cv);
                itemName = (TextView)itemView.findViewById(R.id.item_name);
                itemPrice = (TextView)itemView.findViewById(R.id.item_price);
                itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            }
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle(itemName.getText().toString());
                menu.add(0, v.getId(), 0, "Delete Item");//groupId, itemId, order, title
            }
        }
    }
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;

        }
    }


    /**
     * This is a custom layout that prevents scrolling. We might want it in the future.
     */
    public class CustomGridLayoutManager extends GridLayoutManager {
        CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout){
            super(context, spanCount, orientation, reverseLayout);
        }

        // it will always pass false to RecyclerView when calling "canScrollVertically()" method.
        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }
}
