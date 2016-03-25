package edu.byu.stringcheese.presenttime.recyclerviewresources;

import edu.byu.stringcheese.presenttime.database.Event;

/**
 * Created by liukaichi on 3/24/2016.
 */
public class DashBoardItem extends AbstractDashboardItem {


    public Event event;

    public DashBoardItem(Event event) {
        this.event = event;
    }

    // here getters and setters
    // for title and so on, built
    // using event


    public void setItem(Event event) {
        this.event = event ;
    }

    @Override
    public int getType() {
        return TYPE_EVENT;
    }

    @Override
    public String getName() {
        return event.getName();
    }

    @Override
    public String getDate() {
        return event.getDate();
    }

    @Override
    public int getPhotoId() {
        return event.getPhotoId();
    }

    @Override
    public int getProfileId() {
        return event.getProfileId();
    }

    @Override
    public int getId() {
        return event.getId();
    }
}
