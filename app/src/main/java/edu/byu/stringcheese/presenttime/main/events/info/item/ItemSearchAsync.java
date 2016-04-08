package edu.byu.stringcheese.presenttime.main.events.info.item;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import edu.byu.stringcheese.presenttime.BitmapUtils;
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
            JSONObject jsonResult = new JSONObject(res.toString());
            try {
                JSONArray results = jsonResult.getJSONArray("results");
                for (int i = 0; i < results.length(); i++)
                {
                    JSONObject item = results.getJSONObject(i);
                    if(item.has("upc") && item.has("name") && item.has("price")) {
                        String path = "http://www.upcindex.com/" + item.getString("upc");
                        try {
                            Document doc = Jsoup.parse(new URL(path), 1000);
                            Element imageElement = doc.select(".thumbnail img").first();
                            String absoluteUrl = imageElement.absUrl("src");  //absolute URL on src
                            //String srcValue = imageElement.attr("src");  // exact content value of the attribute.
                            jsonResult.getJSONArray("results").getJSONObject(i).put("img", BitmapUtils.encodeBitmapToString(BitmapUtils.decodeSampledBitmapFromWebImage(Uri.parse(absoluteUrl), 512, 512)));
                        }
                        catch(Exception e)
                        {
                            Log.e("ItemSearchAsync",e.getMessage(),e);
                        }
                    }
                    else
                    {
                        Log.d("ItemSearchAsync",String.format("name:%s price:%s upc:%s",item.has("name"),item.has("price"),item.has("upc")));
                    }

                }

            }
            catch(Exception e)
            {
                Log.e("ItemSearchAsync",e.getMessage(),e);
            }
            finally
            {
                return jsonResult;
            }
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
