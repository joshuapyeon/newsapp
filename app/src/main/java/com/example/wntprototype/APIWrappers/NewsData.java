package com.example.wntprototype.APIWrappers;

import com.example.wntprototype.APIWrappers.APIData;

public class NewsData extends APIData {

    /**
     * The title of an article
     */
    public String title;

    /**
     * The source of an article
     */
    public String source;

    /**
     * The url of an article
     */
    public String url;

    /**
     * The image of an article
     */
    public String urlToImage;

    /**
     * A snippet of content from the article
     */
    public String snippet;

    @Override
    public void setToParse() {
        toParse = title;
    }
}
