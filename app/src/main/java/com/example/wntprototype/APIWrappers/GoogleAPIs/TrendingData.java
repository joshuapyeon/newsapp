package com.example.wntprototype.APIWrappers.GoogleAPIs;

import com.example.wntprototype.APIWrappers.APIData;

import java.util.List;

public class TrendingData extends APIData {

    public String title;

    @Override
    public void setToParse() {
        toParse = title;
    }
}
