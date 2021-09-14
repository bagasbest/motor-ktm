package com.myktmmotor.myktmmotor.ui.rating;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myktmmotor.myktmmotor.R;
import com.myktmmotor.myktmmotor.databinding.FragmentRatingBinding;

import org.jetbrains.annotations.NotNull;


public class RatingFragment extends Fragment {

    private FragmentRatingBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRatingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}