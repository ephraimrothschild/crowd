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
    private ArrayList<HashMap<String, String>> mapErrCode = new ArrayList<HashMap<String, String>>();
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
            mapErrCode = null;
        }
        else{
            if(xml == "timeout"){

                mapErrCode.put(MainActivity.KEY_TIMEOUT, "1");
            }

            else{

                Document doc = null;
                try{
                    doc = parser.getDomElement(xml); // getting DOM element
                }
                catch(DOMException e){

                }

                if(doc == null){
                    mapErrCode = null;
                }
                else{
                    NodeList nl_xml = doc.getElementsByTagName(MainActivity.KEY_XML);
                    // looping through all xml nodes <KEY_USER>
                    for (int i = 0; i < nl_xml.getLength(); i++) {
                        // creating new HashMap

                        Element e = (Element) nl_xml.item(i);
                        // adding each child node to HashMap key => value
                        mapErrCode.put(MainActivity.KEY_ERRCODE, parser.getValue(e, MainActivity.KEY_ERRCODE));
                    }
                }
            }
        }

        return mapErrCode;
    }

    @SuppressLint("NewApi")
    protected void onPostExecute(HashMap<String, String>  result) {

        if(result == null || result.containsKey(MainActivity.KEY_TIMEOUT)){

            MainActivity.displayToast(R.string.httpTimeOut);
            activity.setProgressBarIndeterminateVisibility(false);

        }
        else{
            if(result.get(MainActivity.KEY_ERRCODE).equals("000")){
                MainActivity.fragment= new MyProfileFragment();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_righttoleft, R.anim.slide_out_toptobottom,0,0).replace(R.id.frame_container, MainActivity.fragment).commit();

            }
            else{
                MainActivity.displayToast(result.get(MainActivity.KEY_ERRCODE));
            }
            activity.setProgressBarIndeterminateVisibility(false);
        }
    }
}