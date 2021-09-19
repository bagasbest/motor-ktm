package com.myktmmotor.myktmmotor.ui.services;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myktmmotor.myktmmotor.R;
import com.myktmmotor.myktmmotor.databinding.FragmentServicesBinding;

import org.jetbrains.annotations.NotNull;


public class ServicesFragment extends Fragment {

    private FragmentServicesBinding binding;
    private FirebaseUser user;
    private ServiceAdapter adapter;
    private String status = "Belum Disetujui";
    private String role;

    @Override
    public void onResume() {
        super.onResume();
        getRole();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentServicesBinding.inflate(inflater, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ServiceAddActivity.class));
            }
        });

        // filter berdasarkan status
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.status, android.R.layout.simple_list_item_1);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.statusEt.setAdapter(adapter);
        binding.statusEt.setOnItemClickListener((adapterView, view1, i, l) -> {
            status = binding.statusEt.getText().toString();
            initRecyclerVie();
            initViewModel();
        });

    }

    private void getRole() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (("" + documentSnapshot.get("role")).equals("admin")) {
                        role = "admin";
                    } else {
                        role = "user";
                    }
                    initRecyclerVie();
                    initViewModel();
                });
    }

    private void initRecyclerVie() {
        binding.rvService.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ServiceAdapter();
        binding.rvService.setAdapter(adapter);
    }

    private void initViewModel() {
        ServiceViewModel viewModel = new ViewModelProvider(this).get(ServiceViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        if (role.equals("admin")) {
            viewModel.setAllService(status);
        } else {
            viewModel.setAllServiceByUid(user.getUid(), status);
        }


        viewModel.getServiceList().observe(getViewLifecycleOwner(), service -> {
            if (service.size() > 0) {
                binding.progressBar.setVisibility(View.GONE);
                binding.noData.setVisibility(View.GONE);
                adapter.setData(service);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}