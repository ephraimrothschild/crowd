package com.itc.crowd;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.User;

public final class PlaylistHelper {
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

    public Pager<Playlist> getPlaylists()
    {
        return getSpotifyApi().getService().getPlaylists(getUser().id);
    }

    public Pager<PlaylistTrack> getPlaylistTracks(Playlist playlist)
    {
        return getSpotifyApi().getService().getPlaylistTracks(getUser().id, playlist.id);
    }
}
