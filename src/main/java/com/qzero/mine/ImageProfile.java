package com.qzero.mine;

public class ImageProfile {

    private int xOffset;
    private int yOffset;
    private int rowNum;
    private int blockSize;

    public enum ImageType{
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        COVERED(-1),
        FLAGGED(-2),
        BLANK(0);
        int value;

        ImageType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public ImageProfile() {
    }

    public ImageProfile(int xOffset, int yOffset, int rowNum, int blockSize) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.rowNum = rowNum;
        this.blockSize = blockSize;
    }

    public int getxOffset() {
        return xOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    @Override
    public String toString() {
        return "ImageProfile{" +
                "xOffset=" + xOffset +
                ", yOffset=" + yOffset +
                ", rowNum=" + rowNum +
                ", blockSize=" + blockSize +
                '}';
    }
}
