package com.example.cafemanagementservices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafemanagementservices.model.MonAn;

import java.text.DecimalFormat;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(MonAn monAn);
    }

    private final List<MonAn> items;
    private final OnItemClickListener listener;
    private final DecimalFormat moneyFormat = new DecimalFormat("#,### đ");

    public MenuAdapter(List<MonAn> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mon_an, parent, false);
        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MonAn m = items.get(position);
        holder.bind(m, listener, moneyFormat);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMon;
        TextView tvTenMon, tvGia;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMon = itemView.findViewById(R.id.imgMon);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvGia = itemView.findViewById(R.id.tvGia);
        }

        public void bind(MonAn m, OnItemClickListener listener, DecimalFormat fmt) {
            tvTenMon.setText(m.tenMon != null ? m.tenMon : "");
            tvGia.setText(fmt.format(m.gia));

            // TODO: nếu có imageUrl -> dùng Glide/Picasso load
            imgMon.setImageResource(R.drawable.ic_coffee_cup);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(m);
            });
        }
    }
}