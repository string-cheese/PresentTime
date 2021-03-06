package edu.byu.stringcheese.presenttime.main.events;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.byu.stringcheese.presenttime.BitmapUtils;
import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Profile;

public class AddEventActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "AddEventActivity";
    private Profile profile;
    Calendar myCalendar = Calendar.getInstance();
    EditText editText;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        if(getIntent().getStringExtra("clientProfileId") != null)
        {
            profile = DBAccess.getProfile(Integer.parseInt(getIntent().getStringExtra("clientProfileId")));
        }
        else
        {
            Snackbar.make(this.getCurrentFocus(), "Something is wrong, this doesn't exist", Snackbar.LENGTH_LONG)
                    .setAction("Go Back", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
        }
        editText = (EditText) findViewById(R.id.add_event_date);

        findViewById(R.id.add_event_done).setOnClickListener(this);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        editText.setInputType(InputType.TYPE_NULL);
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });/*
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    new DatePickerDialog(AddEventActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }

            }
        });*/

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddEvent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.byu.stringcheese.presenttime/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddEvent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.byu.stringcheese.presenttime/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_event_done:
                addNewEvent();
        }
    }

    private void addNewEvent() {
        Log.d(TAG, "adding new event...");
        String eventName = ((EditText) findViewById(R.id.add_event_name)).getText().toString();
        String eventDate = ((EditText) findViewById(R.id.add_event_date)).getText().toString();
        String eventType = ((Spinner) findViewById(R.id.eventType)).getSelectedItem().toString();
        String eventAddress = ((EditText) findViewById(R.id.add_event_address)).getText().toString();

        int eventImageId = R.drawable.balloon;
        if (eventType.equals("Wedding"))
            eventImageId = R.mipmap.cake;
        if (eventType.equals("Christmas"))
            eventImageId = R.mipmap.gifts;
        if(eventName.length() > 0 && eventDate.length() > 0 && eventType.length() > 0 && eventAddress.length() > 0) {
            profile.addEvent(eventName, eventDate, BitmapUtils.encodeResourceToString(getResources(), eventImageId, 512, 512), eventAddress);
            //SlidingTabsBasicFragment.mViewPager.setCurrentItem(1);
            finish();
        }
        else
        {
            Snackbar.make(findViewById(R.id.add_event_root_view),"All fields are required",Snackbar.LENGTH_LONG).show();
        }
    }
}
