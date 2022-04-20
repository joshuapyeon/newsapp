package com.example.wntprototype.APIWrappers;

import com.example.wntprototype.DataCache;

import java.util.List;

public class DataManager {

    /**
     * Reference to the global data cache
     */
    private DataCache cache = DataCache.getCache();

    /**
     * Constructor
     */
    public DataManager(){
    }

    /**
     * I am putting the responsibility of potential data management in this class
     * @param search
     */
    public void buildData(APISearch search){
        List<TrendingContent> data = null;
        try {
            data = search.getSource().getAPIWrapper().execute(search).get();
        } catch (Exception e){
            e.printStackTrace();
        }
        if(search.getSource().hasKeyword()){
            cache.setKeywordSearched(search.getQuery());
        }else{
            cache.setKeywordSearched("");
        }
        cache.setData(data);
    }

}
