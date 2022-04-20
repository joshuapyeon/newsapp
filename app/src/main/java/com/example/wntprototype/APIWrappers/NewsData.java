package com.example.wntprototype.APIWrappers;

public class NewsData {

    /**
     * The title of an article
     */
    public String title = "";

    /**
     * The source of an article
     */
    public String source = "";

    /**
     * The url of an article
     */
    public String url = "";

    /**
     * The image of an article
     */
    public String urlToImage = "";

    /**
     * A snippet of content from the article
     */
    public String snippet = "";

    /**
     * Says if the news has a title
     * @return if this has a title
     */
    public boolean hasTitle(){
        return !(title.equals(""));
    }

    /**
     * Says if the news has a source
     * @return If this has a source
     */
    public boolean hasSource(){
        return !(source.equals(""));
    }

    /**
     * Says if the news has a url
     * @return if this has a url
     */
    public boolean hasUrl(){
        return !(url.equals(""));
    }

    /**
     * Says if the news has a urlToImage
     * @return if this has a urlToImage
     */
    public boolean hasUrlToImage(){
        return !(urlToImage.equals(""));
    }

    /**
     * Says if the news has a snippet
     * @return if this has a snippet
     */
    public boolean hasSnippet(){
        return !(snippet.equals(""));
    }

}
