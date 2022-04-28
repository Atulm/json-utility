package com.example.demo.utils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExternalAPIUtils {

    public static Response execute(final String requestURL) throws Exception {
        final OkHttpClient client = new OkHttpClient.Builder().build();
        final MediaType mediaType = MediaType.parse("application/json");
        final Request request = new Request.Builder().url(requestURL).addHeader("Content-Type", "application/json")
                .build();
        final Response response = client.newCall(request).execute();
        return response;
    }
}
