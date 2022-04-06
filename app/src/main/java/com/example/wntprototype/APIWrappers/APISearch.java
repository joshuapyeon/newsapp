package com.example.wntprototype.APIWrappers;

import com.example.wntprototype.APIWrappers.NewsAPI.Category;
import com.example.wntprototype.APIWrappers.NewsAPI.Country;

/**
 * This Class is what is passed to the data pull with search parameters
 */
public class APISearch {

    /**
     * The query for the data pull to search
     */
    private boolean containsQuery = false;

    /**
     * the Query
     */
    private String query;

    /**
     * The Data source
     */
    private DataSource source;

    /**
     * returns the source
     * @return the source
     */
    public DataSource getSource() {
        return source;
    }

    /**
     * Sets the Searches source
     * @param source the source of the data pull
     */
    public void setSource(DataSource source) {
        this.source = source;
        this.hasSource = true;
    }

    /**
     * Says if the search has a data source
     */
    private boolean hasSource = false;

    /**
     *  This returns the query.
     * @return The Query for the Search
     */
    public String getQuery(){
        return query;
    }

    /**
     * This method gives the query a value.
     * @param s The String to set the query.
     */
    public void setQuery(String s){
        if(!s.equals("")) {
            containsQuery = true;
            query = s;
        }
    }

    /**
     * Clears the Query.
     */
    public void clearQuery(){
        query = "";
        containsQuery = false;
    }

    /**
     * Tells if the search has a query.
     * @return A boolean value saying if the search has a query
     */
    public boolean hasQuery(){
        return containsQuery;
    }

}
