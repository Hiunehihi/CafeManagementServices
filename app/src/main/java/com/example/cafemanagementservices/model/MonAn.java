package com.example.cafemanagementservices.model;

public class MonAn {
    public String id;
    public String tenMon;
    public String moTa;
    public long gia;
    public String hinhAnhUrl;
    public String loai;

    public MonAn() {
    }

    public MonAn(String id, String tenMon, String moTa,
                 long gia, String hinhAnhUrl, String loai) {
        this.id = id;
        this.tenMon = tenMon;
        this.moTa = moTa;
        this.gia = gia;
        this.hinhAnhUrl = hinhAnhUrl;
        this.loai = loai;
    }
}