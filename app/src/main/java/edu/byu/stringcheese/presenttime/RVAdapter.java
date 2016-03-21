package edu.byu.stringcheese.presenttime; /**
 * Created by amandafisher on 3/20/16.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {

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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_activity, viewGroup, false);//$$$
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