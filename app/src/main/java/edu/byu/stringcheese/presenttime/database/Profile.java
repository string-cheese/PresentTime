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
    private String googleId;
    private String encodedProfileImage;

    public Profile()
    {

    }

    public Profile(int id, String name, String email, String googleId, String favoriteStore, String hobbies, String birthDate, String anniversaryDate, String favoriteRestaurant, String favoriteColor, String encodedProfileImage) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.favoriteStore = favoriteStore;
        this.hobbies = hobbies;
        this.birthDate = birthDate;
        this.anniversaryDate = anniversaryDate;
        this.favoriteRestaurant = favoriteRestaurant;
        this.favoriteColor = favoriteColor;
        this.googleId = googleId;
        this.encodedProfileImage = encodedProfileImage;
    }

    public String getEncodedProfileImage() {
        return encodedProfileImage;
    }

    public Profile addFriend(String email)
    {
        if(DBAccess.getProfileByEmail(email) != null) {
            //update profile event list
            Firebase friends = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("friends");
            this.friends.add(email);

            friends.setValue(this.friends);
            return DBAccess.getProfileByEmail(email);
        }
        else
        {
            //friend doesn't exist
            return null;
        }
    }

    public Event addEvent(String eventName, String eventDate, String encodedImage, String eventAddress) {
        //add event
        Firebase events = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("events");
        Event event = new Event(eventName,eventDate,encodedImage,eventAddress, id, this.events.size());
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

    public void updateName(String name) {
        this.name = name;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("name");
        db.setValue(name);
    }/*

    public void updateEmail(String email) {
        this.email = email;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("email");
        db.setValue(email);
    }*/

    public void updateFavoriteStore(String favoriteStore) {
        this.favoriteStore = favoriteStore;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("favoriteStore");
        db.setValue(favoriteStore);
    }

    public void updateHobbies(String hobbies) {
        this.hobbies = hobbies;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("hobbies");
        db.setValue(hobbies);
    }

    public void updateBirthDate(String birthDate) {
        this.birthDate = birthDate;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("birthDate");
        db.setValue(birthDate);
    }

    public void updateAnniversaryDate(String anniversaryDate) {
        this.anniversaryDate = anniversaryDate;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("anniversaryDate");
        db.setValue(anniversaryDate);
    }

    public void updateFavoriteRestaurant(String favoriteRestaurant) {
        this.favoriteRestaurant = favoriteRestaurant;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("favoriteRestaurant");
        db.setValue(favoriteRestaurant);
    }

    public void updateFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("favoriteColor");
        db.setValue(favoriteColor);
    }

    public void removeEvent(int eventId) {
        //add event
        Firebase events = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("events");
        this.events.remove(eventId);
        events.setValue(this.events);
    }

    public void addFriendByGoogleId(String googleId) {
        for(Profile profile : DBAccess.getProfiles())
        {
            if(profile.getGoogleId().equals(googleId))
            {
                addFriend(profile.getEmail());
            }
        }
    }

    public String getGoogleId() {
        return googleId;
    }

    public void updateEncodedProfileImage(String encodedProfileImage) {
        this.encodedProfileImage = encodedProfileImage;
        Firebase db = FirebaseDatabase.ref.child("profiles").child(String.valueOf(id)).child("encodedProfileImage");
        db.setValue(favoriteColor);
    }
}
