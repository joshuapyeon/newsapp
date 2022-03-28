package com.example.wntprototype.APIWrappers.NewsAPI;

import com.example.wntprototype.APIWrappers.APIData;

public class NewsAPIData extends APIData {
    public String sourceName;
    public String author;
    public String title;
    public String url;
    public String urlToImage;
    public String publishDate;
    public String description;
    public String content;

    @Override
    public void setToParse() {
        toParse = title;
    }
}