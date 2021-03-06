package com.example.wntprototype;

import com.example.wntprototype.APIWrappers.TrendingContent;

import java.util.List;

public class DataCache {

    /**
     * Singleton Instance that has a global DataCache.
     */
    private static final DataCache cache = new DataCache();

    /**
     * The data for all the classes to access
     */
    private List<TrendingContent> data = null;

    /**
     * Holds the last keyword that was searched
     */
    private String keywordSearched = "";

    public String getKeywordSearched() {
        return keywordSearched;
    }

    public void setKeywordSearched(String keywordSearched) {
        this.keywordSearched = keywordSearched;
    }

    public boolean hasKeyword(){
        return !keywordSearched.equals("");
    }

    /**
     * Private constructor
     */
    private DataCache() {
    }

    /**
     * Public accessor to the Singleton class
     *
     * @return The instance of DataCache
     */
    public static DataCache getCache() {
        return cache;
    }

    /**
     * This gives access to the variable saying if the cache has data.
     *
     * @return Says if there is data in the cache
     */
    public Boolean hasData() {
        return (data != null && data.size() > 0);
    }

    /**
     * Gives access to the Cached data
     *
     * @return The Data from the sources
     */
    public List<TrendingContent> getData() {
        return data;
    }

    /**
     * Sets the data for global use.
     *
     * @param data The data from the API data pull.
     */
    public void setData(List<TrendingContent> data) {
            this.data = data;
    }
}
