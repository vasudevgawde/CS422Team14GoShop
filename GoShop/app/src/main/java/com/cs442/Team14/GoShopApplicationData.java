package com.cs442.Team14;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Hrushi on 13.03.2016.
 */
public class GoShopApplicationData extends Application {

    //Name of the shared preference
    public static final String GO_SHOP_SHARED_PREFERENCES = "GoShop_Shared_Preferences";

    //Holds the user's logged-in status: true: User is logged-in, false: User is logged-out
    public static final String USER_LOGGED_IN_STATUS = "Logged_Status";

    //Holds the user's name
    public static final String USER_NAME = "User_Name";

    //Holds the user's password
    public static final String USER_PASSWORD = "User_Pwd";

    //Holds the user's password
    public static final String USER_EMAIL = "User_Email";

    //Holds the user's Phone number
    public static final String USER_PHONE = "User_Phone";

    //Holds the user's address
    public static final String USER_ADDRESS = "User_Address";

    //Holds whether a registered user already exits on this phone: true: user has already registered on this phone, false: new user, needs registration
    public static final String USER_CREDENTIAL_EXISTS = "User_Credentials_Status";

    //Sign-in type: GoogleSignIn, AppSignIn
    public static final String SIGN_IN_TYPE = "SignIn_Type";

    //Google sign-in details
    public static final String GOOGLE_USER_PHOTO_URL = "Google_Photo_Url";
    public static final String GOOGLE_USER_NAME = "Google_User_Name";
    public static final String GOOGLE_USER_EMAIL = "GmailId";

    public static final String GOOGLE_SIGNIN_TYPE = "GoogleSignIn";

    public static final String APP_SIGNIN_TYPE = "AppSignIn";


    private static GoShopApplicationData instance = null;

    private GoogleApiClient googleApiClient;

    private GoShopApplicationData(){

    }

    public static GoShopApplicationData getInstance() {
        if (null == instance){
            synchronized (GoShopApplicationData.class){
                instance = new GoShopApplicationData();
            }
        }
        return instance;
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }
}
