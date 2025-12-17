package com.example.cafemanagementservices.admin;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafemanagementservices.R;
import com.example.cafemanagementservices.firebase.FirebaseService;
import com.example.cafemanagementservices.model.Ban;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TableManageActivity extends AppCompatActivity {

    private RecyclerView rvTables;
    private FloatingActionButton fabAdd;
    private final List<Ban> banList = new ArrayList<>();
    private TableManageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_manage);

        rvTables = findViewById(R.id.rvTableManage);
        fabAdd = findViewById(R.id.fabAddTable);

        // Dùng Grid 2 cột cho đẹp
        rvTables.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new TableManageAdapter(banList);
        rvTables.setAdapter(adapter);

        // Sự kiện: Click thường -> Sửa thông tin
        adapter.setOnItemClickListener(this::showEditDialog);

        // Sự kiện: Click giữ -> Xóa bàn
        adapter.setOnItemLongClickListener(this::showDeleteDialog);

        fabAdd.setOnClickListener(v -> showAddDialog());

        loadTables();
    }

    private void loadTables() {
        FirebaseService.getBanRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        banList.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Ban b = child.getValue(Ban.class);
                            if (b != null) {
                                b.id = child.getKey();
                                banList.add(b);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TableManageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // --- CHỨC NĂNG THÊM BÀN ---
    private void showAddDialog() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (20 * getResources().getDisplayMetrics().density);
        container.setPadding(pad, pad, pad, pad);

        EditText edtTen = new EditText(this);
        edtTen.setHint("Tên bàn (vd: Bàn A1)");
        container.addView(edtTen);

        EditText edtKhuVuc = new EditText(this);
        edtKhuVuc.setHint("Khu vực (vd: Tầng 1)");
        container.addView(edtKhuVuc);

        new AlertDialog.Builder(this)
                .setTitle("Thêm bàn mới")
                .setView(container)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String ten = edtTen.getText().toString().trim();
                    String khuVuc = edtKhuVuc.getText().toString().trim();
                    if (ten.isEmpty() || khuVuc.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String id = FirebaseService.getBanRef().push().getKey();
                    if (id != null) {
                        Ban b = new Ban();
                        b.id = id; // Lưu luôn ID vào object
                        b.tenBan = ten;
                        b.khuVuc = khuVuc;
                        b.trangThai = "Trong"; // Mặc định là Trống

                        FirebaseService.getBanRef().child(id).setValue(b);
                        Toast.makeText(this, "Đã thêm bàn", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // --- CHỨC NĂNG SỬA BÀN ---
    private void showEditDialog(Ban b) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (20 * getResources().getDisplayMetrics().density);
        container.setPadding(pad, pad, pad, pad);

        // Tên bàn
        EditText edtTen = new EditText(this);
        edtTen.setText(b.tenBan);
        edtTen.setHint("Tên bàn");
        container.addView(edtTen);

        // Khu vực
        EditText edtKhuVuc = new EditText(this);
        edtKhuVuc.setText(b.khuVuc);
        edtKhuVuc.setHint("Khu vực");
        container.addView(edtKhuVuc);

        // Trạng thái (Radio Button)
        TextView tvLabel = new TextView(this);
        tvLabel.setText("Trạng thái:");
        tvLabel.setPadding(0, 20, 0, 10);
        container.addView(tvLabel);

        RadioGroup rgStatus = new RadioGroup(this);
        rgStatus.setOrientation(RadioGroup.HORIZONTAL);

        RadioButton rbTrong = new RadioButton(this);
        rbTrong.setText("Trống");
        rgStatus.addView(rbTrong);

        RadioButton rbCoNguoi = new RadioButton(this);
        rbCoNguoi.setText("Có người");
        rgStatus.addView(rbCoNguoi);

        // Set trạng thái hiện tại
        if ("CoNguoi".equals(b.trangThai)) {
            rbCoNguoi.setChecked(true);
        } else {
            rbTrong.setChecked(true);
        }
        container.addView(rgStatus);

        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa bàn")
                .setView(container)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String ten = edtTen.getText().toString().trim();
                    String khuVuc = edtKhuVuc.getText().toString().trim();

                    if (ten.isEmpty() || khuVuc.isEmpty()) return;

                    String newStatus = rbCoNguoi.isChecked() ? "CoNguoi" : "Trong";

                    // Cập nhật lên Firebase
                    b.tenBan = ten;
                    b.khuVuc = khuVuc;
                    b.trangThai = newStatus;

                    if (b.id != null) {
                        FirebaseService.getBanRef().child(b.id).setValue(b);
                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // --- CHỨC NĂNG XÓA BÀN ---
    private void showDeleteDialog(Ban b) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa bàn")
                .setMessage("Bạn chắc chắn muốn xóa bàn \"" + b.tenBan + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    if (b.id != null) {
                        FirebaseService.getBanRef().child(b.id).removeValue();
                        Toast.makeText(this, "Đã xóa bàn", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ================== ADAPTER ==================
    static class TableManageAdapter extends RecyclerView.Adapter<TableManageAdapter.TableViewHolder> {

        interface OnItemClickListener { void onItemClick(Ban b); }
        interface OnItemLongClickListener { void onItemLongClick(Ban b); }

        private final List<Ban> items;
        private OnItemClickListener clickListener;
        private OnItemLongClickListener longClickListener;

        public TableManageAdapter(List<Ban> items) {
            this.items = items;
        }

        public void setOnItemClickListener(OnItemClickListener l) { this.clickListener = l; }
        public void setOnItemLongClickListener(OnItemLongClickListener l) { this.longClickListener = l; }

        @NonNull
        @Override
        public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ban, parent, false);
            return new TableViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
            Ban b = items.get(position);
            holder.tvTenBan.setText(b.tenBan);
            holder.tvKhuVuc.setText(b.khuVuc);

            // LOGIC MÀU SẮC
            if ("CoNguoi".equals(b.trangThai)) {
                // Màu Đỏ cam: Có người
                holder.cardView.setCardBackgroundColor(Color.parseColor("#FF7043"));
                holder.tvTrangThai.setText("Đang phục vụ");
            } else {
                // Màu Xanh teal: Trống
                holder.cardView.setCardBackgroundColor(Color.parseColor("#26A69A"));
                holder.tvTrangThai.setText("Bàn trống");
            }

            // Click thường -> Sửa
            holder.itemView.setOnClickListener(v -> {
                if (clickListener != null) clickListener.onItemClick(b);
            });

            // Click giữ -> Xóa
            holder.itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) longClickListener.onItemLongClick(b);
                return true;
            });
        }

        @Override
        public int getItemCount() { return items != null ? items.size() : 0; }

        static class TableViewHolder extends RecyclerView.ViewHolder {
            TextView tvTenBan, tvKhuVuc, tvTrangThai;
            CardView cardView;

            public TableViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTenBan = itemView.findViewById(R.id.tvTenBanManage);
                tvKhuVuc = itemView.findViewById(R.id.tvKhuVucBanManage);
                tvTrangThai = itemView.findViewById(R.id.tvTrangThaiBan);
                cardView = itemView.findViewById(R.id.cardBan);
            }
        }
    }
}