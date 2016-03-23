package edu.byu.stringcheese.presenttime;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by liukaichi on 3/17/2016.
 */
public class ItemInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info);
        Database.Item item = Database.getInstance().getItem((int) getIntent().getExtras().get("itemId"));
        TextView itemName = (TextView)findViewById(R.id.item_name);
        itemName.setText(item.getItemName());
        TextView itemPrice = (TextView)findViewById(R.id.item_price);
        itemPrice.setText(String.valueOf(item.getItemCost()));
        TextView itemLocation = (TextView)findViewById(R.id.item_location);
        itemLocation.setText(item.getLocation());
        ImageView itemImage = (ImageView)findViewById(R.id.item_image);
        itemImage.setImageResource(item.getImage());


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
