package edu.byu.stringcheese.presenttime.database;

/**
 * Created by dtaylor on 3/22/2016.
 */

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.stringcheese.presenttime.LoginActivity;
import edu.byu.stringcheese.presenttime.R;

/**
 * Created by dtaylor on 3/21/2016.
 */
public class FirebaseDatabase {
    private Map<String,Profile> profiles = new HashMap<>();
    private Map<String,Event> events = new HashMap<>();
    private Map<String,Item> items = new HashMap<>();
    private static FirebaseDatabase _instance;

    public static FirebaseDatabase getInstance()
    {
        if(_instance != null)
            return _instance;
        return new FirebaseDatabase();
    }

    public static void setInstance(FirebaseDatabase db)
    {
        if(_instance == null)
            _instance = db;
    }

    public FirebaseDatabase()
    {

    }

    public Profile addProfile(String name, String email)
    {
        //add item
        Firebase profiles = LoginActivity.ref.child("profiles");
        Firebase newProfile = profiles.push();
        String key = newProfile.getKey();
        Profile profile = new Profile(name, email, key);
        newProfile.setValue(profile);
        return profile;
    }

    public void fakeData()
    {
        Profile profile = addProfile("Justin","justin@cool.com");

        Event event = profile.addEvent("Justin's Wedding", "June 15th, 2016", R.mipmap.wedding, "SLC Temple");
        event.addItem("Car",15000,"Toyota",R.mipmap.car);
        event.addItem("Boat",7000,"The Dock",R.mipmap.boat);
        event.addItem("Cake",100,"Cakes And More",R.mipmap.cake);
        event.addItem("Cake2",100,"Cakes And More",R.mipmap.cake);
        event.addItem("Cake3", 100, "Cakes And More", R.mipmap.cake);
        event.addItem("Cake4", 100, "Cakes And More", R.mipmap.cake);

        Event event2 = profile.addEvent("Sam's 25th Birthday", "December 17th, 2016", R.mipmap.balloon, "Buffalo Wild Wings");
        event2.addItem("House",105000,"Toyota",R.mipmap.house);
        event2.addItem("Pancake",7000,"The Dock",R.mipmap.pancake);
        event2.addItem("Dog",100,"Cakes And More",R.mipmap.dog);
        event2.addItem("Mouse", 100, "Cakes And More", R.mipmap.mouse);
        event2.addItem("Card Game", 100, "Cakes And More", R.mipmap.card);
        event2.addItem("Bike", 100, "Cakes And More", R.mipmap.bike);

        Event event3 = profile.addEvent("Amanda's Graduation", "August 11th, 2016", R.mipmap.graduation, "BYU");
        event3.addItem("House2",105000,"Toyota",R.drawable.ic_media_pause);
        event3.addItem("Pancake3",7000,"The Dock",R.drawable.ic_star_black_24dp);
        event3.addItem("Dog4",100,"Cakes And More",R.drawable.ic_launcher);
        event3.addItem("Mouse5",100,"Cakes And More",R.drawable.ic_launcher);
        event3.addItem("Card Game6", 100, "Cakes And More", R.drawable.ic_launcher);
        event3.addItem("Bike7", 100, "Cakes And More", R.drawable.ic_launcher);




        Profile profile2 = addProfile("Joe","joe@cool.com");

        Event eventa = profile2.addEvent("Joe's Wedding", "June 15th, 2016", R.drawable.balloon, "an address");
        eventa.addItem("Tic",0,"Tac",R.drawable.ic_media_pause);
        eventa.addItem("Tac",25,"The Dock",R.drawable.ic_star_black_24dp);
        eventa.addItem("Toe",178,"Cakes And More",R.drawable.ic_launcher);

        Event event2a = profile2.addEvent("Billy's 23th Birthday", "December 17th, 2016", R.drawable.balloon, "1942 columnus");
        event2a.addItem("Kitty", 65, "Toyota", R.drawable.ic_media_pause);
        event2a.addItem("Dr. Who Stuff",71245000,"The Dock",R.drawable.ic_star_black_24dp);
        event2a.addItem("Monkey",102340,"Cakes And More",R.drawable.ic_launcher);

        Event event3a = profile2.addEvent("Amanda's Graduation", "August 11th, 2016", R.drawable.balloon, "24221 Sagewood dr., Provo Utah");
        event3a.addItem("House2", 0, "Toyota", R.drawable.ic_media_pause);
        event3a.addItem("Pancake3",0,"The Dock",R.drawable.ic_star_black_24dp);
        event3a.addItem("Dog4",0,"Cakes And More",R.drawable.ic_launcher);
    }

    public Map<String, Profile> getProfiles() {
        return profiles;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public static boolean hasInstance() {
        return _instance != null;
    }
}


