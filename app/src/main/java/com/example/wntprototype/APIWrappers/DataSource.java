package com.example.wntprototype.APIWrappers;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APIs.DictWordsWrapper;
import com.example.wntprototype.APIWrappers.APIs.GTSWrapper;
import com.example.wntprototype.APIWrappers.APIs.SpotifyWrapper;
import com.example.wntprototype.APIWrappers.APIs.TrendingWrapper;
import com.example.wntprototype.APIWrappers.APIs.YoutubeWrapper;
import com.example.wntprototype.APIWrappers.APIs.WebSearchWrapper;

import java.util.List;

public enum DataSource {
    WebSearch,
    GoogleTrendingSearches,
    GoogleTrends,
    YoutubeTrends,
    DictionaryWords,
    SpotifyTrends;

    @Override
    public String toString() {
        switch (this) {
            case WebSearch:
                return "Trending News";
            case GoogleTrendingSearches:
                return "Related Searches";
            case GoogleTrends:
                return "Google Trends";
            case YoutubeTrends:
                return "Youtube Trends";
            case DictionaryWords:
                return "Dictionary Words";
            case SpotifyTrends:
                return "Trending Songs";
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
            case DictionaryWords:
            case SpotifyTrends:
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
            case DictionaryWords:
            case SpotifyTrends:
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
            case DictionaryWords:
                return new DictWordsWrapper();
            case SpotifyTrends:
                return new SpotifyWrapper();
            default:
                throw new IllegalArgumentException();
        }
    }
}
