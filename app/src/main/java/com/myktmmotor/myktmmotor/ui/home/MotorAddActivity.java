package com.myktmmotor.myktmmotor.ui.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myktmmotor.myktmmotor.R;
import com.myktmmotor.myktmmotor.databinding.ActivityMotorAddBinding;
import java.util.HashMap;
import java.util.Map;

public class MotorAddActivity extends AppCompatActivity {

    private ActivityMotorAddBinding binding;
    private String dp;
    private static final int REQUEST_FROM_GALLERY = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMotorAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(view -> onBackPressed());


        // KLIK TAMBAH GAMBAR
        binding.imageHint.setOnClickListener(view -> ImagePicker.with(MotorAddActivity.this)
                .galleryOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(REQUEST_FROM_GALLERY));

        binding.uploadMotor.setOnClickListener(view -> formValidation());

    }

    private void formValidation() {
        String name = binding.nameEt.getText().toString().trim();
        String model = binding.modelEt.getText().toString().trim();
        String price = binding.priceEt.getText().toString();
        String topSpeed = binding.topSpeedEt.getText().toString();
        String color = binding.colorEt.getText().toString().trim();
        String year = binding.launchYearEt.getText().toString().trim();
        String spec = binding.specEt.getText().toString().trim();


        if(name.isEmpty()) {
            Toast.makeText(MotorAddActivity.this, "Nama Motor tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(model.isEmpty()) {
            Toast.makeText(MotorAddActivity.this, "Model Motor tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(topSpeed.isEmpty()) {
            Toast.makeText(MotorAddActivity.this, "Kecepatan Tertinggi Motor tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(color.isEmpty()) {
            Toast.makeText(MotorAddActivity.this, "Warna Motor tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(price.isEmpty()) {
            Toast.makeText(MotorAddActivity.this, "Harga Motor tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(year.isEmpty()) {
            Toast.makeText(MotorAddActivity.this, "Tahun Keluar Motor tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(spec.isEmpty()) {
            Toast.makeText(MotorAddActivity.this, "Spesifikasi Motor tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(dp == null) {
            Toast.makeText(MotorAddActivity.this, "Gambar Motor tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        String uid = String.valueOf(System.currentTimeMillis());

        // SIMPAN DATA PERALATAN KAMERA KE DATABASE
        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("nameTemp", name.toLowerCase());
        product.put("model", model);
        product.put("topSpeed", topSpeed);
        product.put("price", price);
        product.put("color", color);
        product.put("year", year);
        product.put("spec", spec);
        product.put("dp", dp);
        product.put("motorId", uid);


        FirebaseFirestore
                .getInstance()
                .collection("motor")
                .document(uid)
                .set(product)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        showSuccessDialog();
                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        showFailureDialog();
                    }
                });

    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Mengunggah Motor")
                .setMessage("Terdapat kesalahan ketika mengunggah motor, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Mengunggah Motor")
                .setMessage("Motor akan segera terbit, anda dapat mengedit atau menghapus kamera jika terdapat kesalahan")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY) {
                uploadArticleDp(data.getData());
            }
        }
    }

    private void uploadArticleDp(Uri data) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "motor/data_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    dp = uri.toString();
                                    binding.imageHint.setVisibility(View.GONE);
                                    Glide
                                            .with(this)
                                            .load(dp)
                                            .into(binding.ArticleDp);
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(MotorAddActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    Log.d("imageDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(MotorAddActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                    Log.d("imageDp: ", e.toString());
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}