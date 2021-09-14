package com.myktmmotor.myktmmotor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.myktmmotor.myktmmotor.databinding.ActivityHomepageBinding;
import com.myktmmotor.myktmmotor.ui.home.HomeFragment;
import com.myktmmotor.myktmmotor.ui.rating.RatingFragment;
import com.myktmmotor.myktmmotor.ui.services.ServicesFragment;

public class HomepageActivity extends AppCompatActivity {

    private ActivityHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ChipNavigationBar navView = findViewById(R.id.nav_view);

        navView.setItemSelected(R.id.navigation_home, true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new HomeFragment()).commit();


        bottomMenu(navView);
    }

    @SuppressLint("NonConstantResourceId")
    private void bottomMenu(ChipNavigationBar navView) {
        navView.setOnItemSelectedListener
                (i -> {
                    Fragment fragment = null;
                    switch (i){
                        case R.id.navigation_home:
                            fragment = new HomeFragment();
                            break;
                        case R.id.navigation_service:
                            fragment = new ServicesFragment();
                            break;
                        case R.id.navigation_rating:
                            fragment = new RatingFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,
                                    fragment).commit();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}