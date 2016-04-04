package edu.byu.stringcheese.presenttime.main.friends.info;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import edu.byu.stringcheese.presenttime.BitmapUtils;
import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Profile;
import edu.byu.stringcheese.presenttime.main.events.EventsSectionFragment;

public class FriendInfoActivity extends AppCompatActivity {

    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        TextView name_text = (TextView) findViewById(R.id.profile_name);
        TextView email_text = (TextView) findViewById(R.id.profile_email);
        TextView anniversary_text = (TextView) findViewById(R.id.profile_anniversary);
        TextView birthday_text = (TextView) findViewById(R.id.profile_birthday);
        TextView favorite_color_text = (TextView) findViewById(R.id.profile_favorite_color);
        TextView favorite_store_text = (TextView) findViewById(R.id.profile_favorite_store);
        TextView favorite_hobbies_text = (TextView) findViewById(R.id.profile_hobbies);
        TextView favorite_restaurant_text = (TextView) findViewById(R.id.profile_restaurant);
        final ImageView profile_image_view = (ImageView) findViewById(R.id.profile_image);

        if(getIntent().getExtras().containsKey("clientProfileId") && getIntent().getExtras().containsKey("eventOwnerId"))
        {
            String profileId = getIntent().getStringExtra("eventOwnerId");
            profile = DBAccess.getProfile(profileId);
            name_text.setText(profile.getName());
            email_text.setText(profile.getEmail());
            anniversary_text.setText(profile.getAnniversaryDate());
            birthday_text.setText(profile.getBirthDate());
            favorite_color_text.setText(profile.getFavoriteColor());
            favorite_store_text.setText(profile.getFavoriteStore());
            favorite_hobbies_text.setText(profile.getHobbies());
            favorite_restaurant_text.setText(profile.getFavoriteRestaurant());
            profile_image_view.setImageBitmap(BitmapUtils.decodeStringToBitmap(profile.getEncodedProfileImage()));
            Bundle args = new Bundle();
            args.putString("clientProfileId", getIntent().getStringExtra("clientProfileId"));
            args.putString("eventOwnerId", getIntent().getStringExtra("eventOwnerId"));

            FragmentTransaction t = getSupportFragmentManager().beginTransaction();


            //Create and add a fragment to frame layout created above.
            EventsSectionFragment eventsFragment = new EventsSectionFragment();
            eventsFragment.setArguments(args);

            t.add(R.id.friend_info_layout, eventsFragment, "eventsFragment");
            t.commit();
        }
    }

}
