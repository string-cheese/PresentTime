package edu.byu.stringcheese.presenttime;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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

        // Adding items to listview
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.product_name, products);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void search(String content) {
        Utils.searchItemAsync(content, new ItemSearchListener(){

            @Override
            public void onSearchComplete(JSONObject jsonObject) {
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
        });
    }
}

