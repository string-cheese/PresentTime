package edu.byu.stringcheese.presenttime;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Item;

/**
 * Created by liukaichi on 3/17/2016.
 */
public class ItemInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info);
        if(getIntent().getStringExtra("itemId") != null)
        {
            Item item = FirebaseDatabase.getInstance().getProfiles().get(Integer.parseInt(getIntent().getStringExtra("profileId"))).getEvents().get(Integer.parseInt(getIntent().getStringExtra("eventId"))).getItems().get(Integer.parseInt(getIntent().getStringExtra("itemId")));TextView itemName = (TextView)findViewById(R.id.item_name);
            itemName.setText(item.getName());
            TextView itemPrice = (TextView)findViewById(R.id.item_price);
            itemPrice.setText(String.valueOf(item.getCost()));
            TextView itemLocation = (TextView)findViewById(R.id.item_location);
            itemLocation.setText(item.getStore());
            ImageView itemImage = (ImageView)findViewById(R.id.item_image);
            itemImage.setImageResource(item.getImageId());
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



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
