package com.itc.crowd.model;

import com.itc.crowd.SpotifyWebApiHelper;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Ephraim on 12/31/2014.
 */
public class Song implements Comparable<Song> {
    private final String spotifyID;
    private final String title;
    private final Track track;
    private final String artists;
    private String imageURL;
    private int count;

    public Song(String spotifyId) {
        this(spotifyId, 1);
    }

    private Song(String spotifyId, int count) {
        this.spotifyID = spotifyId;
        this.count = count;
        this.track = SpotifyWebApiHelper.getTrack(spotifyID);
        this.title = track.name;
        String myArtists = "";
        for (ArtistSimple artist : track.artists) {
            myArtists += (artist.name + ", ");
        }
        this.artists = myArtists.substring(0, myArtists.length() - 3);
            this.imageURL = track.album.images.get(0).url;
    }

    public Song(Song song) {
        this.spotifyID = song.spotifyID;
        this.count = song.count;
        this.title = song.title;
        this.artists = song.artists;
        this.track = song.track;
    }

    public void itterCount() {
        count++;
    }

    public void subtractCount() {
        count--;
    }

    public int getCount() {
        return count;
    }
    public String getSpotifyID() {
        return spotifyID;
    }
    public String getArtists() {
        return artists;
    }
    public Track getTrack() {
        return this.track;
    }
    public String getTitle() {
        return this.title;
    }
    public String getImageURL() {
        return this.imageURL;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Song)) {
            return false;
        }
        return ((Song) o).getSpotifyID().equals(getSpotifyID());
    }

    @Override
    public int compareTo(Song another) {
        if (getCount() > another.getCount()) return 1;
        else if (getCount() < another.getCount()) return -1;
        else return 0;
    }

    @Override
    public String toString() {
        return title;
    }
}
