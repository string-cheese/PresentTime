package edu.byu.stringcheese.presenttime.database;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

import edu.byu.stringcheese.presenttime.LoginActivity;

/**
 * Created by dtaylor on 3/22/2016.
 */
public class Event {
    private HashMap<String, String> items;
    private String name;
    private String date;
    private int photoId;
    private String profileId;
    private String location;
    private String id;

    public Event()
    {

    }

    public Event(String name, String date, int photoId, String location, String profileId, String id) {
        this.name = name;
        this.date = date;
        this.photoId = photoId;
        this.profileId = profileId;
        this.location = location;
        this.id = id;
        this.items = new HashMap<>();
    }

    public String addItem(String name, int cost, String store, int imageID) {
        //add item
        Firebase eventItems = LoginActivity.ref.child("items");
        Firebase newItem = eventItems.push();
        String key = newItem.getKey();
        newItem.setValue(new Item(name, cost, store, imageID, id, key));

        //update profile items list
        Firebase event = LoginActivity.ref.child("profiles").child(profileId);
        Firebase itemList = event.child("items");
        String itemKey = itemList.push().getKey();
        this.items.put(itemKey, key);
        Map<String,Object> value = new HashMap<>();
        value.put("items", this.items);
        event.updateChildren(value);
        return key;
    }

    public HashMap<String, String> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getPhotoId() {
        return photoId;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }
}
