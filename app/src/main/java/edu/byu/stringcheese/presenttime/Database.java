package edu.byu.stringcheese.presenttime;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dtaylor on 3/21/2016.
 */
public class Database {
    private ArrayList<Profile> profiles = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private static Database _instance;

    public Database()
    {
        _instance = this;
        Profile profile = new Profile("Justin","justin@cool.com");

        Event event = new Event("Justin's Wedding", "June 15th, 2016", R.drawable.balloon);
        event.addItem(new Item("Car",15000,"Toyota",R.drawable.ic_media_pause));
        event.addItem(new Item("Boat",7000,"The Dock",R.drawable.ic_star_black_24dp));
        event.addItem(new Item("Cake",100,"Cakes And More",R.drawable.ic_launcher));
        event.addItem(new Item("Cake2",100,"Cakes And More",R.drawable.ic_launcher));
        event.addItem(new Item("Cake3", 100, "Cakes And More", R.drawable.ic_launcher));
        event.addItem(new Item("Cake4", 100, "Cakes And More", R.drawable.ic_launcher));
        profile.addEvent(event);

        Event event2 = new Event("Sam's 25th Birthday", "December 17th, 2016", R.drawable.balloon);
        event2.addItem(new Item("House",105000,"Toyota",R.drawable.ic_media_pause));
        event2.addItem(new Item("Pancake",7000,"The Dock",R.drawable.ic_star_black_24dp));
        event2.addItem(new Item("Dog",100,"Cakes And More",R.drawable.ic_launcher));
        event2.addItem(new Item("Mouse",100,"Cakes And More",R.drawable.ic_launcher));
        event2.addItem(new Item("Card Game", 100, "Cakes And More", R.drawable.ic_launcher));
        event2.addItem(new Item("Bike", 100, "Cakes And More", R.drawable.ic_launcher));
        profile.addEvent(event2);

        Event event3 = new Event("Amanda's Graduation", "August 11th, 2016", R.drawable.balloon);
        event3.addItem(new Item("House2",105000,"Toyota",R.drawable.ic_media_pause));
        event3.addItem(new Item("Pancake3",7000,"The Dock",R.drawable.ic_star_black_24dp));
        event3.addItem(new Item("Dog4",100,"Cakes And More",R.drawable.ic_launcher));
        event3.addItem(new Item("Mouse5",100,"Cakes And More",R.drawable.ic_launcher));
        event3.addItem(new Item("Card Game6", 100, "Cakes And More", R.drawable.ic_launcher));
        event3.addItem(new Item("Bike7", 100, "Cakes And More", R.drawable.ic_launcher));
        profile.addEvent(event3);
        profiles.add(profile);
    }

    public static Database getInstance()
    {
        if(_instance != null)
            return _instance;
        return new Database();
    }

    public Profile getProfile(int i) {
        return profiles.get(i);
    }

    public Event getEvent(int eventId) {
        return events.get(eventId);
    }

    public class Profile
    {
        private static final String TAG = "Profile in Database";
        private ArrayList<Event> userEvents = new ArrayList<>();
        private ArrayList<Profile> friends = new ArrayList<>();
        private String name;
        private String email;
        private int profileId;

        public Profile(String name, String email)
        {
            this.name = name;
            this.email = email;
            this.profileId = profiles.size();
        }

        public void addFriend(Profile profile)
        {
            friends.add(profile);
        }

        public void addEvent(Event event)
        {
            events.add(event);
            userEvents.add(event);
        }

        public void addEvent(String eventName, String eventDate, int photoID) {
            Event newEvent = new Event(eventName, eventDate, photoID);
            events.add(newEvent);
            userEvents.add(newEvent);
            Log.d(TAG, "added the event: " + newEvent.getEventName() + " Date: " + newEvent.getEventDate());
        }

        public String getName()
        {
            return this.name;
        }

        public String getEmail()
        {
            return this.email;
        }

        public List<Event> getUserEvents() {
            return userEvents;
        }


    }

    public class Event {
        ArrayList<Item> eventItems = new ArrayList<>();
        private String eventName;
        private String eventDate;
        private int eventPhotoID;
        private int eventId;

        Event(String eventName, String eventDate, int eventPhotoID) {
            this.eventName = eventName;
            this.eventDate = eventDate;
            this.eventPhotoID = eventPhotoID;
            this.eventId = events.size();
        }

        public String getEventName() {
            return eventName;
        }

        public String getEventDate() {
            return eventDate;
        }

        public int getEventPhotoID() {
            return eventPhotoID;
        }

        public int getEventId() {
            return eventId;
        }

        public void addItem(Item item)
        {
            items.add(item);
            eventItems.add(item);
        }

        public List<Item> getItems() {
            return items;
        }
    }
    public class Item
    {
        private String itemName;
        private int itemCost;
        private String location;
        private int image;
        private int itemId;

        public Item(String name, int cost, String location, int image)
        {
            itemName=name;
            itemCost=cost;
            this.location=location;
            this.image=image;
            itemId = items.size();
        }

        public String getItemName() {
            return itemName;
        }

        public int getItemCost() {
            return itemCost;
        }

        public String getLocation() {
            return location;
        }

        public int getImage() {
            return image;
        }

        public int getItemId() {
            return itemId;
        }
    }
}

