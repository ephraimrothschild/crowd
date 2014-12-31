package com.itc.crowd;


import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by david on 31/12/14.
 */
public class StaticHelpers {
    static public String AUTH_TOKEN;
    static public String SPOTIFY_ACCESS_TOKEN;

    static public String API_URL = "crowdj.herokuapp.com/";
    static public String USERS_ENDPOINT = "users";
    static public String PLAYLISTS_ENDPOINT = "playlists";
    static public String USER_PLAYLISTS_ENDPOINT = "user_playlists";
    static public String SONGS_ENDPOINT = "songs";


    static void displayToast(String str){


        Toast toast = Toast.makeText(ApplicationContextProvider.getContext(), str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
