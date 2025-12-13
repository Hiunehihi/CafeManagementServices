package com.example.cafemanagementservices.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cafemanagementservices.R;
import com.example.cafemanagementservices.model.MonAn;

import java.text.DecimalFormat;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    public interface OnMenuClickListener {
        void onMenuClick(MonAn mon);
    }

    private final List<MonAn> items;
    private final OnMenuClickListener listener;
    private final DecimalFormat fmt = new DecimalFormat("#,### đ");

    public MenuAdapter(List<MonAn> items, OnMenuClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MonAn m = items.get(position);
        holder.bind(m, listener, fmt);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMon;
        TextView tvTenMon, tvGiaMon;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMon = itemView.findViewById(R.id.imgMon);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvGiaMon = itemView.findViewById(R.id.tvGiaMon);
        }

        void bind(MonAn m, OnMenuClickListener listener, DecimalFormat fmt) {
            tvTenMon.setText(m.tenMon);
            tvGiaMon.setText(fmt.format(m.gia));

            Glide.with(itemView.getContext())
                    .load(m.hinhAnhUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
                    .into(imgMon);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onMenuClick(m);
            });
        }
    }
}