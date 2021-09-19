package com.myktmmotor.myktmmotor.ui.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myktmmotor.myktmmotor.R;
import com.myktmmotor.myktmmotor.databinding.ActivityServiceDetailBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServiceDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SERVICE = "service";
    private ActivityServiceDetailBinding binding;
    private ServiceModel model;
    private FirebaseUser user;
    private String serviceDate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = FirebaseAuth.getInstance().getCurrentUser();

        model = getIntent().getParcelableExtra(EXTRA_SERVICE);
        binding.nameEt.setText(model.getName());
        binding.addressEt.setText(model.getAddress());
        binding.dateTime.setText(model.getDateTime());
        binding.merk.setText(model.getMerk());
        binding.phoneEt.setText(model.getPhone());
        binding.kendala.setText(model.getKendala());

        if(model.getStatus().equals("Belum Disetujui")) {
            binding.textView6.setText("Status: Belum Disetujui");

        } else if(model.getStatus().equals("Sudah Disetujui")) {
            binding.textView6.setText("Akan di service pada tanggal: " + model.getServiceDate());
        } else {
            binding.textView6.setText("Status: Selesai");
        }


        getRole();

        binding.acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getStatus().equals("Belum Disetujui")) {
                    acceptService();
                } else if (model.getStatus().equals("Sudah Disetujui")) {
                    finishService();
                }
            }
        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteService();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void deleteService() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Menghapus Service")
                .setMessage("Apakah anda yakin ingin menghapus data service kendaraan ini ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {

                    // delete service
                    ProgressDialog mProgressDialog = new ProgressDialog(this);

                    mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();


                    FirebaseFirestore
                            .getInstance()
                            .collection("service")
                            .document(model.getServiceId())
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        mProgressDialog.dismiss();
                                        showSuccessDeleteServiceDialog();
                                    } else {
                                        mProgressDialog.dismiss();
                                        showFailureDeleteServiceDialog();
                                    }
                                }
                            });
                })
                .setNegativeButton("TIDAK", (dialog, i) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void showSuccessDeleteServiceDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Menghapus Service")
                .setMessage("Selamat, anda berhasil menghapus data service motor ini")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    private void showFailureDeleteServiceDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Menghapus Service")
                .setMessage("Terdapat kesalahan ketika ingin menghapus service motor ini, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void finishService() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Menyelesaikan Service")
                .setMessage("Apakah anda yakin teknisi sudah memperbaiki motor dari pengguna ini ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {

                    // finish service
                    ProgressDialog mProgressDialog = new ProgressDialog(this);

                    mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();

                    FirebaseFirestore
                            .getInstance()
                            .collection("service")
                            .document(model.getServiceId())
                            .update("status", "Selesai")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        mProgressDialog.dismiss();
                                        showSuccessFinishServiceDialog();
                                        binding.textView6.setText("Status: Selesai");
                                        binding.acc.setVisibility(View.GONE);
                                        binding.delete.setVisibility(View.VISIBLE);
                                    } else {
                                        mProgressDialog.dismiss();
                                        showFailureFinishServiceDialog();
                                    }
                                }
                            });
                })
                .setNegativeButton("TIDAK", (dialog, i) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void showSuccessFinishServiceDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Mengakhiri Service")
                .setMessage("Selamat, anda berhasil mengakhiri service motor ini")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void showFailureFinishServiceDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Mengahiri Service")
                .setMessage("Terdapat kesalahan ketika ingin mengakhiri service motor ini, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    @SuppressLint("SetTextI18n")
    private void acceptService() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Menerima Service")
                .setMessage("Apakah anda yakin ingin menerima service motor dari pengguna ini ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    // acc service
                    MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                            .setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build())
                            .setTitleText("Konfirmasi Waktu Service").build();
                    datePicker.show(getSupportFragmentManager(), datePicker.toString());
                    datePicker.addOnPositiveButtonClickListener(selection -> {

                        ProgressDialog mProgressDialog = new ProgressDialog(this);

                        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.show();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                        serviceDate = sdf.format(new Date(Long.parseLong(selection.toString())));

                        // save to database

                        Map<String, Object> service = new HashMap<>();
                        service.put("status", "Sudah Disetujui");
                        service.put("serviceDate", serviceDate);

                        FirebaseFirestore
                                .getInstance()
                                .collection("service")
                                .document(model.getServiceId())
                                .update(service)
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful()) {
                                        mProgressDialog.dismiss();
                                        showSuccessAccServiceDialog();
                                        binding.delete.setVisibility(View.GONE);
                                        binding.textView6.setText("Akan di service pada tanggal: " + serviceDate);
                                    } else {
                                        mProgressDialog.dismiss();
                                        showFailureAccServiceDialog();
                                    }
                                });
                    });
                })
                .setNegativeButton("TIDAK", (dialog, i) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void showSuccessAccServiceDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Menerima Service")
                .setMessage("Selamat, anda berhasil menerima service motor ini, pastikan teknisi datang sesuai tanggal yang telah ditentukan")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void showFailureAccServiceDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Menerima Service")
                .setMessage("Terdapat kesalahan ketika ingin menerima service motor ini, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
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
                          if(model.getStatus().equals("Belum Disetujui")) {
                              binding.acc.setVisibility(View.VISIBLE);
                              binding.delete.setVisibility(View.VISIBLE);
                          } else if (model.getStatus().equals("Sudah Disetujui")) {
                              binding.delete.setVisibility(View.GONE);
                              binding.acc.setVisibility(View.VISIBLE);
                          } else  {
                              binding.acc.setVisibility(View.GONE);
                              binding.delete.setVisibility(View.VISIBLE);
                          }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}