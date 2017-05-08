package com.cs442.Team14;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class SplashScreenActivity extends AppCompatActivity {


    private static final int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(SplashScreenActivity.this, "", Toast.LENGTH_SHORT).show();
                NextActivityIntent();

            }
        }, SPLASH_TIMEOUT);


    }

    GoogleApiClient mGoogleApiClient;

    private void NextActivityIntent()
    {
        SharedPreferences preferences = getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES, MODE_PRIVATE);
        boolean isLogged = preferences.getBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS, false);


        if(isLogged)
        {
            if(preferences.getString(GoShopApplicationData.SIGN_IN_TYPE, "").equals(GoShopApplicationData.GOOGLE_SIGNIN_TYPE))
            {
                GoShopApplicationData applicationData;
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

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(applicationData.getGoogleApiClient());
                applicationData.getGoogleApiClient().connect();
                startActivityForResult(signInIntent, 100);

            }
            else
                startActivity(new Intent(SplashScreenActivity.this, MenuActivity.class));
        }
        else if(preferences.getBoolean(GoShopApplicationData.USER_CREDENTIAL_EXISTS, false))
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        else
            startActivity(new Intent(SplashScreenActivity.this, RegistrationActivity.class));
    }


    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        handleSignInResult(result);


    }



    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            SharedPreferences preferences;
            GoShopApplicationData applicationData;
            applicationData = GoShopApplicationData.getInstance();

            preferences = getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES, MODE_PRIVATE);

            String googleUserName = result.getSignInAccount().getDisplayName();
            String email = result.getSignInAccount().getEmail();
            //String photoUrl = result.getSignInAccount().getPhotoUrl().toString();

            preferences.edit()
                    .putString(GoShopApplicationData.SIGN_IN_TYPE, GoShopApplicationData.GOOGLE_SIGNIN_TYPE)
                    .putString(GoShopApplicationData.GOOGLE_USER_NAME, googleUserName)
                    .putString(GoShopApplicationData.GOOGLE_USER_EMAIL, email)
                    .putBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS,true)
                            //.putString(GoShopApplicationData.GOOGLE_USER_PHOTO_URL, photoUrl);
                    .commit();

            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);

            finish();

        } else {
            Toast.makeText(this, "Login failed! Please try again", Toast.LENGTH_SHORT).show();

        }
    }

    View mProgressView;

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView = findViewById(R.id.splashScreenLoginProgress);

            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            //mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    /*show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });*/

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
    }


}
