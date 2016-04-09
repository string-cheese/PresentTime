package edu.byu.stringcheese.presenttime.main.events.info.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.byu.stringcheese.presenttime.BitmapUtils;
import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Item;
import edu.byu.stringcheese.presenttime.main.events.info.EventInfoActivity;

/**
 * Created by dtaylor on 4/9/2016.
 */
public class ItemRVAdapter extends RecyclerView.Adapter<ItemRVAdapter.ItemViewHolder> {

    private final Context context;
    private final boolean isOwner;
    public List<Item> itemsShown;
    private int itemPosition;

    public ItemRVAdapter(Context context, boolean isOwner, List<Item> items){
        this.itemsShown = items;
        this.context = context;
        this.isOwner = isOwner;
    }

    public int getItemPosition()
    {
        return itemPosition;
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
        itemViewHolder.itemImage.setImageDrawable(new BitmapDrawable(context.getResources(), BitmapUtils.decodeStringToBitmap(itemsShown.get(i).getEncodedImage())));
        itemViewHolder.currentItem = i;
        itemViewHolder.itemId = String.valueOf(itemsShown.get(i).getId());
        itemViewHolder.eventOwnerId = String.valueOf(itemsShown.get(i).getProfileId());
        itemViewHolder.eventId = String.valueOf(itemsShown.get(i).getEventId());
        itemViewHolder.itemStore.setText(itemsShown.get(i).getStore());
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
        TextView itemStore;
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
                    if(context instanceof EventInfoActivity) {
                        Intent intent = new Intent(context, ItemInfoActivity.class);
                        intent.putExtra("itemId", String.valueOf(itemId));
                        intent.putExtra("eventId", String.valueOf(eventId));
                        intent.putExtra("eventOwnerId", String.valueOf(eventOwnerId));
                        intent.putExtra("clientProfileId", ((Activity) context).getIntent().getStringExtra("clientProfileId"));
                        context.startActivity(intent);
                    }
                    else if(context instanceof ItemSearchActivity)
                    {

                        String profileId = ((Activity)context).getIntent().getStringExtra("clientProfileId");
                        String eventId = ((Activity)context).getIntent().getStringExtra("eventId");
                        String img = itemsShown.get(currentItem).getEncodedImage();
                        DBAccess.getEvent(profileId, eventId).addItem(itemName.getText().toString(), Double.parseDouble(itemPrice.getText().toString().replace("$","")), itemStore.getText().toString(), img, false);
                        ((Activity)context).finish();
                    }
                }
            });
            if(isOwner) {
                if(context instanceof EventInfoActivity)
                {
                    itemView.setOnCreateContextMenuListener(this);
                    itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            itemPosition = currentItem;
                            return false;
                        }
                    });
                }
            }
            item_cv = (CardView)itemView.findViewById(R.id.cv);
            itemName = (TextView)itemView.findViewById(R.id.item_name);
            itemPrice = (TextView)itemView.findViewById(R.id.item_price);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemStore = (TextView)itemView.findViewById(R.id.search_item_store);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(itemName.getText().toString());
            menu.add(0, v.getId(), 0, "Delete Item");//groupId, itemId, order, title
        }
    }

}


