package com.itc.crowd.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DialogFragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.itc.crowd.CreatePlaylistDialogFragment;
import com.itc.crowd.PlayerHelper;
import com.itc.crowd.PlaylistsFromJsonAsync;
import com.itc.crowd.R;
import com.itc.crowd.SpotifyWebApiHelper;
import com.itc.crowd.StoryCard;
import com.itc.crowd.adapters.StoryCardAdapter;
import com.itc.crowd.model.Song;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.Config;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class PlaylistsActivity extends Activity  implements PlayerNotificationCallback, ConnectionStateCallback, CreatePlaylistDialogFragment.NoticeDialogListener, PlayerHelper.PlayerHelperListener {
    private Player mPlayer;
    private ImageButton btnQrScan;
    private Button btnQrGenerate;
    private Button btnQrGenerate2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);
        TextView txt = (TextView) findViewById(R.id.playlistTitle);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Open_Sans/OpenSans-Light.ttf");
        txt.setTypeface(font);
        //btnQrGenerate = (Button)findViewById(R.id.btnQrGenerate);
        //btnQrGenerate.setOnClickListener(this);
        //btnQrGenerate2 = (Button)findViewById(R.id.btnQrGenerate2);
        //btnQrGenerate2.setOnClickListener(this);

        final ListView listView = (ListView) findViewById(R.id.playlistsScroll);

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "NEW YEARS PARTY",
                "Saturday",
                "24th Birthday",
                "Party 3",
                "Dutch Party",
                "Android List View",
                "List View Array Adapter",
                "Android Example List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };
        ArrayList<StoryCard> slist = new ArrayList<>();

        for (int i = 0; i < values.length; i++) {
            slist.add(new StoryCard(values[i], String.valueOf(i), "http://i.stack.imgur.com/kdrpp.png"));
        }

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                R.layout.story_card, R.id.story_title, values);
        StoryCardAdapter storyCardAdapter = new StoryCardAdapter(this, slist);
//        TextView storyTitle = (TextView) findViewById(R.id.story_title);
//        Typeface storyTitleFont = Typeface.createFromAsset(getAssets(), "fonts/Open_Sans/OpenSans-Semibold.ttf");
//        storyTitle.setTypeface(storyTitleFont);


        // Assign adapter to ListView
        listView.setAdapter(storyCardAdapter);
//        SpotifyWebApiHelper.getCurrentUserID();
//        new PlaylistsFromJsonAsync(this, SpotifyWebApiHelper.getCurrentUserID()).execute();


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = ((StoryCard) listView.getItemAtPosition(position)).getTitle();

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                logOut();
                Intent newInt = new Intent(PlaylistsActivity.this, MainActivity.class);
                PlaylistsActivity.this.startActivity(newInt);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("test", "started intent");
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Config playerConfig = new Config(this, response.getAccessToken(), getResources().getString(R.string.CLIENT_ID));
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

    public void onQRClick(View v){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
        //else if(v.getId() == R.id.btnQrGenerate)
        //{
        //    IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        //    scanIntegrator.shareText("New years party", "TEXT_TYPE");
        //}
        //else if(v.getId() == R.id.btnQrGenerate2)
        //{
        //    IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        //    scanIntegrator.shareText("24th Birthday", "TEXT_TYPE");
        //}
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

    private PlayerHelper _playerHelper;
    public void onButtonClick(View v)
    {
        _playerHelper = new PlayerHelper();
        _playerHelper.AttachListener(this);
        _playerHelper.PlayTrack("2TpxZ7JUBn3uw46aR7qd6V");
    }

    public void onTrackArtists(String artists)
    {
        Log.d("PlaylistsActivity", "Got artists event: " + artists);
    }

    public void onTrackName(String trackName)
    {
        Log.d("PlaylistsActivity", "Got track name event: " + trackName);
    }

    public void onTrackAlbumImages(List<String> imageUrls)
    {
        Log.d("PlaylistsActivity", "Got album images event.");
        _playerHelper.CleanUp();
    }

    public void logOut() {
        SharedPreferences sharedPref = this.getSharedPreferences("CROWD_AUTH", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
