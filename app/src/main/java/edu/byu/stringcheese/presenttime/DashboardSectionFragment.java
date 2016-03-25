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

import java.util.ArrayList;
import java.util.List;

import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Profile;
import edu.byu.stringcheese.presenttime.recyclerviewresources.AbstractDashboardItem;
import edu.byu.stringcheese.presenttime.recyclerviewresources.DashBoardItem;
import edu.byu.stringcheese.presenttime.recyclerviewresources.DashboardHeader;

/**
 * Created by dtaylor on 3/20/2016.
 */
public class DashboardSectionFragment extends android.support.v4.app.Fragment {
    private RecyclerView recyclerView;
    private Profile profile;

    @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dashboard_section_fragment, container, false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null && getArguments().containsKey("profileId"))
        {
            profile = DBAccess.getProfile(Integer.parseInt(getArguments().getString("profileId")));
            recyclerView = (RecyclerView) view.findViewById(R.id.dashboard_upcoming_rv);

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

    private void initializeAdapter(){
        DashboardRVAdapter adapter = new DashboardRVAdapter(DBAccess.getUpcomingEventsItems(profile));
        recyclerView.setAdapter(adapter);
    }

    class DashboardRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<AbstractDashboardItem> eventsShown = new ArrayList<>();
        DashboardRVAdapter(List<AbstractDashboardItem> events){
            this.eventsShown = events;
        }

        @Override
        public int getItemViewType(int position) {
            return eventsShown.get(position).getType();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            if (viewType == AbstractDashboardItem.TYPE_HEADER) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_header, viewGroup, false);//$$$
                DashboardHeaderViewHolder dashboardHeaderViewHolder = new DashboardHeaderViewHolder(v);
                return dashboardHeaderViewHolder;
            } else {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_template, viewGroup, false);//$$$
                EventViewHolder eventViewHolder = new EventViewHolder(v);
                return eventViewHolder;
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
            int type = getItemViewType(i);
            if (type == AbstractDashboardItem.TYPE_HEADER) {
                DashboardHeader header = (DashboardHeader) DBAccess.getUpcomingEventsItems(profile).get(i);
                DashboardHeaderViewHolder headerHolder = (DashboardHeaderViewHolder) holder;
                headerHolder.label.setText(header.getLabel());
            } else {
                DashBoardItem item = (DashBoardItem) eventsShown.get(i);
                EventViewHolder eventViewHolder = (EventViewHolder) holder;
                if(item != null)
                {
                    eventViewHolder.eventName.setText(eventsShown.get(i).getName());
                    eventViewHolder.eventDate.setText(eventsShown.get(i).getDateAsString());
                    eventViewHolder.eventPhoto.setImageResource(eventsShown.get(i).getPhotoId());
                    eventViewHolder.currentItem = i;
                    eventViewHolder.profileId = String.valueOf(eventsShown.get(i).getProfileId());
                    eventViewHolder.eventId = String.valueOf(eventsShown.get(i).getId());
                }

                // your logic here
            }


        }

        @Override
        public int getItemCount() {
            return eventsShown.size();
        }

        public class DashboardHeaderViewHolder extends RecyclerView.ViewHolder {

            TextView label;
            View line;

            DashboardHeaderViewHolder(final View itemView) {
                super(itemView);

                label = (TextView)itemView.findViewById(R.id.list_header);
                line = itemView.findViewById(R.id.header_line);
            }
        }

        public class EventViewHolder extends RecyclerView.ViewHolder {

            CardView cv;
            TextView eventName;
            TextView eventDate;
            ImageView eventPhoto;
            public int currentItem;
            public String eventId;
            public String profileId;

            EventViewHolder(final View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), EventInfoActivity.class);
                        intent.putExtra("eventId", String.valueOf(eventId));
                        intent.putExtra("profileId", String.valueOf(profileId));
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
