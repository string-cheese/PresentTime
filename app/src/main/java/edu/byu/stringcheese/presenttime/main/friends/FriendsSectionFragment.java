package edu.byu.stringcheese.presenttime.main.friends;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseDrawer;
import com.github.amlcurran.showcaseview.ShowcaseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.byu.stringcheese.presenttime.BitmapUtils;
import edu.byu.stringcheese.presenttime.FragmentHolderActivity;
import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.RecyclerViewTarget;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Profile;

/**
 * Created by dtaylor on 3/20/2016.
 */
public class FriendsSectionFragment extends android.support.v4.app.Fragment implements Observer {
    private RecyclerView recyclerView = null;
    private Profile profile;
    private int itemPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        return rootView;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Delete Friend") {
            profile.removeFriend(itemPosition);
            Toast.makeText(getActivity(), "Friend Removed", Toast.LENGTH_SHORT).show();
        }
        else if (item.getTitle() == "Action 2") {
            Toast.makeText(getActivity(), "Action 2 invoked", Toast.LENGTH_SHORT).show();
        }
        else if (item.getTitle() == "Action 3") {
            Toast.makeText(getActivity(), "Action 3 invoked", Toast.LENGTH_SHORT).show();
        }
        else {
            return false;
        }
        return true;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.friends_rv);
        registerForContextMenu(recyclerView);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(getArguments()!=null && getArguments().getString("clientProfileId") != null)
        {
            profile = DBAccess.getProfile(getArguments().getString("clientProfileId"));
            List<Profile> friends = DBAccess.getFriends(profile);
            recyclerView.setAdapter(new FriendsListViewAdapter(friends));
            FloatingActionButton addFriendButton = (FloatingActionButton) view.findViewById(R.id.add_friend_fab);
            addFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create Object of Dialog class
                    final Dialog addFriend = new Dialog(getActivity());
                    // Set GUI of login screen
                    addFriend.setContentView(R.layout.fragment_dialog_add_friend);
                    addFriend.setTitle("Add a friend by email address");

                    // Init button of login GUI
                    Button btnLogin = (Button) addFriend.findViewById(R.id.btnLogin);
                    Button btnCancel = (Button) addFriend.findViewById(R.id.btnCancel);
                    final EditText friendEmailAddress = (EditText)addFriend.findViewById(R.id.friend_email_address);

                    // Attached listener for login GUI button
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String address = friendEmailAddress.getText().toString().trim();
                            boolean valid = address.matches(".*@.*\\.com");
                            if(address.length() > 0 && valid)
                            {
                                if(profile != null) {
                                    profile.addFriend(address);
                                    //update friends view
                                }
                                // Validate Your login credential here than display message
                                Toast.makeText(getActivity(),
                                        "Friend added", Toast.LENGTH_LONG).show();

                                // Redirect to dashboard / home screen.
                                addFriend.dismiss();
                            }
                            else if(address.length() == 0)
                            {
                                Toast.makeText(getActivity(),
                                        "Please enter friends email", Toast.LENGTH_LONG).show();

                            }
                            else if(!valid)
                            {
                                Toast.makeText(getActivity(),
                                        "Please enter a VALID email address", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addFriend.dismiss();
                        }
                    });

                    // Make dialog box visible.
                    addFriend.show();
                }
            });
            if(getActivity().getPreferences(Activity.MODE_PRIVATE).getBoolean("showcase",true)) {
                SharedPreferences.Editor editor = getActivity().getPreferences(Activity.MODE_PRIVATE).edit();
                editor.putBoolean("showcase", false);
                editor.commit();
                ShowcaseView showcase = new ShowcaseView.Builder(getActivity())
                        .withMaterialShowcase()
                        .setStyle(R.style.CustomShowcaseTheme2)
                        .setTarget(new RecyclerViewTarget(recyclerView, R.id.friend_image_circle))
                        .setContentTitle("Profile")
                        .setContentText("Click here to view your friends profile.")
                        .hideOnTouchOutside()
                        .setShowcaseEventListener(new OnShowcaseEventListener() {
                            @Override
                            public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                ShowcaseView view = new ShowcaseView.Builder(getActivity())
                                        .setShowcaseDrawer(new FriendCardViewDrawer(recyclerView, getResources()))
                                        .setStyle(R.style.CustomShowcaseTheme3)
                                        .setTarget(new RecyclerViewTarget(recyclerView, R.id.friend_card))
                                        .setContentTitle("Events")
                                        .setContentText("Click here to view your friend's events")
                                        .hideOnTouchOutside()
                                        .build();
                                view.hideButton();
                                view.show();
                            }

                            @Override
                            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                            }

                            @Override
                            public void onShowcaseViewShow(ShowcaseView showcaseView) {

                            }

                            @Override
                            public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                            }
                        }).build();
                showcase.hideButton();
                showcase.show();
            }
        }

    }

    @Override
    public void update(Observable observable, Object data) {
        if(recyclerView.getAdapter() != null)
        {
            ((FriendsListViewAdapter)recyclerView.getAdapter()).updateEventsShown(DBAccess.getFriends(profile));
            recyclerView.invalidate();
        }
    }

    class FriendsListViewAdapter extends RecyclerView.Adapter<FriendsListViewAdapter.FriendViewHolder> {
        List<Profile> shownProfiles;

        public FriendsListViewAdapter(List<Profile> profiles) {
            shownProfiles = profiles;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.template_friend, parent, false);
                ((View)parent.getParent()).findViewById(R.id.no_friends_message).setVisibility(View.INVISIBLE);
            return new FriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FriendViewHolder holder, int position) {
            holder.currentItem = position;
            holder.eventOwnerId = String.valueOf(shownProfiles.get(position).getId());
            holder.friendName.setText(shownProfiles.get(position).getName()+"'s Events");
            holder.friendImage.setImageBitmap(BitmapUtils.decodeStringToBitmap(shownProfiles.get(position).getEncodedProfileImage()));
        }

        @Override
        public int getItemCount() {
            return shownProfiles.size();
        }

        public void updateEventsShown(ArrayList<Profile> friends) {
            this.shownProfiles.clear();
            this.shownProfiles.addAll(friends);
            notifyDataSetChanged();
        }

        public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            public final View mView;
            public final TextView friendName;
            public CircularImageView friendImage;
            public String eventOwnerId;
            public int currentItem;

            public FriendViewHolder(View view) {
                super(view);
                mView = view;
                friendName = (TextView) view.findViewById(R.id.friend_name);
                friendImage = (CircularImageView) view.findViewById(R.id.friend_image_circle);
                friendImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), FragmentHolderActivity.class);
                        intent.putExtra("eventOwnerId", String.valueOf(eventOwnerId));
                        intent.putExtra("clientProfileId", getArguments().getString("clientProfileId"));
                        intent.putExtra("class","profile");
                        getActivity().startActivity(intent);
                    }
                });

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        itemPosition = currentItem;
                        return false;
                    }
                });
                itemView.setOnCreateContextMenuListener(FriendViewHolder.this);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FragmentHolderActivity.class);
                        intent.putExtra("eventOwnerId", String.valueOf(eventOwnerId));
                        intent.putExtra("clientProfileId", getArguments().getString("clientProfileId"));
                        intent.putExtra("class","events");
                        getActivity().startActivity(intent);
                    }
                });
            }
            @Override
             public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle(friendName.getText().toString());

                menu.add(0, v.getId(), 0, "Delete Friend");//groupId, itemId, order, title
            }
        }
    }
}
class FriendCardViewDrawer implements ShowcaseDrawer
{
    private int xoffset;
    private float height;
    private float width;
    private final float radius;
    private final Paint basicPaint;
    private final Paint eraserPaint;
    private int backgroundColor;

