package com.itc.crowd;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.User;
import retrofit.Callback;

public final class SpotifyWebApiHelper {
    private SpotifyApi _spotifyApi;
    private User _user;

    private SpotifyApi getSpotifyApi()
    {
        if(_spotifyApi == null)
        {
            _spotifyApi = new SpotifyApi();
            _spotifyApi.setAccessToken(GlobalConfig.getInstance().getPlayerConfig().oauthToken);
        }
        return _spotifyApi;
    }

    private User getUser()
    {
        if(_user == null)
            _user = getSpotifyApi().getService().getMe();
        return _user;
    }

    public void getPlaylists(Callback<Pager<Playlist>> callback)
    {
        getSpotifyApi().getService().getPlaylists(getUser().id, callback);
    }

    public void getPlaylistTracks(Playlist playlist, Callback<Pager<PlaylistTrack>> callback)
    {
        getSpotifyApi().getService().getPlaylistTracks(getUser().id, playlist.id, callback);
    }

    public void getTrack(String trackId, Callback<Track> callback)
    {
        getSpotifyApi().getService().getTrack(trackId, callback);
    }
}
