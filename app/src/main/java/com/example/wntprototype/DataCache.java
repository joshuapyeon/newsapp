package com.example.wntprototype;

import com.example.wntprototype.APIWrappers.APIData;

import java.util.List;

public class DataCache {

    /**
     * Singleton Instance that has a global DataCache.
     */
    private static DataCache cache = new DataCache();

    /**
     * The data for all the classes to access
     */
    private List<APIData> data = null;

    /**
     * This variable checks of there is data (to help with null exception errors)
     */
    private Boolean dataNull = false;

    /**
     * Private constructor
     */
    private DataCache(){
    }

    /**
     * This gives access to the variable saying if the cache has data.
     * @return Says if there is data in the cache
     */
    public Boolean hasData(){
        return dataNull;
    }

    /**
     * Gives access to the Cached data
     * @return The Data from the sources
     */
    public List<APIData> getData() {
        return data;
    }

    /**
     * Sets the data for global use.
     * @param data The data from the API data pull.
     */
    public void setData(List<APIData> data) {
        if(data != null) {
            this.data = data;
            dataNull = true;
        }
    }

    /**
     * Clears the Data in the Cache
     */
    public void clearData(){
        data = null;
        dataNull = false;
    }

    /**
     * Public accessor to the Singleton class
     * @return The instance of DataCache
     */
    public static DataCache getCache() {
        return cache;
    }
}
