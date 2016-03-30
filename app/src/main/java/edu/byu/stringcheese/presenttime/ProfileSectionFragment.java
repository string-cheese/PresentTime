package edu.byu.stringcheese.presenttime;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.Profile;

/**
 * Created by dtaylor on 3/20/2016.
 */
public class ProfileSectionFragment extends android.support.v4.app.Fragment {
    private Profile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        /*TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
        dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));*/
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView name_text = (TextView) view.findViewById(R.id.profile_name);
        TextView email_text = (TextView) view.findViewById(R.id.profile_email);
        TextView anniversary_text = (TextView) view.findViewById(R.id.profile_anniversary);
        TextView birthday_text = (TextView) view.findViewById(R.id.profile_birthday);
        TextView favorite_color_text = (TextView) view.findViewById(R.id.profile_favorite_color);
        TextView favorite_store_text = (TextView) view.findViewById(R.id.profile_favorite_store);
        TextView favorite_hobbies_text = (TextView) view.findViewById(R.id.profile_hobbies);
        TextView favorite_restaurant_text = (TextView) view.findViewById(R.id.profile_restaurant);
        final ImageView profile_image_view = (ImageView) view.findViewById(R.id.profile_image);

        String profileId = getArguments().getString("profileId");
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
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.profile_photo_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //profile.updateEncodedProfileImage();
                //profile_image_view.setImageBitmap(BitmapUtils.decodeStringToBitmap(profile.getEncodedProfileImage()));
            }
        });
    }
}
