package com.softserve.edu.services.api;

import com.softserve.edu.services.api.placeholder.JsonPlaceHolderApi;

public class Services {

    private Services() {
        // Default private constructor, no object creation
    }

    public static JsonPlaceHolderApi placeHolderApi() {
        return new JsonPlaceHolderApi();
    }

}
