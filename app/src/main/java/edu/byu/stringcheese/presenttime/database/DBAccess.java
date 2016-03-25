package edu.byu.stringcheese.presenttime.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.recyclerviewresources.AbstractDashboardItem;
import edu.byu.stringcheese.presenttime.recyclerviewresources.DashBoardItem;
import edu.byu.stringcheese.presenttime.recyclerviewresources.DashboardHeader;

/**
 * Created by dtaylor on 3/22/2016.
 */
public class DBAccess {
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

    public static Event getEvent(String profileId, String eventId) {
        return getEvents(profileId).get(Integer.parseInt(eventId));
    }

    private static ArrayList<Event> getEvents(String profileId) {
        return getProfile(profileId).getEvents();
    }

    public static Profile getProfile(String profileId) {
        return getProfile(Integer.parseInt(profileId));
    }

    public static Profile addProfile(String name, String email, String googleId, String store, String hobbies, String birthday, String anniversary, String restaurant, String favoriteColor) {
        return FirebaseDatabase.getInstance().addProfile(name, email, googleId, store, hobbies, birthday, anniversary, restaurant, favoriteColor);
    }

    public static ArrayList<Event> getEvents(int profileId) {
        return getProfile(profileId).getEvents();
    }

    public static List<Profile> getFriends(String profileId) {
        return getFriends(getProfile(profileId));
    }

    public static List<AbstractDashboardItem> getUpcomingEventsItems(Profile profile) {
        List<AbstractDashboardItem> result = new ArrayList<>();
        result.add(new DashboardHeader("Upcoming Events"));
        for (Event event : getProfile(profile.getId()).getEvents())
        {
            result.add(new DashBoardItem(event));
        }
        return result;
    }

    public static void clear() {
        FirebaseDatabase.getInstance().remove();
    }

    class dateEventSorter implements Comparator<Profile> {

        @Override
        public int compare(Profile lhs, Profile rhs) {

            return 0;
        }
    }
    public static void fakeData()
    {
        Profile profile = DBAccess.addProfile("Justin", "justin@cool.com", "googleId", "J. Crew", "Singing, Longboarding, Hacking", "June 7th", "August 12th", "Station 22", "Navy Blue");

        Event event = profile.addEvent("Justin's Wedding", "June 15th, 2016", R.mipmap.wedding, "SLC Temple");
        event.addItem("Car",15000,"Toyota",R.mipmap.car, false);
        event.addItem("Boat",7000,"The Dock",R.mipmap.boat, false);
        event.addItem("Cake",100,"Cakes And More",R.mipmap.cake, false);
        event.addItem("Cake2",100,"Cakes And More",R.mipmap.cake, false);
        event.addItem("Cake3", 100, "Cakes And More", R.mipmap.cake, false);
        event.addItem("Cake4", 100, "Cakes And More", R.mipmap.cake, false);

        Event event2 = profile.addEvent("Sam's 25th Birthday", "December 17th, 2016", R.mipmap.balloon, "Buffalo Wild Wings");
        event2.addItem("House",105000,"Toyota",R.mipmap.house, false);
        event2.addItem("Pancake",7000,"The Dock",R.mipmap.pancake, false);
        event2.addItem("Dog",100,"Cakes And More",R.mipmap.dog, false);
        event2.addItem("Mouse", 100, "Cakes And More", R.mipmap.mouse, false);
        event2.addItem("Card Game", 100, "Cakes And More", R.mipmap.card, false);
        event2.addItem("Bike", 100, "Cakes And More", R.mipmap.bike, false);

        Event event3 = profile.addEvent("Amanda's Graduation", "August 11th, 2016", R.mipmap.graduation, "BYU");
        event3.addItem("House2",105000,"Toyota",R.drawable.ic_media_pause, false);
        event3.addItem("Pancake3",7000,"The Dock",R.drawable.ic_star_black_24dp, false);
        event3.addItem("Dog4",100,"Cakes And More",R.drawable.ic_launcher, false);
        event3.addItem("Mouse5",100,"Cakes And More",R.drawable.ic_launcher, false);
        event3.addItem("Card Game6", 100, "Cakes And More", R.drawable.ic_launcher, false);
        event3.addItem("Bike7", 100, "Cakes And More", R.drawable.ic_launcher, false);




        Profile profile2 = DBAccess.addProfile("Joe","joe@cool.com", "googleId", "Amazon", "Shooting Games", "December 30th", "August 16th", "Ko Ko's Korean", "Orange");

        Event eventa = profile2.addEvent("Joe first", "June 15th, 2016", R.drawable.balloon, "an address");
        eventa.addItem("Tic",0,"Tac",R.drawable.ic_media_pause, false);
        eventa.addItem("Tac",25,"The Dock",R.drawable.ic_star_black_24dp, false);
        eventa.addItem("Toe",178,"Cakes And More",R.drawable.ic_launcher, false);

        Event event2a = profile2.addEvent("Joe second", "December 17th, 2016", R.drawable.balloon, "1942 columnus");
        event2a.addItem("Kitty", 65, "Toyota", R.drawable.ic_media_pause, false);
        event2a.addItem("Dr. Who Stuff",71245000,"The Dock",R.drawable.ic_star_black_24dp, false);
        event2a.addItem("Monkey",102340,"Cakes And More",R.drawable.ic_launcher, false);

        Event event3a = profile2.addEvent("Joe third", "August 11th, 2016", R.drawable.balloon, "24221 Sagewood dr., Provo Utah");
        event3a.addItem("House2", 0, "Toyota", R.drawable.ic_media_pause, false);
        event3a.addItem("Pancake3",0,"The Dock",R.drawable.ic_star_black_24dp, false);
        event3a.addItem("Dog4",0,"Cakes And More",R.drawable.ic_launcher, false);

        Profile profile3 = DBAccess.addProfile("Bob","bob@cool.com", "googleId", "Amazon", "Shooting Games", "December 30th", "August 16th", "Ko Ko's Korean", "Orange");

        Event eventb = profile3.addEvent("Joe's Wedding", "June 11th, 2016", R.drawable.balloon, "an address");
        eventb.addItem("Tic",0,"Tac",R.drawable.ic_media_pause, false);
        eventb.addItem("Tac", 25, "The Dock", R.drawable.ic_star_black_24dp, false);
        eventb.addItem("Toe",178,"Cakes And More",R.drawable.ic_launcher, false);

        Event event2b = profile3.addEvent("Billy's 23th Birthday", "December 17th, 2016", R.drawable.balloon, "1942 columnus");
        event2b.addItem("Kitty", 65, "Toyota", R.drawable.ic_media_pause, false);
        event2b.addItem("Dr. Who Stuff",71245000,"The Dock",R.drawable.ic_star_black_24dp, false);
        event2b.addItem("Monkey", 102340, "Cakes And More", R.drawable.ic_launcher, false);

        Event event3b = profile3.addEvent("Amanda's Graduation", "August 11th, 2016", R.drawable.balloon, "24221 Sagewood dr., Provo Utah");
        event3b.addItem("House2", 0, "Toyota", R.drawable.ic_media_pause, false);
        event3b.addItem("Pancake3",0,"The Dock",R.drawable.ic_star_black_24dp, false);
        event3b.addItem("Dog4",0,"Cakes And More",R.drawable.ic_launcher, false);


    }
    public static void fakeData2() {
        DBAccess.getProfileByEmail("justin@cool.com").addFriend("joe@cool.com");
        DBAccess.getProfileByEmail("justin@cool.com").addFriend("bob@cool.com");
    }
}
