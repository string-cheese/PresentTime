package edu.byu.stringcheese.presenttime.database;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

import edu.byu.stringcheese.presenttime.LoginActivity;

/**
 * Created by dtaylor on 3/22/2016.
 */
public class Profile
{
    private Map<String,String> events = new HashMap<>();
    private Map<String,String> friends = new HashMap<>();
    private String name;
    private String email;
    private String id;

    public Profile()
    {

    }

    public Profile(String name, String email, String id)
    {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String addFriend(String value)
    {
        //update profile event list
        Firebase profile = LoginActivity.ref.child("profiles").child(id);
        Firebase friends = profile.child("friends");
        String key = friends.push().getKey();

        this.friends.put(key, value);
        Map<String,Object> pair = new HashMap<>();
        pair.put("friends", this.friends);
        profile.updateChildren(pair);
        return key;
    }

    public Event addEvent(String eventName, String eventDate, int photoID, String eventAddress) {
        //add event
        Firebase events = LoginActivity.ref.child("events");
        Firebase newEvent = events.push();
        String key = newEvent.getKey();
        Event event = new Event(eventName,eventDate,photoID,eventAddress,id,key);
        newEvent.setValue(event);

        //update profile event list
        Firebase profile = LoginActivity.ref.child("profiles").child(id);
        Firebase profileEvents = profile.child("events");
        String profileEventKey = profileEvents.push().getKey();
        this.events.put(profileEventKey, key);
        Map<String,Object> value = new HashMap<>();
        value.put("events", this.events);
        profile.updateChildren(value);

        return event;
    }

    public Map<String, String> getEvents() {
        return events;
    }

    public Map<String, String> getFriends() {
        return friends;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}
