package edu.byu.stringcheese.presenttime;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.http.HttpResponse;

public class ItemSearchAsync extends AsyncTask<String, Object, JSONObject>
{
    private ItemSearchListener listener;

    public ItemSearchAsync(ItemSearchListener listener)
    {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        HttpResponse response = null;
        try {
            String API_BASE = "https://api.semantics3.com/test/v1/";
            String endpoint = "products";
            String req = new StringBuffer()
                    .append(API_BASE)
                    .append(endpoint)
                    .append("?q=")
                    .append(URLEncoder.encode("{\"search\":\"" + params[0] + "\",\"offset\":"+params[1]+"}", "UTF-8"))
                    .toString();
            URL url = new URL(req);
            url = url.toURI().normalize().toURL();
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestProperty("User-Agent", "Semantics3 Java Library");
            request.setRequestMethod("GET");
            OAuthConsumer consumer = new DefaultOAuthConsumer("SEM39C02671BF6BA036399786C7DB60A91C1", "MDdjMTkzZGNkMzM0YjdiZjRmMjk1Zjc3Zjg1NDJjZDI");
            consumer.sign(request);
            request.connect();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder res = new StringBuilder();
            String line;
            while ((line = streamReader.readLine()) != null)
                res.append(line);
            return new JSONObject(res.toString());
        } catch(Exception e)
        {
            Log.e("ItemSearchActivity", "Failure to get search results", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        listener.onSearchComplete(jsonObject);
    }
}
