package com.example.cafemanagementservices.model;

import java.util.Map;

public class DonHang {
    public String id;
    public String userId;
    public String tenKhachHang;
    public String banId;
    public String tenBan;
    public String thoiGian;   // "yyyy-MM-dd HH:mm"
    public String trangThai;  // ChoXuLy / HoanTat / DaHuy ...
    public long tongTien;
    public Map<String, OrderItem> danhSachMon;
    public String phuongThucThanhToan;
    public Map<String, ChiTietMon> items;
    public DonHang() {}
}