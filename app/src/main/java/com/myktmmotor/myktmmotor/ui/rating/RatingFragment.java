package com.myktmmotor.myktmmotor.ui.rating;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.myktmmotor.myktmmotor.R;
import com.myktmmotor.myktmmotor.databinding.FragmentRatingBinding;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;


public class RatingFragment extends Fragment {

    private FragmentRatingBinding binding;
    private RatingAdapter adapter;

    int total = 0;
    double totalRating = 0.0;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRatingBinding.inflate(inflater, container, false);

        initRecyclerView();
        initViewModel();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ambil akumulasi rating
        getRating();
        binding.rating.setOnClickListener(view1 -> giveRating());

    }

    private void getRating() {
        FirebaseFirestore
                .getInstance()
                .collection("rate_accumulate")
                .document("rate_accumulate")
                .get()
                .addOnSuccessListener(documentSnapshot -> binding.textView8.setText("Total Penilaian: " + documentSnapshot.get("rating")));
    }

    private void initRecyclerView () {
        binding.rvRating.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RatingAdapter();
        binding.rvRating.setAdapter(adapter);
    }

    private void initViewModel () {
        RatingViewModel viewModel = new ViewModelProvider(this).get(RatingViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.setListRating();
        viewModel.getListRating().observe(getViewLifecycleOwner(), ratingModels -> {
            if (ratingModels.size() > 0) {
                binding.progressBar.setVisibility(View.GONE);
                binding.noData.setVisibility(View.GONE);
                adapter.setData(ratingModels);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void giveRating() {
        Dialog dialog;
        Button btnSubmitRating, btnDismiss;
        RatingBar ratingBar;
        ProgressBar pb;
        EditText commentEt;
        ImageView logo;

        dialog = new Dialog(requireContext());

        dialog.setContentView(R.layout.popup_rating);
        dialog.setCanceledOnTouchOutside(false);


        btnSubmitRating = dialog.findViewById(R.id.submitRating);
        btnDismiss = dialog.findViewById(R.id.dismissBtn);
        ratingBar = dialog.findViewById(R.id.ratingBar);
        commentEt = dialog.findViewById(R.id.commentEt);
        pb = dialog.findViewById(R.id.progress_bar);
        logo = dialog.findViewById(R.id.logo);

        Glide.with(requireContext())
                .load(R.drawable.logo)
                .into(logo);

        btnSubmitRating.setOnClickListener(view -> {
            String comment = commentEt.getText().toString().trim();
            if(ratingBar.getRating() != 0 && !comment.isEmpty()) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                saveRatingInDatabase(ratingBar.getRating(), pb, uid, dialog, comment);
            } else {
                Toast.makeText(requireContext(), "Minimal 1 Bintang & Komentar harus terisi", Toast.LENGTH_SHORT).show();
            }
        });

        btnDismiss.setOnClickListener(view -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void saveRatingInDatabase(float rating, ProgressBar pb, String uid, Dialog dialog, String comment) {

        Map<String, Object> rate = new HashMap<>();
        rate.put("uid", uid);
        rate.put("stars", rating);
        rate.put("comment", comment);


        // simpan rating pengguna ke database
        pb.setVisibility(View.VISIBLE);
        FirebaseFirestore
                .getInstance()
                .collection("rating")
                .document(uid)
                .set(rate)
                .addOnCompleteListener(unused -> {
                    calculateRating(pb, dialog);
                })
                .addOnFailureListener(e -> {
                    pb.setVisibility(View.GONE);
                    dialog.dismiss();
                    Toast.makeText(requireContext(), "Gagal memberi penilaian", Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("SetTextI18n")
    private void calculateRating(ProgressBar pb, Dialog dialog) {

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("rating")
                    .get()
                    .addOnCompleteListener(task -> {
                        total = task.getResult().size();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                // ambil rating jika
                                if (document.exists()) {

                                    double rating = Double.parseDouble("" + document.get("stars"));
                                    totalRating = totalRating + rating;
                                }
                            }

                            String rating = "Total Penilaian: " + String.format("%.1f", (double) totalRating / total ) + " dari " + total + " Penilai";
                            binding.textView8.setText(rating);

                            total = 0;
                            totalRating = 0.0;
                            initRecyclerView();
                            initViewModel();
                            saveRatingToFieldDatabase(rating, pb, dialog);


                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            pb.setVisibility(View.GONE);
                            dialog.dismiss();
                        }
                    });
        } catch (Exception e) {
            pb.setVisibility(View.GONE);
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    private void saveRatingToFieldDatabase(String rating, ProgressBar pb, Dialog dialog) {

        Map<String, Object> rate = new HashMap<>();
        rate.put("rating", rating);

        FirebaseFirestore
                .getInstance()
                .collection("rate_accumulate")
                .document("rate_accumulate")
                .set(rate)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        pb.setVisibility(View.GONE);
                        dialog.dismiss();
                        Toast.makeText(requireContext(), "Berhasil memberi penilaian", Toast.LENGTH_SHORT).show();
                    } else {
                        pb.setVisibility(View.GONE);
                        dialog.dismiss();
                        Toast.makeText(requireContext(), "Gagal memberi penilaian", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}