    public FriendCardViewDrawer(Resources resources) {
        this.radius = resources.getDimension(com.github.amlcurran.showcaseview.R.dimen.showcase_radius_material);
        this.eraserPaint = new Paint();
        this.eraserPaint.setColor(0xFFFFFF);
        this.eraserPaint.setAlpha(0);
        this.eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        this.eraserPaint.setAntiAlias(true);
        this.basicPaint = new Paint();
    }

    public FriendCardViewDrawer(RecyclerView view, Resources resources)
    {
        this(resources);
        CardView card = (CardView)view.getChildAt(0).findViewById(R.id.friend_card);
        CircularImageView imageView = (CircularImageView)view.getChildAt(0).findViewById(R.id.friend_image_circle);
        this.height = card.getHeight();
        this.width = card.getWidth()-imageView.getWidth();
        this.xoffset = imageView.getWidth()/2;
    }

    @Override
    public void setShowcaseColour(int color) {
        // no-op
    }

    @Override
    public void drawShowcase(Bitmap buffer, float x, float y, float scaleMultiplier) {
        Canvas bufferCanvas = new Canvas(buffer);
        //bufferCanvas.drawCircle(x, y, radius, eraserPaint);
        bufferCanvas.drawRect(x-width/2+xoffset,y-height/2,x+width/2+xoffset,y+height/2,eraserPaint);
    }

    @Override
    public int getShowcaseWidth() {
        //return (int) (radius * 2);
        return (int)width;
    }

    @Override
    public int getShowcaseHeight() {
        //return (int) (radius * 2);
        return (int)height;
    }

    @Override
    public float getBlockedRadius() {
        return radius;
    }

    @Override
    public void setBackgroundColour(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void erase(Bitmap bitmapBuffer) {
        bitmapBuffer.eraseColor(backgroundColor);
    }

    @Override
    public void drawToCanvas(Canvas canvas, Bitmap bitmapBuffer) {
        canvas.drawBitmap(bitmapBuffer, 0, 0, basicPaint);
    }

}


