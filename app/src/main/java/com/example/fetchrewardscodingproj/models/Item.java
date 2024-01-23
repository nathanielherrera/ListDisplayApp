package com.example.fetchrewardscodingproj.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Model holding the item information shown in the item list
 * */
public class Item {
    private String name;

    public final String getName() {
        return name;
    }
    private int listId;

    public final int getListId() {
        return listId;
    }
    private int Id;

    public final int getId() {
        return Id;
    }

    @NonNull
    @Override
    public String toString() {
        return "List Id: " + listId + " " + name;
    }

    /** Create an empty Item. */
    public Item() {}

    /** Create an Item with set parameters */
    public Item(@NonNull String name, int listId, int Id) {
        this.name = name;
        this.listId = listId;
        this.Id = Id;
    }

    /** Extracts the numeric value of the item name
     *
     * @return the numeric part of the item name in int form
     * */
    private int extractNumeric() {
        String numericPart = name.replaceAll("\\D", "");
        if (numericPart.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(numericPart);
    }

    /** Comparator used in MainActivity filterAndSort() method */
    public static Comparator<Item> getComparator() {
        return Comparator.comparing(Item::getListId)
                .thenComparing(Item::extractNumeric);
    }


    /** Used to filter list of items per user input */
    public static List<Item> filter(List<Item> list, String filter) {
        String trim = filter.trim().toLowerCase();
        List<Item> newList = new ArrayList<>();
        for (Item item : list) {
            if (item.toString().toLowerCase().contains(trim)) {
                newList.add(item);
            }
        }
        return newList;
    }
}
