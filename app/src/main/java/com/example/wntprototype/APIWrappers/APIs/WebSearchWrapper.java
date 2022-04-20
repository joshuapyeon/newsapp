package com.example.wntprototype.APIWrappers.APIs;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.NewsData;
import com.example.wntprototype.APIWrappers.TrendingContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This gives the top trending stories
 */
public class WebSearchWrapper extends AsyncTask<APISearch, Void, List<TrendingContent>> {

    private final String api_key = "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d";

    @Override
    protected List<TrendingContent> doInBackground(APISearch... apiSearches) {
        OkHttpClient client = new OkHttpClient();

        //Code that builds the date parameter
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);  // number of days to add
        String yesterdaysDate = sdf.format(c.getTime());  // dt is now the new date
        List<TrendingContent> toReturn = new ArrayList<TrendingContent>();

        Request request = null;
        if(apiSearches[0].hasQuery()) {
            String encodedQuery = URLEncoder.encode(apiSearches[0].getQuery());
            request = new Request.Builder()
                    .url("https://contextualwebsearch-websearch-v1.p.rapidapi.com/api/search/NewsSearchAPI?q=" + encodedQuery + "&pageNumber=1&pageSize=50&autoCorrect=true&fromPublishedDate=" + yesterdaysDate + "&toPublishedDate=null")
                    .get()
                    .addHeader("X-RapidAPI-Host", "contextualwebsearch-websearch-v1.p.rapidapi.com")
                    .addHeader("X-RapidAPI-Key", api_key)
                    .build();
            toReturn.addAll(webSearchFormatter(request,client));
        }else {
            request = new Request.Builder()
                    .url("https://google-news.p.rapidapi.com/v1/top_headlines?lang=en&country=US")
                    .get()
                    .addHeader("X-RapidAPI-Host", "google-news.p.rapidapi.com")
                    .addHeader("X-RapidAPI-Key", "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d")
                    .build();
            toReturn.addAll(googleNewsFormatter(request,client));
            request = new Request.Builder()
                    .url("https://contextualwebsearch-websearch-v1.p.rapidapi.com/api/search/TrendingNewsAPI?pageNumber=1&pageSize=50&withThumbnails=false&location=us")
                    .get()
                    .addHeader("x-rapidapi-host", "contextualwebsearch-websearch-v1.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", api_key)
                    .build();
            toReturn.addAll(webSearchFormatter(request,client));
        }
        return toReturn;
    }

    private List<TrendingContent> webSearchFormatter(Request request, OkHttpClient client){
        List<TrendingContent> toReturn = new ArrayList<TrendingContent>();
        try {
            Response response = client.newCall(request).execute();
            if(response.code() != 200) return toReturn;
            JSONObject data = new JSONObject(response.body().string());
            JSONArray values = data.optJSONArray("value");
            for(int i = 0; i < values.length(); i++){
                NewsData temp = new NewsData();
                TrendingContent content = new TrendingContent();
                JSONObject article = values.optJSONObject(i);
                temp.title = article.optString("title");
                temp.snippet = article.optString("snippet");
                temp.url = article.optString("url");
                temp.source = article.optJSONObject("provider").optString("name");
                temp.urlToImage = article.optJSONObject("image").optString("url");
                content.setSources(new ArrayList<NewsData>());
                content.getSources().add(temp);
                content.setUrlToImage(temp.urlToImage);
                content.setValue(1);
                content.setPhrase(temp.title);
                toReturn.add(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private List<TrendingContent> googleNewsFormatter(Request request, OkHttpClient client){
        List<TrendingContent> toReturn = new ArrayList<TrendingContent>();
        try{
            Response response = client.newCall(request).execute();
            if(response.code() != 200) return toReturn;
            JSONObject base = new JSONObject(response.body().string());
            JSONArray articles = base.optJSONArray("articles");
            for(int i = 0; i < articles.length(); i++){
                TrendingContent temp = new TrendingContent();
                JSONObject article = articles.getJSONObject(i);
                temp.setPhrase(article.optString("title"));
                List<NewsData> sources = new ArrayList<>();
                JSONArray subArticles = article.optJSONArray("sub_articles");
                for(int j = 0; j < subArticles.length(); j++){
                    NewsData source = new NewsData();
                    JSONObject subArticle = subArticles.optJSONObject(j);
                    source.url = subArticle.optString("url");
                    source.title = subArticle.optString("title");
                    source.source = subArticle.optString("publisher");
                    sources.add(source);
                }
                temp.setSources(sources);
                temp.setValue(sources.size());
                toReturn.add(temp);
            }
        }catch (Exception e){

        }
        return toReturn;
    }
}
