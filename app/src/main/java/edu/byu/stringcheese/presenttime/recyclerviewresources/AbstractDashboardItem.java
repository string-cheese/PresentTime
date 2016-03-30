package edu.byu.stringcheese.presenttime.recyclerviewresources;

/**
 * Created by liukaichi on 3/24/2016.
 */
public abstract class AbstractDashboardItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_EVENT = 1;
    private int id;
    private int profileId;
    private int name;
    private int date;
    private int photoId;

    abstract public int getType();

    abstract public String getName();

    abstract public String getDateAsString();

    abstract public String getEncodedImage();

    abstract public int getProfileId();

   abstract public int getId();
}
