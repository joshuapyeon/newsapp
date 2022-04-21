package com.example.wntprototype.APIWrappers;

import java.util.List;

/**
 * A unit of Trending Content
 */
public class TrendingContent {

    /**
     * A list of associated articles
     */
    private List<NewsData> sources;

    /**
     * The trending phrase
     */
    private String phrase = "";

    /**
     * The url to the image of this trending content
     */
    private String urlToImage = "";

    /**
     * The quantity of times this word appeared in the data.
     * Note: This is mostly an arbitrary value to show relativity
     */
    private int value = 0;

    /**
     * gets the image url
     * @return the image url
     */
    public String getUrlToImage() {
        return urlToImage;
    }

    /**
     * sets the image url
     * @param urlToImage the image url
     */
    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    /**
     * Gives access to the article list
     * @return The articles
     */
    public List<NewsData> getSources() {
        return sources;
    }

    /**
     * sets the Articles
     * @param sources the articles related to the phrase
     */
    public void setSources(List<NewsData> sources) {
        this.sources = sources;
    }

    /**
     * Says if the trending content has Articles
     * @return if this has articles
     */
    public boolean hasArticles(){
        return sources != null && sources.size() > 0;
    }

    /**
     * Gives access to the trending phrase.
     * @return The trending phrase
     */
    public String getPhrase() {
        return phrase;
    }

    /**
     * Sets the phrase
     * @param phrase the trending phrase
     */
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    /**
     * Says if the trending content has a Phrase
     * @return if this has a phrase
     */
    public boolean hasPhrase(){
        return phrase != null && !phrase.equals("");
    }

    /**
     * Gives access to the word value
     * @return the word value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the phrases Value
     * @param value the phrases value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Says if the trending content has a value
     * @return if this has a value
     */
    public boolean hasValue(){
        return value > 0;
    }
}
