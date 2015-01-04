package com.itc.crowd;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;
import com.spotify.sdk.android.playback.PlayerStateCallback;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlayerHelper extends Activity implements PlayerNotificationCallback, ConnectionStateCallback {

    public interface PlayerHelperListener {
        public void onTrackArtists(String artists);
        public void onTrackName(String trackName);
        public void onTrackAlbumImages(List<String> imageUrls);
    }

    private Spotify _spotify;
    private Player _spotifyPlayer;
    private SpotifyWebApiHelper _spotifyHelper;
    PlayerHelperListener _listener;
    private boolean _isCleaned;

    private SpotifyWebApiHelper getSpotifyHelper()
    {
        if(_spotifyHelper == null)
        {
            _spotifyHelper = new SpotifyWebApiHelper();
        }
        return _spotifyHelper;
    }

    public PlayerHelper()
    {
        _spotify = new Spotify();
        _spotifyPlayer = _spotify.getPlayer(GlobalConfig.getPlayerConfig(), this, new Player.InitializationObserver() {
            @Override
            public void onInitialized() {
                _spotifyPlayer.addConnectionStateCallback(PlayerHelper.this);
                _spotifyPlayer.addPlayerNotificationCallback(PlayerHelper.this);
                _spotifyPlayer.setShuffle(false);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("PlayerHelper", "Could not initialize player: " + throwable.getMessage());
            }
        });
        _isCleaned = false;
    }

    public void CleanUp()
    {
        if(_isCleaned)
            return;
        Spotify.destroyPlayer(this);
        _isCleaned = true;
    }

    @Override
    public void finalize()
    {
        if(!_isCleaned)
            CleanUp();
    }

    public void AttachListener(PlayerHelperListener listener)
    {
        _listener = listener;
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("PlayerHelper", "Playback event received: " + eventType.name());
        switch (eventType)
        {
            case TRACK_START:
                if(_listener != null)
                {
                    processPlayerState(playerState);
                }
                break;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {
        Log.d("PlayerHelper", "Playback error received: " + errorType.name());
    }

    @Override
    public void onLoggedIn() {
        Log.d("PlayerHelper", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("PlayerHelper", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable throwable) {
        Log.d("PlayerHelper", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("PlayerHelper", "Temporary error occurred");
    }

    @Override
    public void onNewCredentials(String s) {
        Log.d("PlayerHelper", "User credentials blob received");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("PlayerHelper", "Received connection message: " + message);
    }

    public void PlayTrack(String trackId)
    {
        _spotifyPlayer.play("spotify:track:" + trackId);
    }

    public void QueueTrack(String trackId)
    {
        _spotifyPlayer.queue("spotify:track:" + trackId);
    }

    public void PreviousTrack()
    {
        _spotifyPlayer.skipToPrevious();
    }

    public void PausePlayer()
    {
        _spotifyPlayer.pause();
    }

    public void ResumePlayer()
    {
        _spotifyPlayer.resume();
    }

    public void SeekToPosition(int positionInMs)
    {
        _spotifyPlayer.seekToPosition(positionInMs);
    }

    private PlayerState _playerState;
    private PlayerState getPlayerState()
    {
        while(_playerState == null)
        {
            // Do nothing
        }
        return _playerState;
        //_spotifyPlayer.getPlayerState(new PlayerStateCallback() {
        //    @Override
        //    public void onPlayerState(PlayerState playerState) {
        //        _playerState = playerState;
        //    }
        //});
        //return _playerState;
    }

    public boolean getIsPlaying()
    {
        PlayerState state = getPlayerState();
        return state.playing;
    }

    public int getCurrentTrackDuration()
    {
        PlayerState state = getPlayerState();
        return state.durationInMs;
    }

    public int getCurrentTrackPosition()
    {
        PlayerState state = getPlayerState();
        return state.positionInMs;
    }

    private String getCurrentTrackId(PlayerState playerState)
    {
        if(!playerState.trackUri.startsWith("spotify:track:"))
            return null;

        return playerState.trackUri.substring(playerState.trackUri.lastIndexOf(":")+1);
    }

    private void processPlayerState(PlayerState playerState)
    {
        if(_listener == null)
            return;

        String trackId = getCurrentTrackId(playerState);
        getSpotifyHelper().getTrack(trackId, new Callback<Track>() {
            @Override
            public void success(Track track, Response response) {
                Log.d("Track success", track.name);
                String artists = "";
                for (ArtistSimple currentArtist : track.artists) {
                    artists += (currentArtist.name+", ");
                }
                artists = artists.substring(0, artists.length()-3);
                _listener.onTrackArtists(artists);

                _listener.onTrackName(track.name);

                List<String> imageUrls = new ArrayList<String>();
                for (Image currentImage : track.album.images) {
                    imageUrls.add(currentImage.url);
                }
                _listener.onTrackAlbumImages(imageUrls);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Track failure", error.toString());
            }
        });
    }
}
