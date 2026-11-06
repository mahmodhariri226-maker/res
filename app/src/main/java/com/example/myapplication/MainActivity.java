package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnShow = findViewById(R.id.btnShow);

        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> showFragment(new AddRestaurantFragment()));
        }
        if (btnShow != null) {
            btnShow.setOnClickListener(v -> showFragment(new ShowReF()));
        }

        if (savedInstanceState == null) {
            showFragment(new Admin());
        }
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
