package com.example.getinshape_v3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.getinshape_v3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements
                                    HomeFragment.OnFragmentInteractionListener,
                                    SearchFragment.OnFragmentInteractionListener,
                                    DiaryFragment.OnFragmentInteractionListener,
                                    FragmentChangeListener {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.search:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.diary:
                    replaceFragment(new DiaryFragment());
                    break;
            }
            return true;
        });

        // Handle the icon focus change when fragments switch
        // Add the OnBackStackChangedListener to keep track of fragment changes
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackEntryCount > 0) {
                String topFragmentTag = getSupportFragmentManager().getBackStackEntryAt(backStackEntryCount - 1).getName();
                switchSelectedIcon(topFragmentTag);
            }
        });

    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.setReorderingAllowed(true);

//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.addToBackStack(fragment.getClass().getName()); // Set the fragment tag in the back stack
        fragmentTransaction.commit();
    }

    // Handle the icon focus change when fragments switch
    private void switchSelectedIcon(String tag) {
        if (tag != null) {
            int itemId = R.id.home; // Default selected icon
            switch (tag) {
                case "com.example.getinshape_v3.HomeFragment":
                    itemId = R.id.home;
                    break;
                case "com.example.getinshape_v3.SearchFragment":
                    itemId = R.id.search;
                    break;
                case "com.example.getinshape_v3.DiaryFragment":
                    itemId = R.id.diary;
                    break;
            }

            // Check if the selected item is already the same as the one to be selected
            if (binding.bottomNavigationView.getSelectedItemId() != itemId) {
                binding.bottomNavigationView.setSelectedItemId(itemId);
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}