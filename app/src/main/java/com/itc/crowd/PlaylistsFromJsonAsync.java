package com.itc.crowd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.StrictMode;

import com.google.gson.Gson;
import com.itc.crowd.model.Organizer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class PlaylistsFromJsonAsync extends AsyncTask<Void, Integer, HashMap<Integer, String>> {

    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private HashMap<Integer, String> playlists = new LinkedHashMap<Integer, String>();
    private Activity activity;
    private String userMail;

    public PlaylistsFromJsonAsync(Activity activity, String userMail){
        this.activity=activity;
        this.userMail=userMail;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.setProgressBarIndeterminateVisibility(true);
        nameValuePairs.add(new BasicNameValuePair("email", "user mail"));
        try {
            nameValuePairs.add(new BasicNameValuePair("usermail", URLEncoder.encode(userMail, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            nameValuePairs.add(new BasicNameValuePair("usermail", userMail));
        }

    }

    @Override
    protected void onProgressUpdate(Integer... values){
        super.onProgressUpdate(values);
        //Toast.makeText(ApplicationContextProvider.getContext(), "traitement asynchrone", Toast.LENGTH_LONG).show();

    }

    @Override
    protected HashMap<Integer, String> doInBackground(Void... arg0) {

        HttpRequest request = new HttpRequest(nameValuePairs);
        String json = request.getJsonFromUrl(StaticHelpers.API_URL + StaticHelpers.PLAYLISTS_ENDPOINT);
        Gson gson = new Gson();

        if(json == null){
            playlists = null;
        }
        else{
            Document doc = null;
            try{
                doc = request.getDomElement(json); // getting DOM element
            }
            catch(DOMException e){

            }

            if(doc == null){
                playlists = null;
            }
            else{
                try {
                    playlists = Organizer.listPlaylists(new JSONObject(json));
                    playlists.entrySet();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return playlists;
    }

    @SuppressLint("NewApi")
    protected void onPostExecute(HashMap<String, String>  result) {

        if(result == null){



        }
        else{

            activity.setProgressBarIndeterminateVisibility(false);
        }
    }
}