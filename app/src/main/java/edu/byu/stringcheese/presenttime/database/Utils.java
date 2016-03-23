package edu.byu.stringcheese.presenttime.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dtaylor on 3/22/2016.
 */
public class Utils {

    private static List<Profile> profiles;

    public static ArrayList<Profile> getFriends(Profile profile) {
        ArrayList<Profile> friends = new ArrayList<>();
        for(String friendId : profile.getFriends())
        {
            friends.add(getProfileByEmail(friendId));
        }
        return friends;
    }

    public static Profile getProfile(int profileId)
    {
        return FirebaseDatabase.getInstance().getProfiles().get(profileId);
    }

    public static Profile getProfileByEmail(String email) {
        for(Profile profile : FirebaseDatabase.getInstance().getProfiles())
        {
            if(profile.getEmail().equals(email))
            {
                return profile;
            }
        }
        return null;
    }

    public static ArrayList<Profile> getProfiles() {
        return FirebaseDatabase.getInstance().getProfiles();
    }
}
