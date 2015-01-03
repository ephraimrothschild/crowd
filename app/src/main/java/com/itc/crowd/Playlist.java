package com.itc.crowd;

import com.itc.crowd.model.Organizer;
import com.itc.crowd.model.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ephraim on 1/3/2015.
 */
public class Playlist {
    private String name;
    private String user;
    private final int ID;
    private LinkedHashMap<String, Song> songs;
    public Playlist(int id, String name, String user) {
        this(id, name,user,new LinkedHashMap<String, Song>());
    }

    public Playlist(int id, String name, String user, LinkedHashMap<String,Song> songs) {
        this.ID = id;
        this.name = name;
        this.user = user;
        this.songs = songs;
    }

//    public Playlist(Playlist playlist) {
//        this.name = playlist.name;
//        this.user = playlist.user;
//        this.songs = playlist.songs;
//        this.ID = playlist.ID;
//    }

    public String getName() {
        return this.name;
    }

    public String getUser() {
        return this.user;
    }

    public Song getSong(String name) {
        return songs.get(name);
    }

    public ArrayList<Song> getListOfSongs() {
        return new ArrayList<Song>(songs.values());
    }

    public HashMap getHashMapOfSongs() {
        return this.songs;
    }

//    public void addSongs(HashMap<String, Song> newSongs) {
//        songs.putAll(newSongs);
//    }

    public void setSongs (LinkedHashMap<String, Song> newSongs) {
        this.songs = newSongs;
    }

    public void clearSongs() {
        songs = new LinkedHashMap<String,Song>();
    }

    public void addSongs(JSONArray newList) {
        for (int i = 0; i < newList.length(); i++) {
            try {
                JSONObject currentObj = newList.getJSONObject(i);
                String currSpotID = currentObj.getString("spotify_id");
                if (songs.containsKey(currSpotID)) {
                    songs.get(currSpotID).itterCount();
                } else {
                    songs.put(currSpotID,new Song(currSpotID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.songs = Organizer.sortSongsByHashMapValues(songs);
    }
}
