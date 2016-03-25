package edu.byu.stringcheese.presenttime.database;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class Event{
    private ArrayList<Item> items = new ArrayList<>();
    private String name;
    private String date;
    private int photoId;
    private String location;
    private int profileId;
    private int id;

    public Event()
    {

    }

    public Event(String name, String date, int photoId, String location, int profileId, int id) {
        this.name = name;
        this.date = date;
        this.photoId = photoId;
        this.location = location;
        this.items = new ArrayList<>();
        this.profileId = profileId;
        this.id = id;
    }

    public Item addItem(String name, double cost, String store, int imageID, boolean purchased) {
        //add item
        Firebase profile = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId));
        Item item = new Item(name, cost, store, imageID,profileId,this.id, items.size(), purchased);
        this.items.add(item);
        profile.setValue(DBAccess.getProfiles().get(profileId));
        return item;
    }

    public ArrayList<Item> getItems() {
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

    public String getLocation() {
        return location;
    }

    public int getProfileId() {
        return profileId;
    }

    public int getId() {
        return id;
    }

    public void updateName(String name) {
        this.name = name;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(id)).child("name");
        db.setValue(name);
    }

    public void updateDate(String date) {
        this.date = date;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(id)).child("date");
        db.setValue(date);
    }

    public void updatePhotoId(int photoId) {
        this.photoId = photoId;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(id)).child("photoId");
        db.setValue(photoId);
    }

    public void updateLocation(String location) {
        this.location = location;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(id)).child("location");
        db.setValue(location);
    }
}
