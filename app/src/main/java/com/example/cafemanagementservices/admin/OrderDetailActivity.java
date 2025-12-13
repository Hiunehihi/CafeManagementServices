package com.example.cafemanagementservices.admin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafemanagementservices.R;
import com.example.cafemanagementservices.adapter.OrderItemAdapter;
import com.example.cafemanagementservices.firebase.FirebaseService;
import com.example.cafemanagementservices.model.DonHang;
import com.example.cafemanagementservices.model.OrderItem;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderInfo, tvOrderTime, tvOrderStatus, tvOrderTotal;
    private RecyclerView rvOrderItems;
    private MaterialButton btnConfirm, btnCancel;

    private String orderId;
    private DonHang currentOrder;
    private final List<OrderItem> itemList = new ArrayList<>();
    private OrderItemAdapter adapter;
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderId = getIntent().getStringExtra("orderId");
        if (orderId == null || orderId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy mã đơn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupRecycler();
        loadOrderDetail();

        btnConfirm.setOnClickListener(v -> updateStatus("HoanTat"));
        btnCancel.setOnClickListener(v -> updateStatus("DaHuy"));
    }

    private void initViews() {
        tvOrderId = findViewById(R.id.tvOrderIdDetail);
        tvOrderInfo = findViewById(R.id.tvOrderInfoDetail);
        tvOrderTime = findViewById(R.id.tvOrderTimeDetail);
        tvOrderStatus = findViewById(R.id.tvOrderStatusDetail);
        tvOrderTotal = findViewById(R.id.tvOrderTotalDetail);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        btnConfirm = findViewById(R.id.btnConfirmOrder);
        btnCancel = findViewById(R.id.btnCancelOrder);
    }

    private void setupRecycler() {
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderItemAdapter(itemList);
        rvOrderItems.setAdapter(adapter);
    }

    private void loadOrderDetail() {
        FirebaseService.getDonHangRef()
                .child(orderId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        currentOrder = snapshot.getValue(DonHang.class);
                        if (currentOrder == null) {
                            Toast.makeText(OrderDetailActivity.this,
                                    "Đơn hàng không tồn tại", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }

                        currentOrder.id = snapshot.getKey();
                        bindOrderToViews();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OrderDetailActivity.this,
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void bindOrderToViews() {
        tvOrderId.setText("Mã đơn: " + currentOrder.id);
        tvOrderInfo.setText("Bàn " + currentOrder.tenBan + " - " + currentOrder.tenKhachHang);
        tvOrderTime.setText(currentOrder.thoiGian);
        tvOrderStatus.setText(currentOrder.trangThai);
        tvOrderTotal.setText("Tổng: " + fmt.format(currentOrder.tongTien));

        // load danh sách món
        itemList.clear();
        if (currentOrder.danhSachMon != null) {
            for (Map.Entry<String, OrderItem> e : currentOrder.danhSachMon.entrySet()) {
                itemList.add(e.getValue());
            }
        }
        adapter.notifyDataSetChanged();

        // enable/disable nút theo trạng thái
        boolean done = "HoanTat".equals(currentOrder.trangThai) ||
                "DaHuy".equals(currentOrder.trangThai);
        btnConfirm.setEnabled(!done);
        btnCancel.setEnabled(!done);
    }

    private void updateStatus(String newStatus) {
        if (currentOrder == null) return;

        FirebaseService.getDonHangRef()
                .child(currentOrder.id)
                .child("trangThai")
                .setValue(newStatus)
                .addOnSuccessListener(unused -> {
                    currentOrder.trangThai = newStatus;
                    tvOrderStatus.setText(newStatus);
                    bindOrderToViews();
                    Toast.makeText(this, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}