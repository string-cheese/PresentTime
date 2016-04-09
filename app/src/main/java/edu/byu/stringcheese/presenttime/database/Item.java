package edu.byu.stringcheese.presenttime.database;

import com.firebase.client.Firebase;

public class Item
{
    private int eventId;
    private int id;
    private int profileId;
    private String name;
    private int cost;
    private int amount_funded;
    private String store;
    private String encodedImage;
    private boolean purchased;
    public Item()
    {

    }
    public Item(String name, int cost, String location, String encodedImage, int profileId, int eventId, int id, boolean purchased, int amount_funded)
    {
        this.name = name;
        this.cost = cost;
        this.store = location;
        this.encodedImage = encodedImage;
        this.profileId = profileId;
        this.eventId = eventId;
        this.id = id;
        this.purchased = purchased;
        this.amount_funded = amount_funded;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getStore() {
        return store;
    }

    public String getEncodedImage() {
        return encodedImage;
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

    public int getAmount_funded() {
        return amount_funded;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void updateName(String name) {
        this.name = name;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(eventId)).child("items").child(String.valueOf(id)).child("name");
        db.setValue(name);
    }

    public void updateCost(int cost) {
        this.cost = cost;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(eventId)).child("items").child(String.valueOf(id)).child("cost");
        db.setValue(cost);
    }

    public void updateStore(String store) {
        this.store = store;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(eventId)).child("items").child(String.valueOf(id)).child("store");
        db.setValue(store);
    }

    public void updateEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(eventId)).child("items").child(String.valueOf(id)).child("encodedImage");
        db.setValue(encodedImage);
    }

    public void updatePurchased(boolean purchased) {
        this.purchased = purchased;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(eventId)).child("items").child(String.valueOf(id)).child("purchased");
        db.setValue(purchased);
    }

    public void updateAmountFunded(int amount_funded)
    {
        this.amount_funded = amount_funded;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId)).child("events").child(String.valueOf(eventId)).child("items").child(String.valueOf(id)).child("amount_funded");
        db.setValue(amount_funded);
    }
}
