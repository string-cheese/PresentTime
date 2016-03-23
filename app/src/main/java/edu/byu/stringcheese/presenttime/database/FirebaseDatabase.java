package edu.byu.stringcheese.presenttime.database;

/**
 * Created by dtaylor on 3/22/2016.
 */

import android.content.Context;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import edu.byu.stringcheese.presenttime.R;

/**
 * Created by dtaylor on 3/21/2016.
 */
public class FirebaseDatabase{
    private ArrayList<Profile> profiles = new ArrayList<>();
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
        Firebase profiles = FirebaseDatabase.ref.child("profiles");
        Profile profile = new Profile(name, email, this.profiles.size());
        this.profiles.add(profile);
        profiles.setValue(this.profiles);
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
    public void fakeData2() {
        //Utils.getProfileByEmail("justin@cool.com").addFriendByEmail("joe@cool.com");
    }

    public ArrayList<Profile> getProfiles() {
        return profiles;
    }

    public static boolean hasInstance() {
        return _instance != null;
    }

    public static Firebase ref;


    public static void initializeFirebase(Context context) {
        Firebase.setAndroidContext(context);
        ref = new Firebase("https://crackling-fire-2441.firebaseio.com/present-time-test");
        ref.addValueEventListener(getValueEventListener());
        ref.getParent().addChildEventListener(getChildEventListener());
    }

    private static DatabaseChildEventListener childEventListener;
    private static DatabaseChildEventListener getChildEventListener()
    {
        if(childEventListener == null)
        {
            childEventListener = new DatabaseChildEventListener();
        }
        return childEventListener;
    }
    private static DatabaseValueEventListener valueEventListener;
    private static DatabaseValueEventListener getValueEventListener()
    {
        if(valueEventListener == null)
        {
            valueEventListener = new DatabaseValueEventListener();
        }
        return valueEventListener;
    }

    public static void addObserver(Observer obs)
    {
        getChildEventListener().addObserver(obs);
        getValueEventListener().addObserver(obs);
    }


}
public class Profile implements Serializable
{
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<String> friends = new ArrayList<>();
    private int id;
    private String name;
    private String email;

    public Profile()
    {

    }

    public Profile(String name, String email, int id)
    {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public Profile addFriend(String email)
    {
        //update profile event list
        Firebase friends = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("friends");
        this.friends.add(email);

        friends.setValue(this.friends);
        return Utils.getProfileByEmail(email);
    }

    public Event addEvent(String eventName, String eventDate, int photoID, String eventAddress) {
        //add event
        Firebase events = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("events");
        Event event = new Event(eventName,eventDate,photoID,eventAddress, id, this.events.size());
        this.events.add(event);
        events.setValue(this.events);
        return event;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }
}
public class Event  implements Serializable{
    private ArrayList<Item> items;
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

    public Item addItem(String name, int cost, String store, int imageID) {
        //add item
        Firebase profile = FirebaseDatabase.ref.child("profiles").child(String.valueOf(profileId));
        Item item = new Item(name, cost, store, imageID,profileId,this.id, items.size());
        this.items.add(item);
        profile.setValue(profiles.get(profileId));
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
}
public class Item implements Serializable
{
    private int eventId;
    private int id;
    private int profileId;
    private String name;
    private int cost;
    private String store;
    private int imageId;

    public Item()
    {

    }
    public Item(String name, int cost, String location, int image, int profileId, int eventId, int id)
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

    public int getCost() {
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



//OUTSIDE
class DatabaseValueEventListener extends Observable implements ValueEventListener {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        System.out.println("There are " + dataSnapshot.getChildrenCount() + " blog posts");
        if (dataSnapshot.getKey() != null && dataSnapshot.getKey().equals("present-time")) {
            GenericTypeIndicator<FirebaseDatabase> t = new GenericTypeIndicator<FirebaseDatabase>() {
            };
            FirebaseDatabase dbTest = dataSnapshot.getValue(t);
            FirebaseDatabase.setInstance(dbTest);
            //NOTIFY OBSERVERS
            setChanged();
            notifyObservers();
        }
                /*for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Database db = postSnapshot.getValue(Database.class);
                    //System.out.println(post.getAuthor() + " - " + post.getTitle());
                }*/
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        System.out.println("The read failed: " + firebaseError.getMessage());
    }
}
class DatabaseChildEventListener extends Observable implements ChildEventListener {

    // Retrieve new posts as they are added to the database
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
        if (dataSnapshot.getKey().equals("present-time-test")) {
            GenericTypeIndicator<FirebaseDatabase.Item> t1 = new GenericTypeIndicator<FirebaseDatabase.Item>(){};
            Item item = dataSnapshot.child("profiles").child("0").child("events").child("0").child("items").child("0").getValue(t1);

            GenericTypeIndicator<ArrayList<FirebaseDatabase.Item>> t2 = new GenericTypeIndicator<ArrayList<FirebaseDatabase.Item>>(){};
            ArrayList<FirebaseDatabase.Item> items = dataSnapshot.child("profiles").child("0").child("events").child("0").child("items").getValue(t2);

            GenericTypeIndicator<ArrayList<FirebaseDatabase.Event>> t3 = new GenericTypeIndicator<ArrayList<FirebaseDatabase.Event>>() {};
            ArrayList<FirebaseDatabase.Event> events = dataSnapshot.child("profiles").child("0").child("events").getValue(t3);

            GenericTypeIndicator<ArrayList<FirebaseDatabase.Profile>> t4 = new GenericTypeIndicator<ArrayList<FirebaseDatabase.Profile>>() {};
            ArrayList<FirebaseDatabase.Profile> profiles = dataSnapshot.child("profiles").getValue(t4);

            GenericTypeIndicator<FirebaseDatabase> t = new GenericTypeIndicator<FirebaseDatabase>() {};
            FirebaseDatabase dbTest = dataSnapshot.getValue(t);
            FirebaseDatabase.setInstance(dbTest);
            //NOTIFY OBSERVERS
            setChanged();
            notifyObservers();
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot.getKey().equals("present-time-test")) {
            GenericTypeIndicator<FirebaseDatabase> t = new GenericTypeIndicator<FirebaseDatabase>() {
            };
            FirebaseDatabase dbTest = dataSnapshot.getValue(t);
            FirebaseDatabase.setInstance(dbTest);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}


