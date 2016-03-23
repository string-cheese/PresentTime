package edu.byu.stringcheese.presenttime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Utils;

/**
 * Created by dtaylor on 3/20/2016.
 */
public class FriendsSectionFragment extends android.support.v4.app.Fragment implements Observer {
    private RecyclerView recyclerView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.friends_section_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.friends_rv);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<FirebaseDatabase.Profile> friends = Utils.getFriends(MainActivity.myProfile);
        recyclerView.setAdapter(new FriendsListViewAdapter(friends));

    }

    @Override
    public void update(Observable observable, Object data) {
        if(recyclerView.getAdapter() != null)
        {
            ((FriendsListViewAdapter)recyclerView.getAdapter()).updateEventsShown(Utils.getFriends(MainActivity.myProfile));
            recyclerView.invalidate();
        }
    }

    class FriendsListViewAdapter extends RecyclerView.Adapter<FriendsListViewAdapter.FriendViewHolder> {
        List<FirebaseDatabase.Profile> shownProfiles;

        public FriendsListViewAdapter(List<FirebaseDatabase.Profile> profiles) {
            shownProfiles = profiles;
        }

        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friend_template, parent, false);


            return new FriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FriendViewHolder holder, int position) {
            holder.currentItem = position;
            holder.profileId = String.valueOf(shownProfiles.get(position).getId());
            holder.friendName.setText(shownProfiles.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return shownProfiles.size();
        }

        public void updateEventsShown(ArrayList<FirebaseDatabase.Profile> friends) {
            this.shownProfiles.clear();
            this.shownProfiles.addAll(friends);
            notifyDataSetChanged();
        }

        public class FriendViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView friendName;
            public String profileId;
            public int currentItem;

            public FriendViewHolder(View view) {
                super(view);
                mView = view;
                friendName = (TextView) view.findViewById(R.id.friend_name);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
                        intent.putExtra("profileId", profileId);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}


