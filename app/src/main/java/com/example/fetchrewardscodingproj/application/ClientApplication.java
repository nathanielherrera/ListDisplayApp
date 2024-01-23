package com.example.fetchrewardscodingproj.application;

import android.app.Application;
import android.util.Log;

import com.example.fetchrewardscodingproj.network.Client;

public final class ClientApplication extends Application {

    /** Item API client created during application startup*/
    private Client client;


    /** Start the app, in this case only a client network
     * Connects to an outside server
     */
    @Override
    public void onCreate() {
        Log.d("Application", "I got to here");
        super.onCreate();
        client = Client.start();
    }
    /** Retrieve the item API client instance for the app */
    public Client getClient() { return client; }
}
