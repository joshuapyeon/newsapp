package com.example.wntprototype.APIWrappers;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.GoogleAPIs.GTSWrapper;
import com.example.wntprototype.APIWrappers.GoogleAPIs.TrendingWrapper;
import com.example.wntprototype.APIWrappers.GoogleAPIs.YoutubeWrapper;
import com.example.wntprototype.APIWrappers.WebSearchAPI.WebSearchWrapper;

import java.util.List;

public enum DataSource {
    WebSearch,
    GoogleTrendingSearches,
    GoogleTrends,
    YoutubeTrends;

    @Override
    public String toString() {
        switch (this) {
            case WebSearch:
                return "News";
            case GoogleTrendingSearches:
                return "Trending Searches";
            case GoogleTrends:
                return "Google Trends";
            case YoutubeTrends:
                return "Youtube Trends";
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Says if this object has a keyword
     * @return if this has a keyword
     */
    public boolean hasKeyword(){
        switch (this) {
            case GoogleTrends:
            case YoutubeTrends:
                return false;
            case WebSearch:
            case GoogleTrendingSearches:
                return true;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Says if this object required
     * @return if this requires a keyword
     */
    public boolean requiresKeyword(){
        switch (this) {
            case WebSearch:
            case GoogleTrends:
            case YoutubeTrends:
                return false;
            case GoogleTrendingSearches:
                return true;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Gives access to the associated API Data source class
     * @return the data source class
     */
    public AsyncTask<APISearch, Void, List<TrendingContent>> getAPIWrapper(){
        switch (this) {
            case WebSearch:
                return new WebSearchWrapper();
            case GoogleTrendingSearches:
                return new GTSWrapper();
            case GoogleTrends:
                return new TrendingWrapper();
            case YoutubeTrends:
                return new YoutubeWrapper();
            default:
                throw new IllegalArgumentException();
        }
    }
}
