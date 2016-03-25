package edu.byu.stringcheese.presenttime.database;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class Profile
{
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<String> friends = new ArrayList<>();
    private int id;
    private String name;
    private String email;
    private String favoriteStore;
    private String hobbies;
    private String birthDate;
    private String anniversaryDate;
    private String favoriteRestaurant;
    private String favoriteColor;

    public Profile()
    {

    }

    public Profile(int id, String name, String email, String favoriteStore, String hobbies, String birthDate, String anniversaryDate, String favoriteRestaurant, String favoriteColor) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.favoriteStore = favoriteStore;
        this.hobbies = hobbies;
        this.birthDate = birthDate;
        this.anniversaryDate = anniversaryDate;
        this.favoriteRestaurant = favoriteRestaurant;
        this.favoriteColor = favoriteColor;
    }

    public Profile addFriend(String email)
    {
        //update profile event list
        Firebase friends = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("friends");
        this.friends.add(email);

        friends.setValue(this.friends);
        return DBAccess.getProfileByEmail(email);
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

    public String getFavoriteStore() {
        return favoriteStore;
    }

    public String getHobbies() {
        return hobbies;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getAnniversaryDate() {
        return anniversaryDate;
    }

    public String getFavoriteRestaurant() {
        return favoriteRestaurant;
    }

    public String getFavoriteColor() {
        return favoriteColor;
    }
}
