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
        if(_instance == null)
            _instance = new FirebaseDatabase();
        return _instance;
    }

    public static void setInstance(FirebaseDatabase db)
    {
        if(_instance == null)
            _instance = db;
    }

    public FirebaseDatabase()
    {

    }

    public Profile addProfile(String name, String email, String store, String hobbies, String birthday, String annviversary, String restaurant, String favoriteColor)
    {
        //add item
        Firebase profiles = FirebaseDatabase.ref.child("profiles");
        Profile profile = new Profile(DBAccess.getProfiles().size(), name, email, store, hobbies, birthday, annviversary, restaurant, favoriteColor);
        DBAccess.getProfiles().add(profile);
        profiles.setValue(DBAccess.getProfiles());
        return profile;
    }

    public ArrayList<Profile> getProfiles() {
        return profiles;
    }

    public static boolean hasInstance() {
        return _instance != null;
    }
    public static Firebase ref;
    public static boolean makeFakeData = false;


    public static void initializeFirebase(Context context) {
        Firebase.setAndroidContext(context);
        ref = new Firebase("https://crackling-fire-2441.firebaseio.com/present-time");
        if(makeFakeData)
            DBAccess.fakeData();
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


//OUTSIDE
class DatabaseValueEventListener extends Observable implements ValueEventListener {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        //System.out.println("There are " + dataSnapshot.getChildrenCount() + " blog posts");
        if (dataSnapshot.getKey() != null && dataSnapshot.getKey().equals("present-time")) {
            GenericTypeIndicator<FirebaseDatabase> t = new GenericTypeIndicator<FirebaseDatabase>() {};
            FirebaseDatabase dbTest = dataSnapshot.getValue(t);
            FirebaseDatabase.setInstance(dbTest);
            //NOTIFY OBSERVERS
            setChanged();
            notifyObservers();
            if(FirebaseDatabase.makeFakeData)
            {
                DBAccess.fakeData2();
                FirebaseDatabase.makeFakeData = false;
            }
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
            /*GenericTypeIndicator<Item> t1 = new GenericTypeIndicator<Item>(){};
            Item item = dataSnapshot.child("profiles").child("0").child("events").child("0").child("items").child("0").getValue(t1);

            GenericTypeIndicator<ArrayList<Item>> t2 = new GenericTypeIndicator<ArrayList<Item>>(){};
            ArrayList<Item> items = dataSnapshot.child("profiles").child("0").child("events").child("0").child("items").getValue(t2);

            GenericTypeIndicator<ArrayList<Event>> t3 = new GenericTypeIndicator<ArrayList<Event>>() {};
            ArrayList<Event> events = dataSnapshot.child("profiles").child("0").child("events").getValue(t3);

            GenericTypeIndicator<ArrayList<Profile>> t4 = new GenericTypeIndicator<ArrayList<Profile>>() {};
            ArrayList<Profile> profiles = dataSnapshot.child("profiles").getValue(t4);*/

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
            GenericTypeIndicator<FirebaseDatabase> t = new GenericTypeIndicator<FirebaseDatabase>() {};
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


