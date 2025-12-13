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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<OrderItem> items;
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");

    public CartAdapter(List<OrderItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OrderItem it = items.get(position);
        holder.tvTenMon.setText(it.tenMon);
        holder.tvSoLuong.setText("x" + it.soLuong);
        holder.tvThanhTien.setText(fmt.format(it.thanhTien));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenMon, tvSoLuong, tvThanhTien;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMonCart);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            tvThanhTien = itemView.findViewById(R.id.tvThanhTien);
        }
    }
}