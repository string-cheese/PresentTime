package edu.byu.stringcheese.presenttime.main.dashboard.recyclerviewresources;

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
    public String getDateAsString() {
        return event.dateAsString();
    }

    @Override
    public String getEncodedImage() {
        return event.getEncodedImage();
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
