package com.example.cafemanagementservices.model;

public class User {
    public String uid;
    public String tenDangNhap;
    public String hoTen;
    public String email;
    public String vaiTro;
    public String matKhau;

    public User() {
    }

    public User(String uid, String tenDangNhap, String hoTen,
                String email, String vaiTro, String matKhau) {
        this.uid = uid;
        this.tenDangNhap = tenDangNhap;
        this.hoTen = hoTen;
        this.email = email;
        this.vaiTro = vaiTro;
        this.matKhau = matKhau;
    }
}