package com.example.wntprototype.APIWrappers.WebSearchAPI;

import com.example.wntprototype.APIWrappers.APIData;

public class WebSearchData extends APIData {
    public String title;
    public String description;
    public String url;
    public String body;
    public String snippet;
    public String source;


    @Override
    public void setToParse() {
        toParse = title;
    }
}
