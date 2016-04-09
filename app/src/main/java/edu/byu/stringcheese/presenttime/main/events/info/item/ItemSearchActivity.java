package edu.byu.stringcheese.presenttime.main.events.info.item;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.Utils;
import edu.byu.stringcheese.presenttime.database.Item;

public class ItemSearchActivity extends AppCompatActivity {
    // Search EditText
    EditText inputSearch;
    public static int max_items = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    search(v.getText().toString());
                    return false;
            }
        });

        // Adding items to listview
        recyclerView = (RecyclerView) findViewById(R.id.item_search_recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(false);
        recyclerView.setVisibility(View.GONE);
        //findViewById(R.id.search_progress).setVisibility(View.GONE);
        initializeAdapter();
    }

    private RecyclerView recyclerView;
    private void initializeAdapter(){
        int numberOfColumns = 2;
        //Check your orientation in your OnCreate
        if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            numberOfColumns = 3;
        }
        CustomGridLayoutManager man = new CustomGridLayoutManager(recyclerView.getContext(), numberOfColumns, GridLayoutManager.VERTICAL, false);
        this.recyclerView.setLayoutManager(man);
        int spacingInPixels = Math.round(-10 * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        ArrayList<Item> searchItems = new ArrayList<>();
        if(searchItems != null) {
            ItemRVAdapter adapter = new ItemRVAdapter(this,true,searchItems);
            recyclerView.setAdapter(adapter);
        }
    }

    public void update(JSONObject searchResult) {
        if(recyclerView.getAdapter() != null)
        {
            try {
                ArrayList<Item> items = new ArrayList<>();
                JSONArray results = searchResult.getJSONArray("results");
                for (int i = 0; i < results.length(); i++)
                {
                    JSONObject item = results.getJSONObject(i);
                    if(item.has("upc") && item.has("name") && item.has("price") && item.has("img")) {
                        String name = item.getString("name");
                        double cost = Double.parseDouble(item.getString("price"));
                        String store;
                        if (item.getJSONArray("sitedetails").length() != 0)
                            store = item.getJSONArray("sitedetails").getJSONObject(0).getString("name");
                        else
                            store = "Manufacturer";
                        String img = item.getString("img");
                        items.add(new Item(name, cost, store, img, -1, -1, -1, false));
                    }
                }
                if (items != null) {
                    ((ItemRVAdapter) recyclerView.getAdapter()).updateEventsShown(items);
                    recyclerView.invalidate();
                }
            }
            catch(Exception e)
            {
                Log.e("ItemSearchActivity", "Failed to parse json result", e);
            }
        }
    }

    /*class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.EventViewHolder> {

        public List<Item> searchItemsShown;
        SearchItemAdapter(List<Item> items){
            this.searchItemsShown = items;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_search_item, viewGroup, false);
            EventViewHolder eventViewHolder = new EventViewHolder(v);
            return eventViewHolder;
        }

        @Override
        public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
            if(searchItemsShown.get(i) != null)
            {
                eventViewHolder.itemName.setText(searchItemsShown.get(i).getName());
                eventViewHolder.itemCost.setText(String.valueOf(searchItemsShown.get(i).getCost()));
                eventViewHolder.itemImage.setBackground(new BitmapDrawable(getResources(), BitmapUtils.decodeStringToBitmap(searchItemsShown.get(i).getEncodedImage())));
                eventViewHolder.itemStore.setText(searchItemsShown.get(i).getStore());
                eventViewHolder.selectedItem = i;
            }
        }

        @Override
        public int getItemCount() {
            return searchItemsShown.size();
        }

        public void updateEventsShown(ArrayList<Item> items) {
            this.searchItemsShown.clear();
            this.searchItemsShown.addAll(items);
            notifyDataSetChanged();
        }

        public class EventViewHolder extends RecyclerView.ViewHolder {

            CardView cv;
            TextView itemName;
            TextView itemCost;
            RelativeLayout itemImage;
            TextView itemStore;
            public int selectedItem;

            EventViewHolder(final View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String profileId = getIntent().getStringExtra("clientProfileId");
                        String eventId = getIntent().getStringExtra("eventId");
                        String img = searchItemsShown.get(selectedItem).getEncodedImage();
                        DBAccess.getEvent(profileId, eventId).addItem(itemName.getText().toString(), Double.parseDouble(itemCost.getText().toString()), itemStore.getText().toString(), img, false);
                        finish();
                    }
                });
                cv = (CardView)itemView.findViewById(R.id.cv);
                itemName = (TextView)itemView.findViewById(R.id.search_item_name);
                itemCost = (TextView)itemView.findViewById(R.id.search_item_cost);
                itemImage = (RelativeLayout)itemView.findViewById(R.id.search_item_image);
                itemStore = (TextView)itemView.findViewById(R.id.search_item_store);
            }
        }
    }*/

    private void search(String content) {
        findViewById(R.id.item_search_recyclerview).setVisibility(View.GONE);
        findViewById(R.id.search_progress).setVisibility(View.VISIBLE);
        Utils.searchItemAsync(content, max_items, new ItemSearchListener() {

            @Override
            public void onSearchComplete(JSONObject jsonObject) {
                update(jsonObject);
                findViewById(R.id.search_progress).setVisibility(View.GONE);
                findViewById(R.id.item_search_recyclerview).setVisibility(View.VISIBLE);
            }
        });
    }
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
}

