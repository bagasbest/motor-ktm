package com.myktmmotor.myktmmotor.ui.services;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myktmmotor.myktmmotor.R;
import com.myktmmotor.myktmmotor.databinding.FragmentServicesBinding;

import org.jetbrains.annotations.NotNull;


public class ServicesFragment extends Fragment {

  private FragmentServicesBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentServicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}