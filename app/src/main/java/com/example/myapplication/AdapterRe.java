package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * The adapter connects our list of restaurants to the RecyclerView.
 */
public class AdapterRe extends RecyclerView.Adapter<AdapterRe.AdapterReViewHolder> {

    public interface OnRestaurantActionListener {
        void onUpdateClick(Restaurant restaurant);
        void onDeleteClick(Restaurant restaurant);
    }

    private final ArrayList<Restaurant> restaurants;
    private final OnRestaurantActionListener listener;

    public AdapterRe(ArrayList<Restaurant> restaurants, OnRestaurantActionListener listener) {
        this.restaurants = restaurants;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterReViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new AdapterReViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);

        holder.tvName.setText(restaurant.getName());
        holder.tvType.setText(restaurant.getType());
        holder.tvLocation.setText(restaurant.getLocation());

        Glide.with(holder.itemView.getContext())
                .load(restaurant.getLogoUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivLogo);

        holder.btnUpdate.setOnClickListener(v -> listener.onUpdateClick(restaurant));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(restaurant));
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    static class AdapterReViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLogo;
        TextView tvName, tvType, tvLocation;
        Button btnUpdate, btnDelete;

        AdapterReViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
