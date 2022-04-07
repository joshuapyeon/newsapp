package com.example.wntprototype.APIWrappers;

import java.util.List;

public class TrendingContent {

    /**
     * A list of associated articles
     */
    private List<NewsData> articles;

    /**
     * The trending phrase
     */
    private String phrase;

    /**
     * The quantity of times this word appeared in the data.
     * Note: This is mostly an arbitrary value to show relativity
     */
    private int value;

    /**
     * Gives access to the article list
     * @return The articles
     */
    public List<NewsData> getArticles() {
        return articles;
    }

    /**
     * sets the Articles
     * @param articles the articles related to the phrase
     */
    public void setArticles(List<NewsData> articles) {
        if(articles != null && articles.size() > 0) {
            this.articles = articles;
        }
    }

    /**
     * Says if the trending content has Articles
     * @return if this has articles
     */
    public boolean hasArticles(){
        return articles != null && articles.size() > 0;
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
        if(phrase != null && !phrase.equals("")) {
            this.phrase = phrase;
        }
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
        if(value > 0) {
            this.value = value;
        }
    }

    /**
     * Says if the trending content has a value
     * @return if this has a value
     */
    public boolean hasValue(){
        return value > 0;
    }
}
