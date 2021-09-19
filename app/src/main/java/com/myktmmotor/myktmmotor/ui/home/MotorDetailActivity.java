package com.myktmmotor.myktmmotor.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myktmmotor.myktmmotor.R;
import com.myktmmotor.myktmmotor.databinding.ActivityMotorDetailBinding;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MotorDetailActivity extends AppCompatActivity {

    public static final String EXTRA_DETAIL = "model";
    private ActivityMotorDetailBinding binding;
    private MotorModel model;
    private FirebaseUser user;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMotorDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = FirebaseAuth.getInstance().getCurrentUser();

        model = getIntent().getParcelableExtra(EXTRA_DETAIL);
        Glide.with(this)
                .load(model.getDp())
                .into(binding.roundedImageView);

        binding.name.setText(model.getName());
        binding.model.setText("Model: " + model.getModel());
        binding.year.setText(model.getYear());

        binding.spec.setText(model.getSpec());
        binding.color.setText(model.getColor());
        binding.topSpeed.setText(model.getTopSpeed() + " km/h");

        NumberFormat formatter = new DecimalFormat("#,###");
        binding.price.setText("Rp. " + formatter.format(Double.parseDouble((model.getPrice()))));

        getRole();

        binding.backButton.setOnClickListener(view -> onBackPressed());

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDeleteDialog();
            }
        });

    }

    private void showConfirmDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Menghapus Motor")
                .setMessage("Apakah anda yakin ingin menghapus motor ini ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {

                    // delete service
                    ProgressDialog mProgressDialog = new ProgressDialog(this);

                    mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();


                    FirebaseFirestore
                            .getInstance()
                            .collection("motor")
                            .document(model.getMotorId())
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        mProgressDialog.dismiss();
                                        showSuccessDeleteMotorDialog();
                                    } else {
                                        mProgressDialog.dismiss();
                                        showFailureDeleteMotorDialog();
                                    }
                                }
                            });
                })
                .setNegativeButton("TIDAK", (dialog, i) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void getRole() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(("" + documentSnapshot.get("role")).equals("admin")) {
                          binding.deleteBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void showSuccessDeleteMotorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Menghapus Motor")
                .setMessage("Selamat, anda berhasil menghapus motor ini")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    private void showFailureDeleteMotorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Menghapus Service")
                .setMessage("Terdapat kesalahan ketika ingin menghapus motor ini, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}