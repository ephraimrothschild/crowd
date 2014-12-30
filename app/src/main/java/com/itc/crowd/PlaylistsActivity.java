package com.itc.crowd;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.DialogFragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.Config;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;

public class PlaylistsActivity extends ActionBarActivity  implements PlayerNotificationCallback, ConnectionStateCallback, CreatePlaylistDialogFragment.NoticeDialogListener, View.OnClickListener {
    private static final String CLIENT_ID = "4dd6a9c11dbf412d944b981abb0f55ab";
    private Player mPlayer;
    private Button btnQrScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);
        btnQrScan = (Button)findViewById(R.id.btnQrScan);
        btnQrScan.setOnClickListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playlists, menu);
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

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("test", "started intent");
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
            Spotify spotify = new Spotify();
            Log.d("test", "made spotify object");
            mPlayer = spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                @Override
                public void onInitialized() {
                    Log.d("test","begin initialized spotify player");
                    mPlayer.addConnectionStateCallback(PlaylistsActivity.this);
                    mPlayer.addPlayerNotificationCallback(PlaylistsActivity.this);
                    mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    Log.d("test","end initialized spotify player");
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                }
            });
        }
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

    public void onClick(View v){
        if(v.getId()==R.id.btnQrScan){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            if(IntentIntegrator.QR_CODE_TYPES.contains(scanFormat))
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Scan content received: " + scanContent, Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Incorrect scan format received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
