package com.myktmmotor.myktmmotor.ui.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myktmmotor.myktmmotor.R;
import com.myktmmotor.myktmmotor.databinding.ActivityServiceAddBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServiceAddActivity extends AppCompatActivity {

    private ActivityServiceAddBinding binding;
    private String dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateTime();
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });


    }

    private void validateForm() {
        String name = binding.nameEt.getText().toString().trim();
        String address = binding.addressEt.getText().toString().trim();
        String merk = binding.merk.getText().toString().trim();
        String phone = binding.phoneEt.getText().toString().trim();
        String kendala = binding.kendala.getText().toString().trim();

        if(name.isEmpty()) {
            Toast.makeText(ServiceAddActivity.this, "Nama anda tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if(address.isEmpty()) {
            Toast.makeText(ServiceAddActivity.this, "Alamat anda tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }  else if(merk.isEmpty()) {
            Toast.makeText(ServiceAddActivity.this, "Merk Motor anda tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }  else if(phone.isEmpty()) {
            Toast.makeText(ServiceAddActivity.this, "No.Telepon anda tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }  else if(kendala.isEmpty()) {
            Toast.makeText(ServiceAddActivity.this, "Kendala Motor anda tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }  else if(dateTime == null) {
            Toast.makeText(ServiceAddActivity.this, "Tanggal Pengajuan Service  tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        String timeInMillis = String.valueOf(System.currentTimeMillis());
        String uid = FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .getUid();

        Map<String, Object> service = new HashMap<>();
        service.put("name", name);
        service.put("address", address);
        service.put("dateTime", dateTime);
        service.put("merk", merk);
        service.put("phone", phone);
        service.put("kendala", kendala);
        service.put("serviceId", timeInMillis);
        service.put("status", "Belum Disetujui");
        service.put("serviceDate", "");
        service.put("uid", uid);

        FirebaseFirestore
                .getInstance()
                .collection("service")
                .document(timeInMillis)
                .set(service)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            showSuccessDialog();
                        } else  {
                            mProgressDialog.dismiss();
                            showFailureDialog();
                        }
                    }
                });

    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Megajukan Service Motor")
                .setMessage("Terdapat kesalahan ketika mengajukan service motor, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Mengajukan Service Motor")
                .setMessage("Selamat, anda berhasil mengajukan service motor\n\nSilahkan klik OKE untuk login!")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    private void getDateTime() {
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build())
                .setTitleText("Jadwal Pengajuan Service").build();
        datePicker.show(getSupportFragmentManager(), datePicker.toString());
        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            dateTime = sdf.format(new Date(Long.parseLong(selection.toString())));
            binding.dateTime.setText(dateTime);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}