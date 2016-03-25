package edu.byu.stringcheese.presenttime.database;

import com.firebase.client.Firebase;

public class Item
{
    private int eventId;
    private int id;
    private int profileId;
    private String name;
    private double cost;
    private String store;
    private int imageId;
    private boolean purchased;
    public Item()
    {

    }
    public Item(String name, double cost, String location, int image, int profileId, int eventId, int id, boolean purchased)
    {
        this.name = name;
        this.cost = cost;
        this.store = location;
        this.imageId = image;
        this.profileId = profileId;
        this.eventId = eventId;
        this.id = id;
        this.purchased = purchased;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public String getStore() {
        return store;
    }

    public int getImageId() {
        return imageId;
    }

    public int getId() {
        return id;
    }

    public int getEventId() {
        return eventId;
    }

    public int getProfileId() {
        return profileId;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void updateName(String name) {
        this.name = name;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(eventId)).child("items").child(String.valueOf(id)).child("name");
        db.setValue(name);
    }

    public void updateCost(double cost) {
        this.cost = cost;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(eventId)).child("items").child(String.valueOf(id)).child("cost");
        db.setValue(cost);
    }

    public void updateStore(String store) {
        this.store = store;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(eventId)).child("items").child(String.valueOf(id)).child("store");
        db.setValue(store);
    }

    public void updateImageId(int imageId) {
        this.imageId = imageId;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(eventId)).child("items").child(String.valueOf(id)).child("imageId");
        db.setValue(imageId);
    }

    public void updatePurchased(boolean purchased) {
        this.purchased = purchased;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(eventId)).child("items").child(String.valueOf(id)).child("purchased");
        db.setValue(purchased);
    }
}
