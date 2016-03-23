package edu.byu.stringcheese.presenttime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.HashMap;
import java.util.Map;

import edu.byu.stringcheese.presenttime.database.Event;
import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Item;
import edu.byu.stringcheese.presenttime.database.Profile;
import edu.byu.stringcheese.presenttime.database.Utils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    LoginButton loginButton;
    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    public static Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        //Firebase setup
        Firebase.setAndroidContext(this);
        initializeFirebase();
        //FirebaseDatabase.getInstance().fakeData();
        //facebook[start]
        callbackManager = CallbackManager.Factory.create();


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "Login attempt failed.");
            }

        });
        //facebook[end]
        //google[start]
        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                        //.requestServerAuthCode(getString(R.string.server_client_id))
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        // [END customize_button]
        //Google[end]
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //facebook[start]
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //facebook[end]
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true, acct);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false, null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false, null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false, null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn, GoogleSignInAccount acct) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("email",acct.getEmail());
            intent.putExtra("name", acct.getDisplayName());
            startActivity(intent);
            //finish();
        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //signIn();
    }

    private void initializeFirebase() {

        ref = new Firebase("https://crackling-fire-2441.firebaseio.com/present-time");
        // Attach an listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Database db = postSnapshot.getValue(Database.class);
                    //System.out.println(post.getAuthor() + " - " + post.getTitle());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        //example for ordering https://www.firebase.com/docs/android/guide/retrieving-data.html#section-ordered-data
        ref.getParent().addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                if (dataSnapshot.getKey().equals("present-time")) {
                    GenericTypeIndicator<FirebaseDatabase> t = new GenericTypeIndicator<FirebaseDatabase>() {
                    };
                    FirebaseDatabase dbTest = dataSnapshot.getValue(t);
                    FirebaseDatabase.setInstance(dbTest);

                }
                /*try {
                    if (dataSnapshot.getKey().equals("present-time")) {
                        GenericTypeIndicator<FirebaseDatabase> t = new GenericTypeIndicator<FirebaseDatabase>() {
                        };
                        FirebaseDatabase dbTest = dataSnapshot.getValue(t);
                        //FirebaseDatabase db = (FirebaseDatabase) dataSnapshot.getValue();
                        FirebaseDatabase.setInstance(dbTest);
                    } else if (dataSnapshot.getKey().equals("events")) {
                        GenericTypeIndicator<Map<String, Event>> t = new GenericTypeIndicator<Map<String, Event>>() {
                        };
                        Map<String, Event> events = dataSnapshot.getValue(t);
                    } else if (dataSnapshot.getKey().equals("items")) {
                        GenericTypeIndicator<Map<String, Item>> t = new GenericTypeIndicator<Map<String, Item>>() {
                        };
                        Map<String, Item> items = dataSnapshot.getValue(t);

                    } else if (dataSnapshot.getKey().equals("profiles")) {
                        GenericTypeIndicator<Map<String, Profile>> t = new GenericTypeIndicator<Map<String, Profile>>() {
                        };
                        Map<String, Profile> profiles = dataSnapshot.getValue(t);

                    }
                }
                catch(Exception e)
                {
                    Log.e("LoginActivity","error loading: "+dataSnapshot.getKey(),e);
                }*/
                //.out.println("Author: " + newPost.getAuthor());
                //System.out.println("Title: " + newPost.getTitle());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //String title = (String) snapshot.child("title").getValue();
                //System.out.println("The updated post title is " + title);
                if (dataSnapshot.getKey().equals("present-time")) {
                    GenericTypeIndicator<FirebaseDatabase> t = new GenericTypeIndicator<FirebaseDatabase>() {
                    };
                    FirebaseDatabase dbTest = dataSnapshot.getValue(t);
                    //FirebaseDatabase db = (FirebaseDatabase) dataSnapshot.getValue();
                    FirebaseDatabase.setInstance(dbTest);
                } else if (dataSnapshot.getKey().equals("events")) {
                    GenericTypeIndicator<Map<String, Event>> t = new GenericTypeIndicator<Map<String, Event>>() {
                    };
                    Map<String, Event> events = dataSnapshot.getValue(t);
                } else if (dataSnapshot.getKey().equals("items")) {
                    GenericTypeIndicator<Map<String, Item>> t = new GenericTypeIndicator<Map<String, Item>>() {
                    };
                    Map<String, Item> items = dataSnapshot.getValue(t);

                } else if (dataSnapshot.getKey().equals("profiles")) {
                    GenericTypeIndicator<Map<String, Profile>> t = new GenericTypeIndicator<Map<String, Profile>>() {
                    };
                    Map<String, Profile> profiles = dataSnapshot.getValue(t);

                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //String title = (String) snapshot.child("title").getValue();
                //System.out.println("The blog post titled " + title + " has been deleted");

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
            //... ChildEventListener also defines onChildChanged, onChildRemoved,
            //    onChildMoved and onCanceled, covered in later sections.
        });
    }
}

