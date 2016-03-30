package edu.byu.stringcheese.presenttime.database;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.byu.stringcheese.presenttime.BitmapUtils;
import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.recyclerviewresources.AbstractDashboardItem;
import edu.byu.stringcheese.presenttime.recyclerviewresources.DashBoardItem;
import edu.byu.stringcheese.presenttime.recyclerviewresources.DashboardHeader;

/**
 * Created by dtaylor on 3/22/2016.
 */
public class DBAccess {
    private static final int NUM_UPCOMING_EVENTS_SHOWN = 3;

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
        ArrayList<Profile> profiles = FirebaseDatabase.getInstance().getProfiles();
        if(profiles.size() > profileId)
        return profiles.get(profileId);
        return null;
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

    public static Event getEvent(String profileId, String eventId) {
        ArrayList<Event> events = getEvents(profileId);
        if(events != null)
            return events.get(Integer.parseInt(eventId));
        return null;
    }

    private static ArrayList<Event> getEvents(String profileId) {
        return getProfile(profileId).getEvents();
    }

    public static Profile getProfile(String profileId) {
        return getProfile(Integer.parseInt(profileId));
    }

    public static Profile addProfile(String name, String email, String googleId, String store, String hobbies, String birthday, String anniversary, String restaurant, String favoriteColor, String encodedProfileImage) {
        return FirebaseDatabase.getInstance().addProfile(name, email, googleId, store, hobbies, birthday, anniversary, restaurant, favoriteColor, encodedProfileImage);
    }

    public static ArrayList<Event> getEvents(int profileId) {
        Profile profile = getProfile(profileId);
        if(profile != null)
            return profile.getEvents();
        return null;
    }

    public static List<Profile> getFriends(String profileId) {
        return getFriends(getProfile(profileId));
    }

    public static List<AbstractDashboardItem> getUpcomingEventsItems(Profile profile) {
        List<AbstractDashboardItem> result = new ArrayList<>();
        result.add(new DashboardHeader("Upcoming Events"));

        List<Event> allEvents = new ArrayList<>();
        for (Profile friend: getFriends(profile))
        {
            if(friend != null) {
                ArrayList<Event> events = friend.getEvents();
                if (events != null) {
                    allEvents.addAll(events);
                }
            }
        }

        Collections.sort(allEvents, Collections.reverseOrder());

        for (int i = 0; i < NUM_UPCOMING_EVENTS_SHOWN; i++)
        {
            if (allEvents.size() == i) break;
            result.add(new DashBoardItem(allEvents.get(i)));

        }
        return result;
    }

    public static void clear() {
        FirebaseDatabase.getInstance().remove();
    }

    public static ArrayList<Item> getItems(Event event) {
        Event actualEvent = DBAccess.getEvent(String.valueOf(event.getProfileId()),String.valueOf(event.getId()));
        if(actualEvent != null)
            return actualEvent.getItems();
        return null;
    }

