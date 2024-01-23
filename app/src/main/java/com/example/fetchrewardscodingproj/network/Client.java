package com.example.fetchrewardscodingproj.network;

import static com.example.fetchrewardscodingproj.Helpers.Helpers.OBJECT_MAPPER;


import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.StringRequest;
import com.example.fetchrewardscodingproj.Helpers.ResultMightThrow;
import com.example.fetchrewardscodingproj.models.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Client {

    /** URL used to retrieve item JSON */
    private final String API_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";


    /**
     * Make request to the server (URL) and receive JSON data
     *
     * @param callback used to receive the result
     */
    public ResultMightThrow<List<Item>> getItem(@NonNull Consumer<ResultMightThrow<List<Item>>> callback) {

        StringRequest itemRequest = new StringRequest(
                Request.Method.GET,
                API_URL,
                response -> {
                    try {
                        List<Item> itemList = OBJECT_MAPPER.readValue(response, new TypeReference<List<Item>>() {});
                        callback.accept(new ResultMightThrow<>(itemList));
                    } catch (JsonProcessingException e) {
                        callback.accept(new ResultMightThrow<>(e));
                    }
                },
                error -> callback.accept(new ResultMightThrow<>(error))) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
                headers.put("Referer", "https://fetch-hiring.s3.amazonaws.com/mobile.html");
                return headers;
            }
        };
        Log.d("Client", "Made it to requestQueue");
        requestQueue.add(itemRequest);
        return new ResultMightThrow<>(new ArrayList<>());
    }

    /**  Client instance to restrict instantiation */
    private static Client instance;

    /** Start the API Client
     *
     * @return API Client instance
     * */
    public static Client start() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    /** Queue for requests */
    private final RequestQueue requestQueue;

    /** Private constructor to initialize and start requestQueue*/
    private Client() {
        Cache cache = new NoCache();
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }
}
