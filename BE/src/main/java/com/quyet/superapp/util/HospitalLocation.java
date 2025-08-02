package com.quyet.superapp.util;

public class HospitalLocation {
    public static final double LATITUDE = 10.870004;
    public static final double LONGITUDE = 106.803267;

    public static final String NAME = "Bệnh viện FPTU";
    public static final String ADDRESS = "Số 1 Võ Văn Ngân, Phường Linh Chiểu, TP. Thủ Đức, TP. Hồ Chí Minh";

    // Có thể dùng để hiển thị ở frontend hoặc log
    public static String getFullLocationDescription() {
        return NAME + " - " + ADDRESS + " (" + LATITUDE + ", " + LONGITUDE + ")";
    }
}
