package edu.byu.stringcheese.presenttime.main.events.info.item;

import org.json.JSONObject;

public interface ItemSearchListener {
    void onSearchComplete(JSONObject object);
}
