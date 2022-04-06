package com.example.wntprototype.APIWrappers;

/**
 * This Class is a parent class to any return value of an API
 */
public abstract class APIData {

    /**
     * The String from the Data that will be parsed.
     */
    protected String toParse = "";

    /**
     * This method will be determined by the child class, where the APIData decides
     * what it wants to parse
     */
    protected abstract void setToParse();

    /**
     * Gives access to the string toParse
     * @return The String to be parsed
     */
    public String getToParse(){
        return toParse;
    }
}
