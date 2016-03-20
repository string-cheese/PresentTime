package edu.byu.stringcheese.presenttime; /**
 * Created by amandafisher on 3/20/16.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.byu.stringcheese.presenttime.Event;
import edu.byu.stringcheese.presenttime.RVAdapter;

public class RecyclerViewActivity extends Activity {

    private List<Event> events;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recyclerview_activity);

        recyclerView =(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        events = new ArrayList<>();
        events.add(new Event("Justin's Wedding", "June 15th, 2016", R.drawable.justin_profile));
        events.add(new Event("Sam's 25th Birthday", "December 17th, 2016", R.drawable.balloon));
        events.add(new Event("Amanda's Graduation", "August 11th, 2016", R.drawable.balloon));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(events);
        recyclerView.setAdapter(adapter);
    }
}