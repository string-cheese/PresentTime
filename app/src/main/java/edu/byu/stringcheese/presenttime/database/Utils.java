package edu.byu.stringcheese.presenttime.database;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by dtaylor on 3/22/2016.
 */
public class Utils {
    public static ArrayList<Event> getEvents(Profile profile) {
        ArrayList<Event> events = new ArrayList<>();
        for(String eventId : profile.getEvents().values())
        {
            events.add(getEvent(eventId));
        }
        return events;
    }

    public static ArrayList<Profile> getFriends(Profile profile) {
        ArrayList<Profile> friends = new ArrayList<>();
        for(String friendId : profile.getFriends().values())
        {
            friends.add(getProfile(friendId));
        }
        return friends;
    }

    public static ArrayList<Item> getItems(Event event) {
        ArrayList<Item> items = new ArrayList<>();
        Map<String,String> map = event.getItems();
        if(map != null)
        {
            for (String itemId : map.values()) {
                items.add(getItem(itemId));
            }
        }
        return items;
    }

    public static Profile getProfile(String profileId)
    {
        for(Profile profile : FirebaseDatabase.getInstance().getProfiles().values())
        {
            if(profile.getId().equals(profileId))
            {
                return profile;
            }
        }
        return null;
    }

    public static Event getEvent(String eventId) {
        for(Event event : FirebaseDatabase.getInstance().getEvents().values())
        {
            if(event.getId().equals(eventId))
            {
                return event;
            }
        }
        return null;
    }

    public static Item getItem(String itemId)
    {
        for(Item item : FirebaseDatabase.getInstance().getItems().values())
        {
            if(item.getId().equals(itemId))
            {
                return item;
            }
        }
        return null;
    }

    public static Profile getProfileByEmail(String email) {
        for(Profile profile : FirebaseDatabase.getInstance().getProfiles().values())
        {
            if(profile.getEmail().equals(email))
            {
                return profile;
            }
        }
        return null;
    }
}
