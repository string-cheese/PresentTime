package edu.byu.stringcheese.presenttime.main.friends;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Profile;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.confirm_add_friend_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getStringExtra("profileId") != null) {
                    Profile profile = DBAccess.getProfile(getIntent().getStringExtra("profileId"));
                    EditText friendEmail = (EditText) findViewById(R.id.friend_email_text);
                    profile.addFriend(friendEmail.getText().toString());
                    finish();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
