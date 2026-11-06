package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class ShowReF extends Fragment
        implements AdapterRe.OnRestaurantActionListener {

    private final ArrayList<Restaurant> restaurants = new ArrayList<>();
    private AdapterRe adapter;
    private TextView tvEmpty;
    private FirebaseServices fbs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_re, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbs = FirebaseServices.getInstance();
        tvEmpty = view.findViewById(R.id.tvEmpty);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerRestaurants);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new AdapterRe(restaurants, this);
            recyclerView.setAdapter(adapter);
        }

        loadRestaurants();
    }

    private void loadRestaurants() {
        fbs.getDb().collection("restaurants").get()
                .addOnSuccessListener(result -> {
                    if (!isAdded()) return;
                    restaurants.clear();
                    for (DocumentSnapshot document : result.getDocuments()) {
                        Restaurant restaurant = document.toObject(Restaurant.class);
                        if (restaurant != null) {
                            restaurant.setId(document.getId());
                            restaurants.add(restaurant);
                        }
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    if (tvEmpty != null) {
                        tvEmpty.setVisibility(restaurants.isEmpty() ? View.VISIBLE : View.GONE);
                    }
                })
                .addOnFailureListener(e -> showMessage("Could not load: " + e.getMessage()));
    }

    @Override
    public void onUpdateClick(Restaurant restaurant) {
        EditReDio dialog = EditReDio.newInstance(restaurant);
        dialog.setOnSavedListener(this::loadRestaurants);
        dialog.show(getChildFragmentManager(), "edit_restaurant");
    }

    @Override
    public void onDeleteClick(Restaurant restaurant) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete restaurant")
                .setMessage("Are you sure you want to delete "
                        + restaurant.getName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> deleteRestaurant(restaurant))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteRestaurant(Restaurant restaurant) {
        fbs.getDb().collection("restaurants").document(restaurant.getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    showMessage("Restaurant deleted");
                    loadRestaurants();
                })
                .addOnFailureListener(e -> showMessage("Delete failed: " + e.getMessage()));
    }

    private void showMessage(String message) {
        if (!isAdded()) return;
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}