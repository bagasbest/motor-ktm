package com.myktmmotor.myktmmotor.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myktmmotor.myktmmotor.HomepageActivity;
import com.myktmmotor.myktmmotor.LoginActivity;
import com.myktmmotor.myktmmotor.R;
import com.myktmmotor.myktmmotor.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MotorAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerView();
        initViewModel("all");
        getRole();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.motorAdd.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), MotorAddActivity.class)));

        binding.logoutBtn.setOnClickListener(view12 -> logout());

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()) {
                    initRecyclerView();
                    initViewModel(editable.toString());
                } else {
                    initRecyclerView();
                    initViewModel("all");
                }
            }
        });
    }

    private void logout() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah anda yakin ingin keluar apliaksi ?")
                .setIcon(R.drawable.ic_baseline_exit_to_app_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    // sign out dari firebase autentikasi
                    FirebaseAuth.getInstance().signOut();

                    // go to login activity
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogInterface.dismiss();
                    startActivity(intent);


                })
                .setNegativeButton("TIDAK", (dialog, i) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void initRecyclerView() {
        // tampilkan daftar motor
        binding.rvMotor.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new MotorAdapter();
        binding.rvMotor.setAdapter(adapter);
    }

    // inisiasi view model untuk menampilkan list produk
    private void initViewModel(String query) {
        MotorViewModel productViewModel = new ViewModelProvider(this).get(MotorViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        if(query.equals("all")) {
            productViewModel.setListMotor();
        } else {
            productViewModel.setListMotorByQuery(query);
        }
        productViewModel.getMotorList().observe(getViewLifecycleOwner(), productList -> {
            if (productList.size() > 0) {
                binding.progressBar.setVisibility(View.GONE);
                binding.noData.setVisibility(View.GONE);
                adapter.setData(productList);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getRole() {
        // CEK APAKAH USER YANG SEDANG LOGIN ADMIN ATAU BUKAN, JIKA YA, MAKA TAMPILKAN tombol add motor
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(("" + documentSnapshot.get("role")).equals("admin")) {
                        binding.motorAdd.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}