package edu.byu.stringcheese.presenttime;

/**
 * Created by amandafisher on 3/20/16.
 */
public class Event {

    String eventName;
    String eventDate;
    int eventPhotoID;

    Event(String eventName, String eventDate, int eventPhotoID) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventPhotoID = eventPhotoID;
    }
}