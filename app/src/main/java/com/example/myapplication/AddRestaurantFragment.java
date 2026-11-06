package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

public class AddRestaurantFragment extends Fragment {
    private EditText etName, etType, etDescription, etLocation;
    private ImageView ivLogo;
    private Button btnAdd;

    private Uri selectedImageUri;
    private FirebaseServices fbs;

    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    ivLogo.setImageURI(uri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_restaurant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbs = FirebaseServices.getInstance();

        etName = view.findViewById(R.id.etName);
        etType = view.findViewById(R.id.etType);
        etDescription = view.findViewById(R.id.etDescription);
        etLocation = view.findViewById(R.id.etLocation);
        ivLogo = view.findViewById(R.id.ivLogo);
        btnAdd = view.findViewById(R.id.btnAdd);

        ivLogo.setOnClickListener(v -> pickImage.launch("image/*"));
        btnAdd.setOnClickListener(v -> addRestaurant());
    }

    private void addRestaurant() {
        String name = etName.getText().toString().trim();
        String type = etType.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || type.isEmpty() || location.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in name, type and location",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageUri == null) {
            Toast.makeText(getContext(), "Please pick a logo", Toast.LENGTH_SHORT).show();
            return;
        }

        btnAdd.setEnabled(false);

        DocumentReference doc = fbs.getDb().collection("restaurants").document();
        StorageReference logoRef = fbs.getStorage().getReference()
                .child("logos/" + doc.getId() + ".jpg");

        logoRef.putFile(selectedImageUri)
                .addOnSuccessListener(snapshot -> {
                    logoRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> saveRestaurant(doc, name, type,
                                    description, location, uri.toString()))
                            .addOnFailureListener(this::showError);
                })
                .addOnFailureListener(this::showError);
    }

    private void saveRestaurant(DocumentReference doc, String name, String type,
                                String description, String location, String logoUrl) {
        Restaurant restaurant = new Restaurant(doc.getId(), name, type,
                description, location, logoUrl);

        doc.set(restaurant)
                .addOnSuccessListener(unused -> {
                    if (!isAdded()) return;
                    Toast.makeText(getContext(), "Restaurant added!",
                            Toast.LENGTH_SHORT).show();
                    MainActivity activity = (MainActivity) requireActivity();
                    activity.showFragment(new ShowReF());
                })
                .addOnFailureListener(this::showError);
    }

    private void showError(Exception e) {
        if (!isAdded()) return;
        btnAdd.setEnabled(true);
        Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
