package edu.byu.stringcheese.presenttime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.byu.stringcheese.presenttime.database.Event;
import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Profile;
import edu.byu.stringcheese.presenttime.database.Utils;

/**
 * Created by dtaylor on 3/20/2016.
 */
public class EventsSectionFragment extends android.support.v4.app.Fragment {

    private Profile profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.events_section_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null && getArguments().containsKey("profileId"))
        {
            profile = Utils.getProfile(getArguments().getString("profileId"));
            recyclerView = (RecyclerView) view.findViewById(R.id.events_section_rv);

            LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
            recyclerView.setLayoutManager(llm);
            recyclerView.setHasFixedSize(true);
            initializeAdapter();
        }
        else
        {
            Snackbar.make(view, "Something is wrong, this doesn't exist", Snackbar.LENGTH_LONG).show();
        }
    }
    private RecyclerView recyclerView;

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(Utils.getEvents(profile));
        recyclerView.setAdapter(adapter);
    }

    class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {

        List<Event> eventsShown;
        RVAdapter(List<Event> events){
            this.eventsShown = events;
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
            if(eventsShown.get(i) != null)
            {
                eventViewHolder.eventName.setText(eventsShown.get(i).getName());
                eventViewHolder.eventDate.setText(eventsShown.get(i).getDate());
                eventViewHolder.eventPhoto.setImageResource(eventsShown.get(i).getPhotoId());
                eventViewHolder.currentItem = i;
                eventViewHolder.eventId = eventsShown.get(i).getId();
            }
        }

        @Override
        public int getItemCount() {
            return eventsShown.size();
        }

        public class EventViewHolder extends RecyclerView.ViewHolder {

            CardView cv;
            TextView eventName;
            TextView eventDate;
            ImageView eventPhoto;
            public int currentItem;
            public String eventId;

            EventViewHolder(final View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), EventInfoActivity.class);
                        intent.putExtra("eventId", eventId);
                        getActivity().startActivity(intent);
                    }
                });
                cv = (CardView)itemView.findViewById(R.id.cv);
                eventName = (TextView)itemView.findViewById(R.id.event_name);
                eventDate = (TextView)itemView.findViewById(R.id.event_date);
                eventPhoto = (ImageView)itemView.findViewById(R.id.event_photo);
            }
        }
    }
}



