package edu.byu.stringcheese.presenttime.main.events.info.item;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import edu.byu.stringcheese.presenttime.BitmapUtils;
import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Item;

/**
 * Created by liukaichi on 3/17/2016.
 */
public class ItemInfoActivity extends AppCompatActivity {
    Item thisItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        if(getIntent().getStringExtra("itemId") != null && getIntent().getStringExtra("eventOwnerId") != null && getIntent().getStringExtra("eventId") != null)
        {
            thisItem = DBAccess.getProfiles().get(
                    Integer.parseInt(getIntent().getStringExtra("eventOwnerId"))).getEvents().get(
                    Integer.parseInt(getIntent().getStringExtra("eventId"))).getItems().get(
                    Integer.parseInt(getIntent().getStringExtra("itemId")));
            setTitle(thisItem.getName());
            TextView itemName = (TextView)findViewById(R.id.item_name);
            itemName.setText(thisItem.getName());
            TextView itemPrice = (TextView)findViewById(R.id.item_price);
            itemPrice.setText(String.valueOf(thisItem.getCost()));
            TextView itemLocation = (TextView)findViewById(R.id.item_location);
            itemLocation.setText(thisItem.getStore());
            ImageView itemImage = (ImageView)findViewById(R.id.item_info_image);
            itemImage.setImageBitmap(BitmapUtils.decodeStringToBitmap(thisItem.getEncodedImage()));

            if (getIntent().hasExtra("eventOwnerId") &&
                    getIntent().getStringExtra("eventOwnerId").equals(getIntent().getStringExtra("clientProfileId")))
            {
                initializeOwnerViews();
            } else
            {
                initializeFriendViews();
            }
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

    private void initializeFriendViews() {
        //FLOATING ACTION BUTTON
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.item_info_fab);
        ((ViewGroup)fab.getParent()).removeView(fab);

//        RelativeLayout purchasedStateLayout = (RelativeLayout) findViewById(R.id.purchased_state_layout);
//        ((ViewGroup)purchasedStateLayout.getParent()).removeView(purchasedStateLayout);

        TextView editItemText = (TextView) findViewById(R.id.edit_item_text);
        ((ViewGroup)editItemText.getParent()).removeView(editItemText);
        final Button fundItemButton = (Button) findViewById(R.id.fund_item_button);
        fundItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ItemInfoActivity.this);
                builder.setMessage("How much would you like to fund?");
                LayoutInflater inflater = ItemInfoActivity.this.getLayoutInflater();
                final View layout = inflater.inflate(R.layout.dialog_fund, null);
                builder.setView(layout);
                builder.setPositiveButton("Fund!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        thisItem.updateAmountFunded(thisItem.getAmount_funded()+(int)Double.parseDouble(((EditText)layout.findViewById(R.id.dialog_fund_amount)).getText().toString()));
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        final Button buyItemButton = (Button) findViewById(R.id.buy_item_button);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        buyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Are you sure you want to buy this item?");
                builder.setPositiveButton("Buy!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        thisItem.updatePurchased(true);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void initializeOwnerViews() {

        Button buyItemButton = (Button) findViewById(R.id.buy_item_button);
        ((ViewGroup)buyItemButton.getParent()).removeView(buyItemButton);


        Button fundItemButton = (Button) findViewById(R.id.fund_item_button);
        ((ViewGroup)fundItemButton.getParent()).removeView(fundItemButton);

        //EDIT ITEM TEXT
        TextView editItemText = (TextView) findViewById(R.id.edit_item_text);
        ((ViewGroup)editItemText.getParent()).removeView(editItemText);

        //FLOATING ACTION BUTTON
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.item_info_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This should allow you to take a picture or get a photo from phone", Snackbar.LENGTH_LONG).show();
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
