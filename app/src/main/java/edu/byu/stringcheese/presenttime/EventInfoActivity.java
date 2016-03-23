package edu.byu.stringcheese.presenttime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Utils;

public class EventInfoActivity extends AppCompatActivity {
    public FirebaseDatabase.Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        if(getIntent().getStringExtra("eventId") != null)
        {
            event = Utils.getProfile(Integer.parseInt(getIntent().getStringExtra("profileId"))).getEvents().get(Integer.parseInt(getIntent().getStringExtra("eventId")));
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
        Button wishListButton = (Button) findViewById(R.id.wish_list_button);
        wishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventInfoActivity.this, EventWishListActivity.class);
                intent.putExtra("eventId", event.getId());
                EventInfoActivity.this.startActivity(intent);
            }
        });
    }
}
