package com.example.wntprototype.APIWrappers;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.GoogleAPIs.GTSWrapper;
import com.example.wntprototype.APIWrappers.GoogleAPIs.TrendingWrapper;
import com.example.wntprototype.APIWrappers.WebSearchAPI.WebSearchWrapper;

import java.util.List;

public enum DataSource {
    WebSearch,
    GoogleTrendingSearches,
    GoogleTrends;

    @Override
    public String toString() {
        switch (this) {
            case WebSearch:
                return "News";
            case GoogleTrendingSearches:
                return "Trending Searches";
            case GoogleTrends:
                return "Google Trends";
            default:
                throw new IllegalArgumentException();
        }
    }
    public boolean hasKeyword(){
        switch (this) {
            case WebSearch:
                return true;
            case GoogleTrendingSearches:
                return true;
            case GoogleTrends:
                return false;
            default:
                throw new IllegalArgumentException();
        }
    }
    public boolean requiresKeyword(){
        switch (this) {
            case WebSearch:
                return false;
            case GoogleTrendingSearches:
                return true;
            case GoogleTrends:
                return false;
            default:
                throw new IllegalArgumentException();
        }
    }
    public AsyncTask<APISearch, Void, List<TrendingContent>> getAPIWrapper(){
        switch (this) {
            case WebSearch:
                return new WebSearchWrapper();
            case GoogleTrendingSearches:
                return new GTSWrapper();
            case GoogleTrends:
                return new TrendingWrapper();
            default:
                throw new IllegalArgumentException();
        }
    }
}
