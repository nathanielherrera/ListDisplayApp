package com.example.fetchrewardscodingproj.activities;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;


import com.example.fetchrewardscodingproj.Helpers.ResultMightThrow;
import com.example.fetchrewardscodingproj.R;
import com.example.fetchrewardscodingproj.adapters.ItemListAdapter;
import com.example.fetchrewardscodingproj.application.ClientApplication;
import com.example.fetchrewardscodingproj.models.Item;


import java.util.concurrent.ExecutorService;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/** Main Activity showing the item list*/
public final class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    /** List of items received by the server (empty at first) */
    private List<Item> itemList = Collections.emptyList();

    /** Adapter that connects list of items to the list displayed to the user */
    private ItemListAdapter listAdapter;

    /** Service to initiate a Client request on separate UiThread */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /** Called when this activity is created
     *
     * @param unused instance state, always empty or null
     *
     * */
    @Override
    protected void onCreate(@Nullable Bundle unused) {
        super.onCreate(unused);
        setContentView(R.layout.activity_main);
        setTitle("Search Items");
        
        listAdapter = new ItemListAdapter(itemList, this);
        // Add the list to the layout
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
        // Initiate a request for the item list
        executorService.submit(() -> {
            ClientApplication application = (ClientApplication) getApplication();
            ResultMightThrow<List<Item>> result = application.getClient().getItem(itemCallBack);
            runOnUiThread(() -> handleItemListResult(result));
        });
        // Register as a callback for changes to the search view component
        // Used to initiate item list filtering.
        SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);

        // Register toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
    }

    /** More general purpose callback to update the UI with items (called on by itemCallBack) */
    public void handleItemListResult(ResultMightThrow<List<Item>> result) {
        try {
            if (result != null) {
                itemList = result.getValue();
                Log.d("MainActivity", "Item List Size: " + itemList.size());
                listAdapter.setItemList(itemList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error updating UI with Items", e);
        }
    }

    /** Callback used to update the list of items during onCreate (designed for use with getItem method) */
    private final Consumer<ResultMightThrow<List<Item>>> itemCallBack =
            (result) -> {
                try {
                    if (result != null) {
                        itemList = result.getValue();
                        List<Item> groupedAndSortedItems = filterAndSort(itemList);
                        Log.d("MainActivity", "ItemList: " + groupedAndSortedItems);
                        listAdapter.setItemList(groupedAndSortedItems);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MainActivity", "getItem threw an exception", e);
                }
            };

    /** Helper method used to filter, group, and sort list of items as per task instructions
     * */
    public List<Item> filterAndSort(List<Item> itemList) {
        List<Item> filteredItems = itemList.stream()
                .filter(item -> item.getName() != null && !item.getName().isEmpty())
                .collect(Collectors.toList());
        //Group items by listId, then sort by listID and then name
        return filteredItems.stream()
                .sorted(Item.getComparator())
                .collect(Collectors.toList());
    }


    /** Callback used when user changes the text in the searchbar
     *
     * <p >Is handled by updating the visible list of items
     *
     * <p>Current functionality favors looking for specific item
     * e.g: entering '2' displays items w/ listId of 2
     * and other listId's whose names contain '2'
     * Filtering by specific listId requires 'ListId: [desiredListId]' (not case sensitive)
     *
     * @param query the user input edit to filter list by
     * @return true as the action is handled
     * */
    @Override
    public boolean onQueryTextChange(String query) {
        List<Item> newList = Item.filter(itemList, query);
        List<Item> groupedAndSortedItems = filterAndSort(newList);
        listAdapter.setItemList(groupedAndSortedItems);
        return true;
    }

    /** Because list is updated on each change to the search value
     * this callback does not need to be handles
     * */
    @Override
    public boolean onQueryTextSubmit(String unused) { return false; }
}