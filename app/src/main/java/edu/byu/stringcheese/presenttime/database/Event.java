package edu.byu.stringcheese.presenttime.database;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Calendar;

import edu.byu.stringcheese.presenttime.Utils;

public class Event implements Comparable<Event>{
    private ArrayList<Item> items = new ArrayList<>();
    private String name;
    private Calendar date;
    private String encodedImage;
    private String location;
    private int profileId;
    private int id;

    public Event()
    {

    }

    public Event(String name, String date, String encodedImage, String location, int profileId, int id) {
        this.name = name;
        this.date = Utils.parseDate(date);
        this.encodedImage = encodedImage;
        this.location = location;
        this.items = new ArrayList<>();
        this.profileId = profileId;
        this.id = id;
    }

    public Item addItem(String name, double cost, String store, String encodedImage, boolean purchased) {
        //add item
        Firebase profile = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId));
        Item item = new Item(name, cost, store, encodedImage,profileId,this.id, items.size(), purchased);
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

    public Calendar getDate() {
        return date;
    }

    public String dateAsString(){
        return Utils.stringifyDate(date);
    }

    public String getEncodedImage() {
        return encodedImage;
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
        this.date = Utils.parseDate(date);
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(id)).child("date");
        db.setValue(date);
    }
    public void updateDate(Calendar date) {
        this.date = date;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(id)).child("date");
        db.setValue(date);
    }

    public void updateEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(id)).child("encodedImage");
        db.setValue(encodedImage);
    }

    public void updateLocation(String location) {
        this.location = location;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(id)).child("location");
        db.setValue(location);
    }

    public void removeItem(int index)
    {
        Firebase profile = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId));
        this.items.remove(index);
        profile.setValue(DBAccess.getProfiles().get(profileId));
    }

    @Override
    public int compareTo(Event another) {
        return another.date.getTime().compareTo(this.date.getTime());
    }
}
