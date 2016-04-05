package edu.byu.stringcheese.presenttime.main.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import java.util.Observable;
import java.util.Observer;

import edu.byu.stringcheese.presenttime.BitmapUtils;
import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Event;
import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Profile;
import edu.byu.stringcheese.presenttime.main.events.info.EventInfoActivity;

/**
 * Created by dtaylor on 3/20/2016.
 */
public class EventsSectionFragment extends Fragment implements Observer {

    private Profile profile;

    public EventsSectionFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null && getArguments().containsKey("clientProfileId"))
        {
            if (getArguments().containsKey("clientProfileId") && getArguments().containsKey("eventOwnerId"))
            {
                initializeFriendViews(view);
            } else
            {

                initializeOwnerViews(view);
            }


            recyclerView = (RecyclerView) view.findViewById(R.id.events_section_rv);

            LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
            recyclerView.setLayoutManager(llm);
            recyclerView.setHasFixedSize(false);
            initializeAdapter();
        }
        else
        {
            Snackbar.make(view, "Something is wrong, this doesn't exist", Snackbar.LENGTH_LONG).show();
        }
    }

    private void initializeFriendViews(View view) {
        profile = DBAccess.getProfile(Integer.parseInt(getArguments().getString("eventOwnerId")));

        FloatingActionButton addEventFAB = (FloatingActionButton) view.findViewById(R.id.add_event_fab);
        addEventFAB.setVisibility(View.INVISIBLE);
    }

    private void initializeOwnerViews(View view) {
        profile = DBAccess.getProfile(Integer.parseInt(getArguments().getString("clientProfileId")));
        getArguments().putString("eventOwnerId", getArguments().getString("clientProfileId"));

        FloatingActionButton addEventFAB = (FloatingActionButton) view.findViewById(R.id.add_event_fab);
        addEventFAB.setVisibility(View.VISIBLE);
        addEventFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                intent.putExtra("clientProfileId", String.valueOf(profile.getId()));
                getActivity().startActivity(intent);
            }
        });
    }

    private RecyclerView recyclerView;
    private void initializeAdapter(){
        ArrayList<Event> events = profile.getEvents();
        if(events != null) {
            RVAdapter adapter = new RVAdapter(events);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if(recyclerView.getAdapter() != null)
        {
            ArrayList<Event> events = DBAccess.getEvents(profile.getId());
            if(events != null) {
                ((RVAdapter) recyclerView.getAdapter()).updateEventsShown(events);
                recyclerView.invalidate();
            }
        }
    }

    class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {

        public List<Event> eventsShown;
        RVAdapter(List<Event> events){
            this.eventsShown = events;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_event, viewGroup, false);//$$$
            EventViewHolder eventViewHolder = new EventViewHolder(v);
            return eventViewHolder;
        }

        @Override
        public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
            if(eventsShown.get(i) != null)
            {
                eventViewHolder.eventName.setText(eventsShown.get(i).getName());
                eventViewHolder.eventDate.setText(eventsShown.get(i).dateAsString());
                eventViewHolder.eventPhoto.setImageBitmap(BitmapUtils.decodeStringToBitmap(eventsShown.get(i).getEncodedImage()));
                eventViewHolder.currentItem = i;
                eventViewHolder.eventOwnerId = String.valueOf(eventsShown.get(i).getProfileId());
                eventViewHolder.eventId = String.valueOf(eventsShown.get(i).getId());
            }
        }

        @Override
        public int getItemCount() {
            return eventsShown.size();
        }

        public void updateEventsShown(ArrayList<Event> events) {
            this.eventsShown.clear();
            this.eventsShown.addAll(events);
            notifyDataSetChanged();
        }

        public class EventViewHolder extends RecyclerView.ViewHolder {

            CardView cv;
            TextView eventName;
            TextView eventDate;
            ImageView eventPhoto;
            public int currentItem;
            public String eventId;
            public String eventOwnerId;

            EventViewHolder(final View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), EventInfoActivity.class);
                        intent.putExtra("eventId", String.valueOf(eventId));
                        intent.putExtra("clientProfileId", getArguments().getString("clientProfileId"));
                        intent.putExtra("eventOwnerId", String.valueOf(eventOwnerId));
                        getActivity().startActivity(intent);
                    }
                });
                cv = (CardView)itemView.findViewById(R.id.cv);
                eventName = (TextView)itemView.findViewById(R.id.event_template_name);
                eventDate = (TextView)itemView.findViewById(R.id.event_template_date);
                eventPhoto = (ImageView)itemView.findViewById(R.id.event_template_photo);
            }
        }
    }
}



