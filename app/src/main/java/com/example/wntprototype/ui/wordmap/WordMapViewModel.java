package com.example.wntprototype.ui.wordmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WordMapViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WordMapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Input your email address and password to store your email account to send articles to others!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}