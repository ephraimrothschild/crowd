package com.itc.crowd;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.app.ActivityManager;
import android.content.ComponentName;

import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;


public class MainActivity extends Activity implements PlayerNotificationCallback, ConnectionStateCallback, CreatePlaylistDialogFragment.NoticeDialogListener{

    private static final String CLIENT_ID = "4dd6a9c11dbf412d944b981abb0f55ab";
    private static final String REDIRECT_URI = "crowd://callback";
    private static MainActivity THISCLASS = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ImageButton button = (ImageButton) findViewById(R.id.spotifyConnectButton);
        this.THISCLASS = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpotifyAuthentication.openAuthWindow(CLIENT_ID, "token", REDIRECT_URI,
                        new String[]{"user-read-private", "streaming"}, null, THISCLASS);
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("test", "started intent");
        super.onNewIntent(intent);
        Uri uri = intent.getData();
            if (uri != null) {Intent myIntent = new Intent(MainActivity.this, PlaylistsActivity.class);
//                myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
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

    public void onOpenCreatePlaylist(View view)
    {
        DialogFragment newFragment = new CreatePlaylistDialogFragment();
        newFragment.show(getFragmentManager(), "CreatePlaylistDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

}
