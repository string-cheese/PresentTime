package edu.byu.stringcheese.presenttime;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import edu.byu.stringcheese.presenttime.database.Event;
import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Item;
import edu.byu.stringcheese.presenttime.database.Utils;

public class EventWishListActivity extends AppCompatActivity {

    public Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_wish_list);
        if(getIntent().getStringExtra("eventId") != null) {
            event = Utils.getEvent(getIntent().getStringExtra("eventId"));
            //((TextView)findViewById(R.id.selectedEvent)).setText(event.getName());
            recyclerView = (RecyclerView) findViewById(R.id.event_wish_list_rv);

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
    }
    private RecyclerView recyclerView;

    private void initializeAdapter(){
        ItemRVAdapter adapter = new ItemRVAdapter(Utils.getItems(event));
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
            itemViewHolder.itemId = itemsShown.get(i).getId();
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
                        Intent intent = new Intent(EventWishListActivity.this, ItemInfoActivity.class);
                        intent.putExtra("itemId", itemId);
                        EventWishListActivity.this.startActivity(intent);
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

            /*// Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }*/
        }
    }

}
