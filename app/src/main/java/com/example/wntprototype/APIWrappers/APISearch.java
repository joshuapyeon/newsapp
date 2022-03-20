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
    private boolean containsQuery;
    private String query;

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
        containsQuery = true;
        query = s;
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

    /**
     * Something we can re-add later if needed, I just Defaulted it to United States because I
     * Don't think the user will need to change the country any time soon.
     */
//    public boolean hasCountry;
//    public Country country;

    /**
     * The rest of these are non functional, they were supposed to be for NewsAPI but I am generalizing
     * so it can work for any API call.
     */
//    public boolean hasLanguage;
//    public String language;

//    public boolean hasSource;
//    public String source;

//    public boolean hasCategory;
//    public Category category;

//    public boolean hasDomain;
//    public String domain;
}
