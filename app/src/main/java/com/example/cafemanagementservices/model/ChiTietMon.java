package com.example.cafemanagementservices.model;

public class ChiTietMon {
    public String monId;
    public String tenMon;
    public int soLuong;
    public long donGia;
    public long thanhTien;

    public ChiTietMon() {
    }

    public ChiTietMon(String monId, String tenMon, int soLuong, long donGia) {
        this.monId = monId;
        this.tenMon = tenMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = donGia * soLuong;
    }
}
