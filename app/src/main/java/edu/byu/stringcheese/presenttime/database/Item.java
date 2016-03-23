package edu.byu.stringcheese.presenttime.database;

/**
 * Created by dtaylor on 3/22/2016.
 */
public class Item
{
    private String name;
    private int cost;
    private String store;
    private int imageId;
    private String eventId;
    private String id;

    public Item()
    {

    }
    public Item(String name, int cost, String location, int image, String eventId, String id)
    {
        this.name =name;
        this.cost =cost;
        this.store =location;
        this.imageId =image;
        this.eventId = eventId;
        this.id = id;
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

    public int getImageId() {
        return imageId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getId() {
        return id;
    }
}
