package com.myktmmotor.myktmmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.myktmmotor.myktmmotor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tampilkan logo untuk splash screen
        Glide.with(this)
                .load(R.drawable.logo)
                .into(binding.logo);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            // ke halaman login setelah 4 detik
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }, 4000);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}