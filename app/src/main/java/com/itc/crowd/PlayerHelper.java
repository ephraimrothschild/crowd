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

public class PlayerHelper extends Activity implements PlayerNotificationCallback, ConnectionStateCallback {
    private Spotify _spotify;
    private Player _spotifyPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _spotify = new Spotify();
        _spotifyPlayer = _spotify.getPlayer(GlobalConfig.getInstance().getPlayerConfig(), this, new Player.InitializationObserver() {
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
        final PlayerState result;
        _spotifyPlayer.getPlayerState(new PlayerStateCallback() {
            @Override
            public void onPlayerState(PlayerState playerState) {
                _playerState = playerState;
            }
        });
        return _playerState;
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

    public String getCurrentTrackId()
    {
        PlayerState state = getPlayerState();
        if(!state.trackUri.startsWith("spotify:track:"))
            return null;

        return state.trackUri.substring(state.trackUri.lastIndexOf(":")+1);
    }
}
