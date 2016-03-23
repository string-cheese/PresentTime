package edu.byu.stringcheese.presenttime;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.NumberFormat;

import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Utils;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddItemActivity";
    private FirebaseDatabase.Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        if(getIntent().getStringExtra("eventId") != null)
        {
            event = FirebaseDatabase.getInstance().getProfiles().get(Integer.parseInt(getIntent().getStringExtra("profileId"))).getEvents().get(Integer.parseInt(getIntent().getStringExtra("eventId")));
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
        final EditText priceTextBox = (EditText)findViewById(R.id.add_item_price);
        priceTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    priceTextBox.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    current = formatted;
                    priceTextBox.setText(formatted);
                    priceTextBox.setSelection(formatted.length());

                    priceTextBox.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_item_done:
                addNewItem();
        }
    }

    private void addNewItem() {
        Log.d(TAG, "adding new item...");
        String itemName = ((EditText) findViewById(R.id.add_item_name)).getText().toString();
        int itemPrice = Integer.parseInt(((EditText) findViewById(R.id.add_item_price)).getText().toString());
        String itemLocation = ((EditText) findViewById(R.id.add_item_location)).getText().toString();
        event.addItem(itemName, itemPrice, itemLocation, R.drawable.balloon);
        finish();
    }
}
