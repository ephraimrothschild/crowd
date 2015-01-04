package com.itc.crowd.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itc.crowd.Playlist;
import com.itc.crowd.R;
import com.itc.crowd.SpotifyWebApiHelper;
import com.itc.crowd.model.Song;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class PlaylistsAdapter extends ArrayAdapter<Playlist> {
    public PlaylistsAdapter(Activity activity, List<Playlist> playlist) {
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
        Playlist playlist = getItem(position);
        TextView titleView = (TextView) view.findViewById(R.id.story_title);
        TextView textView = (TextView) view.findViewById(R.id.story_text);
//        ImageView imageView = (ImageView) view.findViewById(R.id.story_image);
        titleView.setText(playlist.getName());
        textView.setText(playlist.getListOfSongs().get(0).toString());
//        ImageLoader im = ImageLoader.getInstance();
//        imageView.setImageBitmap(im.loadImageSync(SpotifyWebApiHelper.getUser(playlist.getUser()).images.get(0).url));
        return view;

    }
}