package com.itc.crowd;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.User;
import retrofit.Callback;

public final class SpotifyWebApiHelper {
    private static SpotifyApi _spotifyApi;
    private static User _user;
    private static SpotifyService service;

    private static SpotifyApi getSpotifyApi()
    {
        if(_spotifyApi == null)
        {
            _spotifyApi = new SpotifyApi();
            _spotifyApi.setAccessToken(StaticHelpers.SPOTIFY_ACCESS_TOKEN);
        }
        return _spotifyApi;
    }

    public static SpotifyService getSpotifyService() {
        if (service == null) service = getSpotifyApi().getService();
        return service;
    }

    private static User getCurrentUser()
    {
        if(_user == null)
            _user = getSpotifyService().getMe();
        return _user;
    }

    public static String getCurrentUserID() {
        return getCurrentUser().id;
    }

    public static User getUser(String userEmail) {
        return getSpotifyService().getUser(userEmail);
    }

    public void getPlaylists(Callback<Pager<Playlist>> callback)
    {
        getSpotifyService().getPlaylists(getCurrentUser().id, callback);
    }

    public void getPlaylistTracks(Playlist playlist, Callback<Pager<PlaylistTrack>> callback)
    {
        getSpotifyService().getPlaylistTracks(getCurrentUser().id, playlist.id, callback);
    }

    public void getTrack(String trackId, Callback<Track> callback)
    {
        getSpotifyService().getTrack(trackId, callback);
    }

    public String getPlaylistTracks(Playlist playlist)
    {
        return getSpotifyService().getPlaylistTracks(getCurrentUser().id, playlist.id).toString();
    }

    public static Track getTrack(String trackId)
    {
        return getSpotifyService().getTrack(trackId);
    }


    public static String getUserDisplayName(String userID) {
        return getUser(userID).display_name;
    }

}
