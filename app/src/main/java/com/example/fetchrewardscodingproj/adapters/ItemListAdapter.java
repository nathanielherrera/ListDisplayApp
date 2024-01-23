package com.example.fetchrewardscodingproj.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fetchrewardscodingproj.R;
import com.example.fetchrewardscodingproj.models.Item;

import java.util.List;
import java.util.function.Consumer;

/**
 * Adapter used to display the list of items via Android RecyclerView
 * */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private List<Item> itemList;

    private final Consumer<Item> onClickCallBack;

    private final Activity activity;

    public ItemListAdapter(List<Item> itemList, Activity activity, Consumer<Item> onClickCallBack) {
        this.itemList = itemList;
        this.activity = activity;
        this.onClickCallBack = onClickCallBack;
    }

    public ItemListAdapter(List<Item> itemList, Activity activity) {
        this(itemList, activity, null);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
        activity.runOnUiThread(this::notifyDataSetChanged);
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {
        Item item = itemList.get(position);
        // Set the title text as the result of calling toString
        holder.title.setText(item.toString());
        holder.layout.setOnClickListener(
                view -> {
                    if (onClickCallBack != null) {
                        onClickCallBack.accept(item);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final LinearLayout layout;
        private final TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.LinearLayout);
            title = itemView.findViewById(R.id.TextViewTitle);
        }
    }
}
