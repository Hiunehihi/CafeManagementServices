package com.example.cafemanagementservices.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cafemanagementservices.R;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        findViewById(R.id.cardOrders).setOnClickListener(
                v -> startActivity(new Intent(this, OrderListActivity.class)));

        findViewById(R.id.cardMenu).setOnClickListener(
                v -> startActivity(new Intent(this, MenuActivity.class)));

        findViewById(R.id.cardTables).setOnClickListener(
                v -> startActivity(new Intent(this, TableManageActivity.class)));

        findViewById(R.id.cardReport).setOnClickListener(
                v -> startActivity(new Intent(this, RevenueReportActivity.class)));
    }
}