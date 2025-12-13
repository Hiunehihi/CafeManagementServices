package com.example.cafemanagementservices.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cafemanagementservices.R;
import com.example.cafemanagementservices.model.DonHang;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    public interface OnOrderClickListener {
        void onOrderClick(DonHang order);
    }

    private final List<DonHang> orders;
    private final OnOrderClickListener listener;
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");

    public OrderAdapter(List<DonHang> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail_item, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        DonHang d = orders.get(position);
        holder.bind(d, listener, fmt);
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderInfo, tvOrderTime, tvOrderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderInfo = itemView.findViewById(R.id.tvOrderInfo);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
        }

        public void bind(DonHang d, OnOrderClickListener listener, DecimalFormat fmt) {
            tvOrderId.setText("Mã đơn: " + d.id);
            tvOrderInfo.setText("Bàn " + d.tenBan + " - " + d.tenKhachHang +
                    " (" + fmt.format(d.tongTien) + ")");
            tvOrderTime.setText(d.thoiGian);
            tvOrderStatus.setText(d.trangThai);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onOrderClick(d);
            });
        }
    }
}