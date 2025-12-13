package com.example.cafemanagementservices.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseService {
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseDatabase db = FirebaseDatabase.getInstance("https://project-android-10c7d-default-rtdb.asia-southeast1.firebasedatabase.app/");

    public static FirebaseAuth getAuth() {
        return auth;
    }

    public static DatabaseReference getTaiKhoanRef() {
        return db.getReference("TaiKhoan");
    }

    public static DatabaseReference getMonAnRef() {
        return db.getReference("MonAn");
    }

    public static DatabaseReference getBanRef() {
        return db.getReference("Ban");
    }

    public static DatabaseReference getDonHangRef() {
        return db.getReference("DonHang");
    }
}