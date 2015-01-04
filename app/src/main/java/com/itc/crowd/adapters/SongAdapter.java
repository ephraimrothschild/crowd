package com.itc.crowd.adapters;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itc.crowd.R;
import com.itc.crowd.model.Song;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class SongAdapter extends ArrayAdapter<Song> {
    public SongAdapter(Activity activity, List<Song> playlist) {
        super(activity, 0, playlist);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).build();
        ImageLoader.getInstance().init(config);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Activity activity = (Activity) getContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View view = convertView;
        // Inflate the views from XML
        if (view == null) {
            view = inflater.inflate(R.layout.story_card, null);
        }
        Song song = getItem(position);
        TextView titleView = (TextView) view.findViewById(R.id.story_title);
        TextView textView = (TextView) view.findViewById(R.id.story_text);
        ImageView imageView = (ImageView) view.findViewById(R.id.story_image);
        titleView.setText(song.getTitle());
        textView.setText(song.getArtists());
        ImageLoader im = ImageLoader.getInstance();
        imageView.setImageBitmap(im.loadImageSync(song.getImageURL()));
        return view;

    }
/*
        private class DownloadImageTask extends
                        AsyncTask<ImageDownloadingObject, Integer, Bitmap> {
                Bitmap img = null;
                ImageView imageView = null;
                
                // ImageDownloadingObject imgDownloadObject=null;;
                protected Bitmap doInBackground(ImageDownloadingObject... imgDataObj) {
                        
                        imageView = imgDataObj[0].getImaeContainer();
                        Log.i("DownloadImageTask",
                                        "doInBackground");
                        if (imageView.getDrawingCache() == null) {
                                // try loading from cach or download and save if not exist
                                MediaDownloader md = new MediaDownloader("KidsTv/Images");
                                img = md.getCachedImage(imgDataObj[0].getUrl());
                                //Log.d("Loading Image", "finished loading image position="  + imgDataObj[0].getPosition());
                                return img;
                        }
                        //Log.i("SITE IMAGE", "using imageView.getDrawingCache()");
                        Log.d("Loading Image", "finished loading image position="  + imgDataObj[0].getPosition());

                        return imageView.getDrawingCache();

                }

                // protected void onProgressUpdate(Integer... progress) {
                // setProgressPercent(progress[0]);
                // }

                protected void onPostExecute(Bitmap img) {
                        if (img == null)
                                imageView.setImageResource(R.drawable.rss);
                        else
                                imageView.setImageBitmap(img);
                }

        }*/

}