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
    public Item()
    {

    }
    public Item(String name, double cost, String location, int image, int profileId, int eventId, int id)
    {
        this.name =name;
        this.cost =cost;
        this.store =location;
        this.imageId =image;
        this.profileId = profileId;
        this.eventId = eventId;
        this.id = id;
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
}
