package com.itc.crowd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.StrictMode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PlaylistsFromJsonAsync extends AsyncTask<Void, Integer, ArrayList<HashMap<String, String>>> {

    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> playlists = new ArrayList<HashMap<String, String>>();
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
    protected ArrayList<HashMap<String, String>> doInBackground(Void... arg0) {

        HttpRequest request = new HttpRequest(nameValuePairs);
        String json = request.getJsonFromUrl(StaticHelpers.API_URL + StaticHelpers.PLAYLISTS_ENDPOINT);

        if(json == null){
            playlists = null;
        }
        else{
            if(json != "timeout"){
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
                    //PARSE JSON
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