package com.example.go4lunchjava.restaurant_details.pojo_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantDetailsResponse {

    @SerializedName("result")
    @Expose
    private RestaurantDetailsResult result;

    @SerializedName("html_attributions")
    @Expose
    private String[] html_attributions;

    @SerializedName("status")
    @Expose
    private String status;

    public RestaurantDetailsResult getResult() {
        return result;
    }
}
