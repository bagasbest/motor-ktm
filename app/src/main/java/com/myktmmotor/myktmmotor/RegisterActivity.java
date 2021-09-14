package com.myktmmotor.myktmmotor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myktmmotor.myktmmotor.databinding.ActivityRegisterBinding;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this)
                .load(R.drawable.logo)
                .into(binding.logo);

        binding.backToLogin.setOnClickListener(view -> onBackPressed());

        binding.registerBtn.setOnClickListener(view -> formValidation());

    }

    private void formValidation() {
        String name = binding.nameEt.getText().toString().trim();
        String email = binding.emailEt.getText().toString().trim();
        String password = binding.passwordEt.getText().toString().trim();

        if(name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Nama Lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(RegisterActivity.this, "Email tidak sesuai format", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Kata Sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        registerateUser(name, email, password);

    }

    private void registerateUser(String name, String email, String password) {

        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        saveUserBioToDatabase(mProgressDialog, name, email, password);
                    } else {
                        mProgressDialog.dismiss();
                        showFailureDialog();
                    }
                });
    }

    private void saveUserBioToDatabase(ProgressDialog mProgressDialog, String name, String email, String password) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> register = new HashMap<>();
        register.put("uid", uid);
        register.put("name", name);
        register.put("email", email);
        register.put("password", password);
        register.put("role", "user");

        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .set(register)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        mProgressDialog.dismiss();
                        showSuccessDialog();
                    } else {
                        mProgressDialog.dismiss();
                        showFailureDialog();
                    }
                });
    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal Melakukan Registrasi")
                .setMessage("Terdapat kesalahan ketika melakukan registrasi, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Melakukan Registrasi")
                .setMessage("Selamat, anda berhasil melakukan registerasi\n\nSilahkan klik OKE untuk login!")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}