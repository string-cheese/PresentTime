package edu.byu.stringcheese.presenttime.database;

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
}
