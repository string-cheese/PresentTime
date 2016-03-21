package edu.byu.stringcheese.presenttime;


import java.util.ArrayList;

/**
 * Created by dtaylor on 3/21/2016.
 */
public class Database {
    ArrayList<Profile> profiles = new ArrayList<>();

    public class Profile
    {
        ArrayList<Event> events = new ArrayList<>();
        String name;
    }
}

