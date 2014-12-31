package com.itc.crowd.model;

/**
 * Created by Ephraim on 12/31/2014.
 */
public class Song {
    private final String spotifyID;
    private int count;
    public Song(String spotifyId) {
        this.spotifyID = spotifyId;
    }

    private Song(String spotifyId, int count) {
        this.spotifyID = spotifyId;
        this.count = count;
    }

    public Song(Song song) {
        this.spotifyID = song.spotifyID;
        this.count = song.count;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Song)) {
            return false;
        }
        return ((Song) o).getSpotifyID().equals(getSpotifyID());
    }
}
