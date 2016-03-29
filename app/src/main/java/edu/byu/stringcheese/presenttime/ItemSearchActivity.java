package edu.byu.stringcheese.presenttime;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;

public class ItemSearchActivity extends AppCompatActivity {
    // List view
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> adapter;
    // Search EditText
    EditText inputSearch;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    ArrayList<String> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Listview Data
        products = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    search(v.getText().toString());
                    return false;
            }
        });
        /*inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //ItemSearchActivity.this.adapter.getFilter().filter(cs);
                search(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });*/

        // Adding items to listview
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.product_name, products);
        lv.setAdapter(adapter);

        /*productsApi = new Products(
                "SEM39C02671BF6BA036399786C7DB60A91C1",
                "MDdjMTkzZGNkMzM0YjdiZjRmMjk1Zjc3Zjg1NDJjZDI"
        );*/
    }

    private void search(String content) {
        try {
            String query = "{\"search\":\""+content+"\"}";
            new AsyncTask<String,Void,JSONObject>()
            {

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
                                .append(URLEncoder.encode(params[0], "UTF-8"))
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
                        Log.e("ItemSearchActivity","Failure to get search results",e);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    products.clear();
                    try {
                        JSONArray productsArray = jsonObject.getJSONArray("results");
                        for(int i = 0; i < productsArray.length(); i++)
                        {
                            JSONObject prod = productsArray.getJSONObject(i);
                            products.add(prod.getString("name"));
                            if(i==10)
                                break;
                        }
                        lv.setAdapter(new ArrayAdapter<>(ItemSearchActivity.this, R.layout.list_item, R.id.product_name, products));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(query);
        }
        catch(Exception e)
        {
            Log.e(this.getClass().getName(),"error searching for product",e);
        }
    }

}
