package com.itc.crowd.model;

import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

/**
 * Created by Ephraim on 12/31/2014.
 */
public class Organizer {

    public static void addToList(LinkedHashMap<String, Integer> oldList, JSONArray newList) {
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
        LinkedHashMap<String,Integer> sortedSongs = sortHashMapByValuesD(songs);
        List<Entry<String,Integer>> newentrySet = new ArrayList<>(sortedSongs.entrySet());
        songs.clear();
        for (int i = 0; i < sortedSongs.size(); i++) {
            Entry<String,Integer> currentSet = newentrySet.get(i);
            songs.put(currentSet.getKey(),currentSet.getValue());
        }
    }

    private static LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
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
    public static HashMap<Integer, String> listPlaylists(JSONObject obj) {
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

    public static HashMap<Integer, LinkedHashMap<String, Integer>> createPlaylists(JSONObject obj) {
        HashMap<Integer, LinkedHashMap<String, Integer>> playlists = new HashMap<Integer, LinkedHashMap<String, Integer>>();
        try {
            JSONArray playlistsArray = obj.getJSONArray("Playlists");
            for (int i = 0; i < playlistsArray.length(); i++) {
                JSONObject currentPlayList = playlistsArray.getJSONObject(i);
                LinkedHashMap<String, Integer> playlist = new LinkedHashMap<String, Integer>();
                addToList(playlist, currentPlayList.getJSONArray("songs"));
                playlists.put(currentPlayList.getInt("id"),playlist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playlists;
    }




}
