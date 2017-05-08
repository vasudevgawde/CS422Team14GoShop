package com.cs442.Team14;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    GoShopApplicationData applicationData;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applicationData = GoShopApplicationData.getInstance();
        preferences = getSharedPreferences(applicationData.GO_SHOP_SHARED_PREFERENCES, MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayUserDetails(navigationView.getHeaderView(0));
    }

    private void displayUserDetails(View view)
    {
        TextView name = (TextView) view.findViewById(R.id.menuActivityUserName);
        TextView email = (TextView) view.findViewById(R.id.menuActivityUserEmail);
        String signinType = preferences.getString(GoShopApplicationData.SIGN_IN_TYPE, "");

        if(signinType.equals(GoShopApplicationData.GOOGLE_SIGNIN_TYPE)){
            //display google account details
            name.setText(preferences.getString(GoShopApplicationData.GOOGLE_USER_NAME, ""));
            email.setText(preferences.getString(GoShopApplicationData.GOOGLE_USER_EMAIL, ""));
            // TODO: 14.03.2016 Display pic

        }
        else {
            //display user's registration details
            name.setText(preferences.getString(GoShopApplicationData.USER_NAME,""));
            email.setText(preferences.getString(GoShopApplicationData.USER_EMAIL,""));
            // TODO: 14.03.2016 Display pic
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences preferences = getSharedPreferences(GoShopApplicationData.GO_SHOP_SHARED_PREFERENCES, MODE_PRIVATE);
        if(preferences==null)
            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
        /*if(preferences.contains(GoShopApplicationData.USER_LOGGED_IN_STATUS))
            preferences.edit().putBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS,false).commit();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){

            case R.id.homePage:

                break;

            case R.id.userProfile:
                break;

            case R.id.cartPage:
                break;
            case R.id.favoritesPage:
                break;
            case R.id.orderHistory:
                break;
            case R.id.inviteFriends:
                break;
            case R.id.logout:
                preferences.edit().putBoolean(GoShopApplicationData.USER_LOGGED_IN_STATUS, false).commit();
                if(preferences.getString(GoShopApplicationData.SIGN_IN_TYPE,"").equals(GoShopApplicationData.GOOGLE_SIGNIN_TYPE)){

                    Auth.GoogleSignInApi.signOut(applicationData.getGoogleApiClient());
                }

                Toast.makeText(MenuActivity.this, "Logged out", Toast.LENGTH_SHORT).show();


                //Show login page
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

                break;
            case R.id.aboutGoShop:
                //Display dialog
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                                            .setIcon(R.drawable.ic_menu_about_goshop)
                                            .setTitle(R.string.menuActivityAboutGoShop)
                                            .setMessage(R.string.aboutGoShopText)
                                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            })
                                            .create();
                alertDialog.show();

                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
