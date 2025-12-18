package com.example.cafemanagementservices.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cafemanagementservices.R;
import com.example.cafemanagementservices.admin.AdminDashboardActivity;
import com.example.cafemanagementservices.customer.CustomerHomeActivity;
import com.example.cafemanagementservices.firebase.FirebaseService;
import com.example.cafemanagementservices.model.User;
import com.example.cafemanagementservices.util.HashUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edtUsername, edtPassword;
    private MaterialButton btnLogin;
    private TextView tvToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupActions();

        String prefillUser = getIntent().getStringExtra("prefill_username");
        if (prefillUser != null && !prefillUser.isEmpty()) {
            edtUsername.setText(prefillUser);
        }
    }

    private void initViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        tvToRegister= findViewById(R.id.tvToRegister);
    }

    private void setupActions() {
        tvToRegister.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String username = edtUsername.getText() != null ? edtUsername.getText().toString().trim() : "";
        String password = edtPassword.getText() != null ? edtPassword.getText().toString().trim() : "";

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Tính toán Hash của mật khẩu nhập vào
        String passwordHash = HashUtils.md5(password);

        btnLogin.setEnabled(false); // Khóa nút để tránh bấm nhiều lần

        FirebaseService.getTaiKhoanRef()
                .orderByChild("tenDangNhap")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Nếu không tìm thấy user
                        if (!snapshot.hasChildren()) {
                            btnLogin.setEnabled(true); // Mở lại nút
                            Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (DataSnapshot child : snapshot.getChildren()) {
                            User u = child.getValue(User.class);
                            if (u == null) continue;

                            boolean isMatched = false;

                            // --- LOGIC KIỂM TRA MẬT KHẨU ---
                            if (u.matKhau != null) {
                                // Trường hợp 1: Tài khoản mới (Mật khẩu trong DB là Hash)
                                if (u.matKhau.equals(passwordHash)) {
                                    isMatched = true;
                                }
                                // Trường hợp 2: Tài khoản cũ (Mật khẩu trong DB là chữ thường "123456")
                                else if (u.matKhau.equals(password)) {
                                    isMatched = true;
                                    // (Tùy chọn) Tại đây có thể cập nhật lại mật khẩu thành Hash để bảo mật hơn cho lần sau
                                }
                            }

                            if (isMatched) {
                                btnLogin.setEnabled(true); // Mở lại nút trước khi chuyển trang
                                u.uid = child.getKey();

                                navigateUser(u);
                                return;
                            }
                        }

                        // Chạy hết vòng lặp mà không khớp
                        btnLogin.setEnabled(true); // Mở lại nút
                        Toast.makeText(LoginActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        btnLogin.setEnabled(true); // Mở lại nút
                        Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateUser(User u) {
        Intent i;
        if ("KhachHang".equalsIgnoreCase(u.vaiTro)) {
            i = new Intent(LoginActivity.this, CustomerHomeActivity.class);
        } else {
            // Admin hoặc NhanVien
            i = new Intent(LoginActivity.this, AdminDashboardActivity.class);
        }
        i.putExtra("userId", u.uid);
        i.putExtra("userName", u.hoTen);
        startActivity(i);
        finish();
    }
}