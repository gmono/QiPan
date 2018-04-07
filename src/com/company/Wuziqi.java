package com.company;

public class Wuziqi extends QiPan {
    public Wuziqi(int rows, int cols) {
        super(rows, cols);
    }

    @Override
    protected void scanWinners() {
        return;
    }

    @Override
    public boolean canPut(int r, int c, int playerid) {
        if(matrix[r][c]==Empty) return true;
        return false;
    }
}
