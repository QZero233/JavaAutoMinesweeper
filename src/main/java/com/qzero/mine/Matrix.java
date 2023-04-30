package com.qzero.mine;

public class Matrix {

    private int[] arr;
    private int row;
    private int col;

    public Matrix(int row, int col) {
        this.row = row;
        this.col = col;
        arr=new int[row*col];
    }

    private int idx(int x, int y){
        return y*col+x;
    }

    public void set(int x,int y,int val){
        arr[idx(x,y)]=val;
    }

    public int get(int x,int y){
        return arr[idx(x,y)];
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
