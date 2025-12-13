package com.example.cafemanagementservices.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafemanagementservices.R;
import com.example.cafemanagementservices.model.OrderItem;

import java.text.DecimalFormat;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ItemViewHolder> {

    private final List<OrderItem> items;
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");

    public OrderItemAdapter(List<OrderItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        OrderItem it = items.get(position);
        holder.tvTenMon.setText(it.tenMon);
        holder.tvSoLuong.setText("x" + it.soLuong);
        holder.tvThanhTien.setText(fmt.format(it.thanhTien));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenMon, tvSoLuong, tvThanhTien;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMonDetail);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuongDetail);
            tvThanhTien = itemView.findViewById(R.id.tvThanhTienDetail);
        }
    }
}