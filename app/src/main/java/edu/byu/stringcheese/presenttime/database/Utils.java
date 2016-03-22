package edu.byu.stringcheese.presenttime.database;

import java.util.ArrayList;

/**
 * Created by dtaylor on 3/22/2016.
 */
public class Utils {
    public static ArrayList<Event> getEvents(Profile profile) {
        ArrayList<Event> events = new ArrayList<>();
        for(String eventId : profile.getEvents().values())
        {
            events.add(FirebaseDatabase.getInstance().getEvent(eventId));
        }
        return events;
    }

    public static ArrayList<Profile> getFriends(Profile profile) {
        ArrayList<Profile> friends = new ArrayList<>();
        for(String friendId : profile.getFriends().values())
        {
            friends.add(FirebaseDatabase.getInstance().getProfile(friendId));
        }
        return friends;
    }

    public static ArrayList<Item> getItems(Event event) {
        ArrayList<Item> items = new ArrayList<>();
        for(String itemId : event.getItems().values())
        {
            FirebaseDatabase.getInstance().getItem(itemId);
        }
        return items;
    }
}
