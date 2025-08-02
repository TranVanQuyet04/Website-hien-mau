package com.quyet.superapp.enums;

public enum BloodBagSize {
    SIZE_50(50),
    SIZE_100(100),
    SIZE_150(150),
    SIZE_250(250),
    SIZE_350(350),
    SIZE_450(450);

    private final int volumeMl;

    BloodBagSize(int volumeMl) {
        this.volumeMl = volumeMl;
    }

    public int getVolumeMl() {
        return volumeMl;
    }
}
