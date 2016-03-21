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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        /*TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
        dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));*/

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.friends_rv);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(new FriendsListViewAdapter(DummyContent.ITEMS, mListener));

    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.DummyItem item);
    }

    class FriendsListViewAdapter extends RecyclerView.Adapter<FriendsListViewAdapter.ViewHolder> {
        private final List<DummyContent.DummyItem> mValues;
        private final OnListFragmentInteractionListener mListener = null;

        public FriendsListViewAdapter(List<DummyContent.DummyItem> items, OnListFragmentInteractionListener listener) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friend_template, parent, false);
            view.setOnClickListener(new MyOnClickListener());

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            //holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w("dot", v.toString());
                    Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
                    intent.putExtra("name", "Trent Jones");
                    startActivity(intent);

//                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                       // mListener.onListFragmentInteraction(holder.mItem);
  //                  }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            //public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                //mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}

class MyOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Log.w("view", v.toString());
    }
}

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
class DummyContent {

    /**
     * An array of sample (dummy) eventItems.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) eventItems, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        addItem(new DummyItem("Lawrie Branden", "Lawrie Branden", "Events"));
        addItem(new DummyItem("Phebe Johanna", "Phebe Johanna", "Events"));
        addItem(new DummyItem("Phillipa Edwyn", "Phillipa Edwyn", "Events"));
        addItem(new DummyItem("Maitland Justice", "Maitland Justice", "Events"));
        addItem(new DummyItem("Paget Nicky", "Paget Nicky", "Events"));
        addItem(new DummyItem("Ansel Tahnee", "Ansel Tahnee", "Events"));
        addItem(new DummyItem("Charla Raymund", "Charla Raymund", "Events"));
        addItem(new DummyItem("Temple Baxter", "Temple Baxter", "Events"));
        addItem(new DummyItem("Polly Beauregard", "Polly Beauregard", "Events"));
        addItem(new DummyItem("Jade Charles", "Jade Charles", "Events"));
        addItem(new DummyItem("Staci Leanora", "Staci Leanora", "Events"));
        addItem(new DummyItem("Gordie Kaleb", "Gordie Kaleb", "Events"));
        addItem(new DummyItem("Thurstan Jodi", "Thurstan Jodi", "Events"));
        addItem(new DummyItem("Stu Nat", "Stu Nat", "Events"));
        addItem(new DummyItem("Georgia Kristia", "Georgia Kristia", "Events"));
        addItem(new DummyItem("Anson Kirby", "Anson Kirby", "Events"));
        addItem(new DummyItem("Charis Orinda", "Charis Orinda", "Events"));
        addItem(new DummyItem("Landen Anastasia", "Landen Anastasia", "Events"));
        addItem(new DummyItem("Sheryll Penelope", "Sheryll Penelope", "Events"));
        addItem(new DummyItem("Niles Mitchell", "Niles Mitchell", "Events"));
        addItem(new DummyItem("Carrie Fred", "Carrie Fred", "Events"));
        addItem(new DummyItem("Jarrod Genevieve", "Jarrod Genevieve", "Events"));
        addItem(new DummyItem("Nikole Saffron", "Nikole Saffron", "Events"));
        addItem(new DummyItem("Grenville Jerrard", "Grenville Jerrard", "Events"));
        addItem(new DummyItem("Jay Mickey", "Jay Mickey", "Events"));


    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}

