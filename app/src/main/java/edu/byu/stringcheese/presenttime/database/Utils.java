package edu.byu.stringcheese.presenttime.database;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by dtaylor on 3/22/2016.
 */
public class Utils {

    public static ArrayList<FirebaseDatabase.Profile> getFriends(FirebaseDatabase.Profile profile) {
        ArrayList<FirebaseDatabase.Profile> friends = new ArrayList<>();
        for(String friendId : profile.getFriends())
        {
            friends.add(getProfileByEmail(friendId));
        }
        return friends;
    }

    public static FirebaseDatabase.Profile getProfile(int profileId)
    {
        return FirebaseDatabase.getInstance().getProfiles().get(profileId);
    }

    public static FirebaseDatabase.Profile getProfileByEmail(String email) {
        for(FirebaseDatabase.Profile profile : FirebaseDatabase.getInstance().getProfiles())
        {
            if(profile.getEmail().equals(email))
            {
                return profile;
            }
        }
        return null;
    }
}
