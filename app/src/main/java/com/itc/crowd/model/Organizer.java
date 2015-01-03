package com.itc.crowd.model;

import com.itc.crowd.Playlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Created by Ephraim on 12/31/2014.
 */
public class Organizer {

    /**
     * Adds songs to a LinkedHashMap of songs.
     * @param oldList - The LinkedHashMap<String, Integer> to add to.
     * @param newList - The JSONArray containing the data with the songs.
     *                Should be in the form of:
     * <code>[
     *  {
     *    "id": 52,
     *    "spotify_id": 21,
     *    "user_id": 3,
     *    "playlist_id": 23
     *  },
     *  {
     *    "id": 53,
     *    "spotify_id": 22,
     *    "user_id": 3,
     *    "playlist_id": 23
     *  }
     *]</code>
     */
    public static void addSongs(LinkedHashMap<String, Integer> oldList, JSONArray newList) {
        for (int i = 0; i < newList.length(); i++) {
            try {
                JSONObject currentObj = newList.getJSONObject(i);
                String currSpotID = currentObj.getString("spotify_id");
                if (oldList.containsKey(currSpotID)) {
                    int oldNum = oldList.get(currSpotID);
                    oldList.put(currSpotID,oldNum+1);
                } else {
                    oldList.put(currSpotID,1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sortSongs (HashMap<String, Integer> songs) {
        LinkedHashMap<String,Integer> sortedSongs = sortSongsByHashMapValues(songs);
        List<Entry<String,Integer>> newentrySet = new ArrayList<>(sortedSongs.entrySet());
        songs.clear();
        for (int i = 0; i < sortedSongs.size(); i++) {
            Entry<String,Integer> currentSet = newentrySet.get(i);
            songs.put(currentSet.getKey(),currentSet.getValue());
        }
    }

    /**
     * Sorts the hashmap based on the values instead of the keys.
     * @param passedMap - The HashMap<String, Integer> to be sorted
     * @return The new, sorted hashmap.
     */
    public static LinkedHashMap sortSongsByHashMapValues(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();
            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)){
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String)key, (Double)val);
                    break;
                }

            }

        }
        return sortedMap;
    }

    /**
     * Gets a list of playlists, in the form of a hashmap, containing the unique ID
     * as the key, and the name of the playlist as its value.
     * @param obj - The JSONObject to be parsed and turned into the hashmap of playlists
     * @return A list of playlists as a HashMap where the ID is the key, and the
     * name is the value.
     */
    public static HashMap<Integer, String> generateListOfPlaylists(JSONObject obj) {
        HashMap<Integer, String> playlists = new HashMap<Integer,String>();
        try {
            JSONArray playListsArray = obj.getJSONArray("Playlist");
           for (int i = 0; i < playListsArray.length(); i++) {
               JSONObject currentPlayList = playListsArray.getJSONObject(i);
               playlists.put(currentPlayList.getInt("id"),currentPlayList.getString("name"));
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playlists;
    }

    /**
     * Creates a HashMap containing an the playlist ID as the key, and a LinkedHashMap
     * as the value. The inner LinkedHashmap represents the songs in the playlist. The
     * key is the spotify ID for the song, and the value is the number of people who
     * requested it.
     * @param obj - The JSONObject to be parsed
     * @return The full representation of all the playlists.
     */
    public static HashMap<Integer, LinkedHashMap<String, Integer>> generateFullPlaylists(JSONObject obj) {
        HashMap<Integer, LinkedHashMap<String, Integer>> playlists = new HashMap<Integer, LinkedHashMap<String, Integer>>();
        try {
            JSONArray playlistsArray = obj.getJSONArray("Playlists");
            for (int i = 0; i < playlistsArray.length(); i++) {
                JSONObject currentPlayList = playlistsArray.getJSONObject(i);
                LinkedHashMap<String, Integer> playlist = new LinkedHashMap<String, Integer>();
                addSongs(playlist, currentPlayList.getJSONArray("songs"));
                playlists.put(currentPlayList.getInt("id"),playlist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playlists;
    }


    public static HashMap<Integer, Playlist> generateFullPlaylistObjects(JSONObject json) {
        HashMap<Integer, Playlist> playlists = new HashMap<Integer,Playlist>();
        try {
            JSONArray playlistsArray = json.getJSONArray("Playlists");
            for (int i = 0; i < playlistsArray.length(); i++) {
                JSONObject currentPlayList = playlistsArray.getJSONObject(i);
                Playlist list = new Playlist(currentPlayList.getInt("id"), currentPlayList.getString("name"), currentPlayList.getString("user"));
                list.addSongs(currentPlayList.getJSONArray("songs"));
                playlists.put(currentPlayList.getInt("id"),list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playlists;
    }




}
