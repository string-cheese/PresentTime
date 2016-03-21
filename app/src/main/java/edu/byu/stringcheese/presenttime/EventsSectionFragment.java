package edu.byu.stringcheese.presenttime;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dtaylor on 3/20/2016.
 */
public class EventsSectionFragment extends android.support.v4.app.Fragment {
    TextView eventName;
    TextView eventDate;
    ImageView eventPhoto;
    /** The CardView widget. */
    //@VisibleForTesting
    CardView mCardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.events_section_fragment, container, false);
        /*TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
        dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));*/
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView =(RecyclerView)view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }
    private List<Event> events;
    private RecyclerView recyclerView;


    private void initializeData(){
        events = new ArrayList<>();
        events.add(new Event("Justin's Wedding", "June 15th, 2016", R.drawable.balloon));
        events.add(new Event("Sam's 25th Birthday", "December 17th, 2016", R.drawable.balloon));
        events.add(new Event("Amanda's Graduation", "August 11th, 2016", R.drawable.balloon));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(events);
        recyclerView.setAdapter(adapter);
    }


}
class Event {

    String eventName;
    String eventDate;
    int eventPhotoID;

    Event(String eventName, String eventDate, int eventPhotoID) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventPhotoID = eventPhotoID;
    }
}
class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView eventName;
        TextView eventDate;
        ImageView eventPhoto;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventDate = (TextView)itemView.findViewById(R.id.event_date);
            eventPhoto = (ImageView)itemView.findViewById(R.id.event_photo);
        }
    }

    List<Event> events;

    RVAdapter(List<Event> events){
        this.events = events;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_template, viewGroup, false);//$$$
        EventViewHolder eventViewHolder = new EventViewHolder(v);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        eventViewHolder.eventName.setText(events.get(i).eventName);
        eventViewHolder.eventDate.setText(events.get(i).eventDate);
        eventViewHolder.eventPhoto.setImageResource(events.get(i).eventPhotoID);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}

