package com.itc.crowd.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itc.crowd.R;
import com.itc.crowd.StoryCard;
import com.itc.crowd.model.Song;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class StoryCardAdapter extends ArrayAdapter<StoryCard> {
    public StoryCardAdapter(Activity activity, List<StoryCard> cards) {
        super(activity, 0, cards);
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
        StoryCard card = getItem(position);
        TextView titleView = (TextView) view.findViewById(R.id.story_title);
        TextView textView = (TextView) view.findViewById(R.id.story_text);
        ImageView imageView = (ImageView) view.findViewById(R.id.story_image);
        titleView.setText(card.getTitle());
        textView.setText(card.getText());
        ImageLoader im = ImageLoader.getInstance();
        if (card.getImageURL() != null) {
            imageView.setImageBitmap(im.loadImageSync(card.getImageURL()));
        } else {
            imageView.setImageResource(R.drawable.ic_launcher);
        }
        return view;

    }
}