    public static void fakeData(Resources resources)
    {
        Profile profile = DBAccess.addProfile("Justin", "justin@cool.com", "googleId", "Cotton On", "Singing, Longboarding, Hacking", "June 7th", "August 12th", "Station 22", "Navy Blue", BitmapUtils.encodeResourceToString(resources,R.drawable.justin_profile, 512, 512));

        Event event = profile.addEvent("Justin's Wedding", "June 15th, 2016", BitmapUtils.encodeResourceToString(resources,R.drawable.wedding, 512, 512), "SLC Temple");
        event.addItem("F150",100000,"Ford",BitmapUtils.encodeResourceToString(resources,R.drawable.f150, 512, 512), false);
        event.addItem("Apple Watch",400,"Apple",BitmapUtils.encodeResourceToString(resources,R.drawable.applewatch, 512, 512), false);
        event.addItem("KitchenAid Mixer",230,"Kohls",BitmapUtils.encodeResourceToString(resources,R.drawable.kitchenaid, 512, 512), false);


        Profile profile2 = DBAccess.addProfile("Amanda","amanda@cool.com", "googleId", "J. Crew", "Design, Hiking, Music, Videography, Travel", "June 7th", "NA", "Station 22", "Teal", BitmapUtils.encodeResourceToString(resources,R.mipmap.ic_amanda, 512, 512));

        Event eventa = profile2.addEvent("Amanda's Graduation", "August 11th, 2016", BitmapUtils.encodeResourceToString(resources,R.drawable.graduation, 512, 512), "BYU Wilkinson Ballroom");
        eventa.addItem("MacBook Pro",2500,"Apple",BitmapUtils.encodeResourceToString(resources,R.drawable.macbook, 512, 512), false);
        eventa.addItem("Arvo Watch",90,"ArvoWear",BitmapUtils.encodeResourceToString(resources,R.drawable.arvo, 512, 512), false);
        eventa.addItem("Phantom 4 Drone",1999,"Scheels",BitmapUtils.encodeResourceToString(resources,R.drawable.drone, 512, 512), false);

        Event event2a = profile2.addEvent("Amanda's '22' Birthday", "June 7th, 2016", BitmapUtils.encodeResourceToString(resources,R.drawable.balloon, 512, 512), "Belmont Condos");
        event2a.addItem("Phantom 4", 1999, "Scheels", BitmapUtils.encodeResourceToString(resources,R.drawable.drone, 512, 512), false);
        event2a.addItem("G Series",119000,"Mercedese Benz",BitmapUtils.encodeResourceToString(resources,R.drawable.mercedes, 512, 512), false);
        event2a.addItem("MacBook Pro",2500,"Apple",BitmapUtils.encodeResourceToString(resources,R.drawable.macbook, 512, 512), false);

        Event event3a = profile2.addEvent("Amanda's Christmas", "December 25th, 2016", BitmapUtils.encodeResourceToString(resources,R.drawable.christmas, 512, 512), "Draper, UT");
        event3a.addItem("Apple Watch", 0, "Apple", BitmapUtils.encodeResourceToString(resources,R.drawable.applewatch, 512, 512), false);
        event3a.addItem("Puppy",600,"Humane Society of Utah",BitmapUtils.encodeResourceToString(resources,R.drawable.puppy, 512, 512), false);
        event3a.addItem("iPhone 6s",0,"Apple",BitmapUtils.encodeResourceToString(resources,R.drawable.iphone, 512, 512), false);

        Profile profile3 = DBAccess.addProfile("Sam","sam@cool.com", "googleId", "Amazon", "Business Startups", "December 17th", "NA", "Bam Bam's BBQ", "Army Green", BitmapUtils.encodeResourceToString(resources,R.mipmap.david_profile, 512, 512));

        Event eventb = profile3.addEvent("Sam's '25' Birthday", "December 17th, 2016", BitmapUtils.encodeResourceToString(resources,R.drawable.balloon, 512, 512), "Buffalo Wild Wings");
        eventb.addItem("F150",100000,"Ford",BitmapUtils.encodeResourceToString(resources,R.drawable.f150, 512, 512), false);
        eventb.addItem("iPhone 6s Plus", 900, "Apple", BitmapUtils.encodeResourceToString(resources,R.drawable.iphone, 512, 512), false);
        eventb.addItem("MacBook Pro",2500,"Apple",BitmapUtils.encodeResourceToString(resources,R.drawable.macbook, 512, 512), false);

        Event event2b = profile3.addEvent("Sam's Wedding", "October 7th, 2016", BitmapUtils.encodeResourceToString(resources,R.drawable.wedding, 512, 512), "SLC Temple");
        event2b.addItem("Puppy", 600, "Humane Society of Utah", BitmapUtils.encodeResourceToString(resources,R.drawable.puppy, 512, 512), false);
        event2b.addItem("Phantom 4 Drone",2000,"Scheels",BitmapUtils.encodeResourceToString(resources,R.drawable.drone, 512, 512), false);
        event2b.addItem("KitchenAid Mixer", 230, "Kohls", BitmapUtils.encodeResourceToString(resources,R.drawable.kitchenaid, 512, 512), false);

//        Event event3b = profile3.addEvent("Sam's Bachelor Party", "October 6th, 2016", R.drawable.balloon, "24221 Sagewood Dr., Provo Utah");
//        event3b.addItem("Dr. Pepper", 10, "Target", R.drawable.ic_star_black_24dp, false);
//        event3b.addItem("Balloons",10,"Zurchers",R.drawable.ic_star_black_24dp, false);
//        event3b.addItem("Pizza",50,"SLAB Pizza",R.drawable.ic_launcher, false);


    }
    public static void fakeData2() {
        DBAccess.getProfileByEmail("justin@cool.com").addFriend("sam@cool.com");
        DBAccess.getProfileByEmail("justin@cool.com").addFriend("amanda@cool.com");
    }
}
