package com.automatium.action;

/**
 * Created by Gurusharan on 27-11-2016.
 */
public class NewPage implements UIAction {
    private String url;

    public NewPage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
