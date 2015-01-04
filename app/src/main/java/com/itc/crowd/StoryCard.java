package com.itc.crowd;

/**
 * Created by Ephraim on 1/3/2015.
 */
public class StoryCard {
    private String title;
    private String text;
    private String imageURL;
    public StoryCard(String title, String text, String imageURL) {
        this(title,text);
        this.imageURL = imageURL;
    }

    public StoryCard(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {return this.title;}
    public String getText() {return this.text;}
    public String getImageURL() {return this.imageURL;}
}
