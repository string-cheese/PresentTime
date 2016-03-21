package edu.byu.stringcheese.presenttime;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class EventInfoActivity extends AppCompatActivity {
    public Database.Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        event = Database.getInstance().getEvent((int)getIntent().getExtras().get("eventId"));
        //((TextView)findViewById(R.id.selectedEvent)).setText(event.getEventName());
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ItemAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(EventInfoActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    class ItemAdapter extends BaseAdapter {

        List<Database.Item> shownItems;
        Context context;

        ItemAdapter(EventInfoActivity context){
            this.context = context;
            shownItems = context.event.getItems();
        }

        @Override
        public int getCount() {
            return shownItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(shownItems.get(position).getImage());
            return imageView;
        }
    }
}
