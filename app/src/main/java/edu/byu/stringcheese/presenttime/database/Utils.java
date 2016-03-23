package edu.byu.stringcheese.presenttime.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by dtaylor on 3/22/2016.
 */
public class Utils {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("M dd/yy");

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

    public static List<Event> getUpcomingEvents(Profile profile) {
        ArrayList<Event> events = new ArrayList<>();
        ArrayList<Profile> friends = getFriends(profile);
        for (Profile friend : friends) {
            //TODO: change this.
        }

        return events;
    }

    class dateEventSorter implements Comparator<Profile> {

        @Override
        public int compare(Profile lhs, Profile rhs) {

            return 0;
        }
    }
}
