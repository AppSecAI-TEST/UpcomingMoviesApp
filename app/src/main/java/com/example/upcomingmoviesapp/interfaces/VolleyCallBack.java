package com.example.upcomingmoviesapp.interfaces;

import org.json.JSONArray;

public interface VolleyCallBack {

    public void getResponse(JSONArray jsonArray, String tag);

    public void getError(String error);

    public void displayFailedResponse(String failedMessage);

}
