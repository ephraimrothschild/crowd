package com.itc.crowd.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.itc.crowd.ApplicationContextProvider;
import com.itc.crowd.GlobalConfig;
import com.itc.crowd.R;
import com.itc.crowd.StaticHelpers;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.Config;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;


public class MainActivity extends Activity implements PlayerNotificationCallback, ConnectionStateCallback{




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = this.getSharedPreferences("CROWD_AUTH", Context.MODE_PRIVATE);
        if(sharedPref.getString("SPOTIFY_ACCESS_TOKEN", null) !=null) {
            Intent myIntent = new Intent(MainActivity.this, PlaylistsActivity.class);
            MainActivity.this.startActivity(myIntent);
            finish();
        }
        else {
            setContentView(R.layout.login);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("test", "started intent");
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Config playerConfig = new Config(this, response.getAccessToken(), getResources().getString(R.string.CLIENT_ID));
            GlobalConfig.setPlayerConfig(playerConfig);

            //myIntent.putExtra("key", value); //Optional parameters

            StaticHelpers.SPOTIFY_ACCESS_TOKEN = response.getAccessToken();
            SharedPreferences sharedPref = this.getSharedPreferences("CROWD_AUTH", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("SPOTIFY_ACCESS_TOKEN", StaticHelpers.SPOTIFY_ACCESS_TOKEN);
            editor.apply();


            Intent myIntent = new Intent(MainActivity.this, PlaylistsActivity.class);
            MainActivity.this.startActivity(myIntent);

            onDestroy();
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Throwable throwable) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onNewCredentials(String s) {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {

    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {

    }

    public void runSpotifyAuth(View view) {
        SpotifyAuthentication.openAuthWindow(getResources().getString(R.string.CLIENT_ID), "token", getResources().getString(R.string.REDIRECT_URI),
                new String[]{"user-read-private user-read-email", "streaming"}, null, this);


    }

}
