package edu.byu.stringcheese.presenttime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EventInfoActivity extends AppCompatActivity {
    public Database.Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        event = Database.getInstance().getEvent((int)getIntent().getExtras().get("eventId"));
        TextView title = (TextView)findViewById(R.id.event_title_text_view);
        title.setText(event.getEventName());
        TextView address = (TextView)findViewById(R.id.address_text_view);
        address.setText(event.getEventAddress());
        Button wishListButton = (Button)findViewById(R.id.wish_list_button);
        wishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventInfoActivity.this, EventWishListActivity.class);
                intent.putExtra("eventId", event.getEventId());
                EventInfoActivity.this.startActivity(intent);
            }
        });
    }
}
