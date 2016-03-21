package edu.byu.stringcheese.presenttime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dtaylor on 3/20/2016.
 */
public class FriendsSectionFragment extends android.support.v4.app.Fragment {
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        List<Database.Profile> possibleFriends = Database.getInstance().getProfiles().subList(1, Database.getInstance().getProfiles().size() - 1);
        recyclerView.setAdapter(new FriendsListViewAdapter(possibleFriends, mListener));

    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Database.Profile profile);
    }

    class FriendsListViewAdapter extends RecyclerView.Adapter<FriendsListViewAdapter.FriendViewHolder> {
        private final List<Database.Profile> shownProfiles;
        private final OnListFragmentInteractionListener mListener = null;

        public FriendsListViewAdapter(List<Database.Profile> profiles, OnListFragmentInteractionListener listener) {
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
            holder.profileId = shownProfiles.get(position).getProfileId();

            holder.profile = shownProfiles.get(position);
            //holder.mIdView.setText(shownProfiles.get(position).id);
            holder.mContentView.setText(shownProfiles.get(position).getName());


        }

        @Override
        public int getItemCount() {
            return shownProfiles.size();
        }

        public class FriendViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            //public final TextView mIdView;
            public final TextView mContentView;
            public Database.Profile profile;
            public int profileId;
            public int currentItem;

            public FriendViewHolder(View view) {
                super(view);
                mView = view;
                //mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w("dot", v.toString());
                        Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
                        intent.putExtra("profileId", profileId);
                        startActivity(intent);

//                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        // mListener.onListFragmentInteraction(holder.profile);
                        //                  }
                    }
                });
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}


