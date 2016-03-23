package edu.byu.stringcheese.presenttime;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import edu.byu.stringcheese.presenttime.database.Event;
import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Item;
import edu.byu.stringcheese.presenttime.database.Utils;

public class EventInfoActivity extends AppCompatActivity {
    public Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        if(getIntent().getStringExtra("eventId") != null && getIntent().getStringExtra("profileId") != null)
        {
            event = Utils.getProfile(Integer.parseInt(getIntent().getStringExtra("profileId"))).getEvents().get(Integer.parseInt(getIntent().getStringExtra("eventId")));

            //((TextView)findViewById(R.id.selectedEvent)).setText(event.getName());
            recyclerView = (RecyclerView) findViewById(R.id.event_info_rv);

            /*LinearLayoutManager llm = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(llm);
            recyclerView.setHasFixedSize(true);*/
            int numberOfColumns = 2;
            //Check your orientation in your OnCreate
            if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
                numberOfColumns = 3;
            }
            GridLayoutManager man = new GridLayoutManager(this, numberOfColumns, GridLayoutManager.VERTICAL, false);
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

        //FLOATING ACTION BUTTON
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.event_info_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventInfoActivity.this, AddItemActivity.class);
                intent.putExtra("eventId", String.valueOf(event.getId()));
                intent.putExtra("profileId", String.valueOf(event.getProfileId()));
                startActivity(intent);
            }
        });
    }
    private RecyclerView recyclerView;

    private void initializeAdapter(){
        ItemRVAdapter adapter = new ItemRVAdapter(event.getItems());
        recyclerView.setAdapter(adapter);
    }
    class ItemRVAdapter extends RecyclerView.Adapter<ItemRVAdapter.ItemViewHolder> {

        List<Item> itemsShown;

        ItemRVAdapter(List<Item> items){
            this.itemsShown = items;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_item_template, viewGroup, false);//$$$
            ItemViewHolder itemViewHolder = new ItemViewHolder(v);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            itemViewHolder.itemName.setText(itemsShown.get(i).getName());
            itemViewHolder.itemPrice.setText("$"+String.valueOf(itemsShown.get(i).getCost()));
            //itemViewHolder.itemLocation.setText(itemsShown.get(i).getStore());
            itemViewHolder.itemImage.setBackgroundResource(itemsShown.get(i).getImageId());
            itemViewHolder.currentItem = i;
            itemViewHolder.itemId = String.valueOf(itemsShown.get(i).getId());
        }

        @Override
        public int getItemCount() {
            return itemsShown.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            CardView item_cv;
            TextView itemName;
            TextView itemPrice;
            //TextView itemLocation;
            RelativeLayout itemImage;
            public int currentItem;
            public String itemId;

            ItemViewHolder(final View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(EventInfoActivity.this, ItemInfoActivity.class);
                        intent.putExtra("itemId", itemId);
                        EventInfoActivity.this.startActivity(intent);
                    }
                });
                item_cv = (CardView)itemView.findViewById(R.id.cv);
                itemName = (TextView)itemView.findViewById(R.id.item_name);
                itemPrice = (TextView)itemView.findViewById(R.id.item_cost);
                itemImage = (RelativeLayout)itemView.findViewById(R.id.item_image);
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
