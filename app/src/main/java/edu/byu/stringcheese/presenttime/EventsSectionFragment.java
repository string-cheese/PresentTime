package edu.byu.stringcheese.presenttime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dtaylor on 3/20/2016.
 */
public class EventsSectionFragment extends android.support.v4.app.Fragment {

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
        initializeAdapter();
    }
    private RecyclerView recyclerView;

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(Database.getInstance().getProfile(0).getUserEvents());
        recyclerView.setAdapter(adapter);
    }

    class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {

        List<Database.Event> eventsShown;

        RVAdapter(List<Database.Event> events){
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
            eventViewHolder.eventName.setText(eventsShown.get(i).getEventName());
            eventViewHolder.eventDate.setText(eventsShown.get(i).getEventDate());
            eventViewHolder.eventPhoto.setImageResource(eventsShown.get(i).getEventPhotoID());
            eventViewHolder.currentItem = i;
            eventViewHolder.eventId = eventsShown.get(i).getEventId();
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
            public int eventId;

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



