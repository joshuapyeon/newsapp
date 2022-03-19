package com.example.wntprototype.APIWrappers.GoogleAPIs;

import com.example.wntprototype.APIWrappers.APIData;

public class GTSData extends APIData {

    /**
     * The trending search content.
     */
    public String content;

    @Override
    public void setToParse() {
        toParse = content;
    }
}
