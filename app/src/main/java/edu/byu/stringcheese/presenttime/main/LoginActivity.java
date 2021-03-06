package edu.byu.stringcheese.presenttime.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.util.Observable;
import java.util.Observer;

import edu.byu.stringcheese.presenttime.BitmapUtils;
import edu.byu.stringcheese.presenttime.R;
import edu.byu.stringcheese.presenttime.database.DBAccess;
import edu.byu.stringcheese.presenttime.database.FirebaseDatabase;
import edu.byu.stringcheese.presenttime.database.Profile;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, Observer {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private LoginButton facebook_login_button;
    private SignInButton google_login_button;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private Button google_logout_button;
    private Button google_disconnect_button;
    private Button debug_login;
    private Button debug_amanda_login;
    private Button debug_sam_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        //Firebase setup
        FirebaseDatabase.addObserver(this);
        FirebaseDatabase.initializeFirebase(this, getResources());
        debug_login = (Button) findViewById(R.id.debug_login_button);
        debug_amanda_login = (Button) findViewById(R.id.debug_amanda_login_button);
        debug_sam_login = (Button) findViewById(R.id.debug_sam_login_button);
        facebook_login_button = (LoginButton) findViewById(R.id.facebook_login_button);
        google_login_button = (SignInButton) findViewById(R.id.google_login_button);
        google_logout_button = (Button) findViewById(R.id.sign_out_button);
        google_disconnect_button = (Button) findViewById(R.id.disconnect_button);


        mStatusTextView = (TextView) findViewById(R.id.status);

        facebook_login_button.setEnabled(false);
        google_login_button.setEnabled(false);
        debug_login.setEnabled(false);

        // Button listeners
        google_login_button.setOnClickListener(this);
        google_logout_button.setOnClickListener(this);
        google_disconnect_button.setOnClickListener(this);
        debug_login.setOnClickListener(this);
        debug_amanda_login.setOnClickListener(this);
        debug_sam_login.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        facebook_login_button.setReadPermissions("user_friends");
        facebook_login_button.registerCallback(callbackManager, new FacebookLoginCallback());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
        google_login_button = (SignInButton) findViewById(R.id.google_login_button);
        google_login_button.setSize(SignInButton.SIZE_STANDARD);
        google_login_button.setScopes(gso.getScopeArray());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void updateUI(boolean signedIn, final GoogleSignInAccount acct) {
        if (signedIn) {
            google_login_button.setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            Plus.PeopleApi.load(mGoogleApiClient, acct.getId()).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
                @Override
                public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {
                    PersonBuffer buffer = loadPeopleResult.getPersonBuffer();
                    final Person person = buffer.get(0);
                    buffer.release();

                    Profile myProfile = DBAccess.getProfileByEmail(acct.getEmail());
                    if (myProfile == null) {
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... params) {
                                return BitmapUtils.decodeImageFromWebToString(acct.getPhotoUrl(), 512, 512);
                            }

                            @Override
                            protected void onPostExecute(String encodedImage) {
                                DBAccess.addProfile(acct.getDisplayName(), acct.getEmail(), person.getId(), "", "", person.getBirthday(), "", "", "", encodedImage);
                                //Get Friends Info
                                Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
                                    @Override
                                    public void onResult(People.LoadPeopleResult loadPeopleResult) {
                                        if (loadPeopleResult.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
                                            PersonBuffer personBuffer = loadPeopleResult.getPersonBuffer();
                                            try {
                                                int count = personBuffer.getCount();
                                                for (int i = 0; i < count; i++) {
                                                    DBAccess.getProfileByEmail(acct.getEmail()).addFriendByGoogleId(personBuffer.get(i).getId());
                                            /*Log.i("person", "Person " + i + " name: " + personBuffer.get(i).getDisplayName() + " - id: " + personBuffer.get(i).getId());
                                            Log.i("birthday", personBuffer.get(i).getBirthday() + "" + "  hasBday" + personBuffer.get(i).hasBirthday());*/
                                                }
                                            } finally {
                                                personBuffer.release();
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.putExtra("clientProfileId", String.valueOf(DBAccess.getProfileByEmail(acct.getEmail()).getId()));
                                                startActivity(intent);
                                            }
                                        } else {
                                            Log.i("error", "Error");
                                        }
                                    }
                                });
                            }
                        }.execute();
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("clientProfileId", String.valueOf(DBAccess.getProfileByEmail(acct.getEmail()).getId()));
                        startActivity(intent);
                    }
                }
            });

            //finish();
        } else {
            mStatusTextView.setText(R.string.signed_out);

            google_login_button.setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_login_button:
                    signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
            case R.id.debug_amanda_login_button:
                if (FirebaseDatabase.hasInstance()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Profile profile = DBAccess.getProfileByEmail("amanda@cool.com");
                    if(profile != null) {
                        intent.putExtra("clientProfileId", String.valueOf(profile.getId()));
                        startActivity(intent);
                    }
                    else
                    {
                        //Snackbar.make(LoginActivit)
                        Log.e(TAG, "DEBUG DATA NOT ADDED");
                    }
                } else
                    Snackbar.make(v, "Database Not Yet Loaded", Snackbar.LENGTH_SHORT);
                break;
            case R.id.debug_sam_login_button:
                if (FirebaseDatabase.hasInstance()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Profile profile = DBAccess.getProfileByEmail("sam@cool.com");
                    if(profile != null) {
                        intent.putExtra("clientProfileId", String.valueOf(profile.getId()));
                        startActivity(intent);
                    }
                    else
                    {
                        //Snackbar.make(LoginActivit)
                        Log.e(TAG, "DEBUG DATA NOT ADDED");
                    }
                } else
                    Snackbar.make(v, "Database Not Yet Loaded", Snackbar.LENGTH_SHORT);
                break;
            case R.id.debug_login_button:
                if (FirebaseDatabase.hasInstance()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Profile profile = DBAccess.getProfileByEmail("justin@cool.com");
                    if(profile != null) {
                        intent.putExtra("clientProfileId", String.valueOf(profile.getId()));
                        startActivity(intent);
                    }
                    else
                    {
                        //Snackbar.make(LoginActivit)
                        Log.e(TAG, "DEBUG DATA NOT ADDED");
                    }
                } else
                    Snackbar.make(v, "Database Not Yet Loaded", Snackbar.LENGTH_SHORT);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //signIn();
    }

    @Override
    public void update(Observable observable, Object data) {
        facebook_login_button.setEnabled(true);
        google_login_button.setEnabled(true);
        debug_login.setEnabled(true);
        debug_sam_login.setEnabled(true);
        debug_amanda_login.setEnabled(true);
    }

    private class FacebookLoginCallback implements FacebookCallback<LoginResult> {
            @Override
            public void onSuccess(LoginResult loginResult) {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
    }
}

