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
            _instance = db;
    }

    public FirebaseDatabase()
    {

    }

    public Profile addProfile(String name, String email, String googleId, String store, String hobbies, String birthday, String annviversary, String restaurant, String favoriteColor)
    {
        //add item
        Firebase profiles = FirebaseDatabase.ref.child("profiles");
        Profile profile = new Profile(DBAccess.getProfiles().size(), name, email, googleId, store, hobbies, birthday, annviversary, restaurant, favoriteColor);
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
    public static boolean makeFakeData = true;
    public static String databaseName = "present-time-test";


    public static void initializeFirebase(Context context) {
        Firebase.setAndroidContext(context);
        ref = new Firebase("https://crackling-fire-2441.firebaseio.com/"+FirebaseDatabase.databaseName);
        if(makeFakeData) {
            DBAccess.clear();
            DBAccess.fakeData();
        }
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


    public void remove() {
        ref.removeValue();
    }
}


//OUTSIDE
class DatabaseValueEventListener extends Observable implements ValueEventListener {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        //System.out.println("There are " + dataSnapshot.getChildrenCount() + " blog posts");
        if (dataSnapshot.getKey() != null && dataSnapshot.getKey().equals(FirebaseDatabase.databaseName)) {
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
        if (dataSnapshot.getKey().equals(FirebaseDatabase.databaseName)) {
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
        if (dataSnapshot.getKey().equals(FirebaseDatabase.databaseName)) {
            GenericTypeIndicator<FirebaseDatabase> t = new GenericTypeIndicator<FirebaseDatabase>() {};
            FirebaseDatabase dbTest = dataSnapshot.getValue(t);
            FirebaseDatabase.setInstance(dbTest);
            setChanged();
            notifyObservers();
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


