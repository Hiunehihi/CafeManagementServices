package com.example.cafemanagementservices.model;

public class OrderItem {
    public String monId;
    public String tenMon;
    public long gia;
    public int soLuong;
    public long thanhTien;

    public OrderItem() {}
    public OrderItem(String monId, String tenMon, long gia, int soLuong) {
        this.monId = monId;
        this.tenMon = tenMon;
        this.gia = gia;
        this.soLuong = soLuong;
        this.thanhTien = gia * soLuong;
    }
}