package com.myktmmotor.myktmmotor.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.myktmmotor.myktmmotor.R;
import com.myktmmotor.myktmmotor.databinding.ActivityMotorDetailBinding;

public class MotorDetailActivity extends AppCompatActivity {

    private ActivityMotorDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMotorDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}