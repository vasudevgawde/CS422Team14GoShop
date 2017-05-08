package com.cs442.Team14;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{


    private SharedPreferences preferences;

    private static final int RC_SIGN_IN = 100;

    /**
     * Authentication store containing known user names and passwords.
     */
    private static final String[] LOGIN_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUserName;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private GoShopApplicationData applicationData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        applicationData = GoShopApplicationData.getInstance();
        preferences = getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES, MODE_PRIVATE);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();

        applicationData.setGoogleApiClient(new GoogleApiClient.Builder(this).
                addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build());


        findViewById(R.id.sign_in_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(applicationData.getGoogleApiClient());
                applicationData.getGoogleApiClient().connect();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        SharedPreferences preferences = getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES, MODE_PRIVATE);

        LOGIN_CREDENTIALS[0] = preferences.getString(GoShopApplicationData.USER_NAME, "");
        LOGIN_CREDENTIALS[1] = preferences.getString(GoShopApplicationData.USER_PASSWORD, "");


        // Set up the login form.
        mUserName = (EditText) findViewById(R.id.loginUserName);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLoginButton = (Button) findViewById(R.id.loginSignInButton);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);


        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            String googleUserName = result.getSignInAccount().getDisplayName();
            String email = result.getSignInAccount().getEmail();
            //String photoUrl = result.getSignInAccount().getPhotoUrl().toString();


            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
            preferences.edit()
                    .putString(GoShopApplicationData.SIGN_IN_TYPE, GoShopApplicationData.GOOGLE_SIGNIN_TYPE)
                    .putString(GoShopApplicationData.GOOGLE_USER_NAME, googleUserName)
                    .putString(GoShopApplicationData.GOOGLE_USER_EMAIL, email)
                    .putBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS,true)
                    //.putString(GoShopApplicationData.GOOGLE_USER_PHOTO_URL, photoUrl);
                    .commit();

            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);

            finish();

        } else {
            Toast.makeText(LoginActivity.this, "Login failed! Please try again", Toast.LENGTH_SHORT).show();

        }
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserName.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String name = mUserName.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (password.equals("")) {
            //mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.setError("Enter password");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (name.equals("")) {
            //mUserName.setError(getString(R.string.error_field_required));
            mUserName.setError("Enter user name");
            focusView = mUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(name, password);
            mAuthTask.execute((Void) null);
        }
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(LoginActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mUsername = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            if(mUsername.equals((LOGIN_CREDENTIALS[0]))&&
               mPassword.equals(LOGIN_CREDENTIALS[1])){

                return true;
            }

            //return false by default
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                preferences.edit()
                        .putString(GoShopApplicationData.SIGN_IN_TYPE, GoShopApplicationData.APP_SIGNIN_TYPE)
                        .putBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS, true)
                        .commit();

                startActivity(intent);
                finish();

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

