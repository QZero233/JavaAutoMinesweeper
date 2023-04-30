package com.qzero.mine;

public class ImageProfile {

    private int xOffset;
    private int yOffset;
    private int xNum;
    private int yNum;
    private int xSize;
    private int ySize;
    private String imagePath;

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

    public ImageProfile(int xOffset, int yOffset, int xNum, int yNum, int xSize, int ySize,String imagePath) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xNum = xNum;
        this.yNum = yNum;
        this.xSize = xSize;
        this.ySize = ySize;
        this.imagePath=imagePath;
    }

    public ImageProfile(int xOffset, int yOffset, int rowNum, int blockSize,String imagePath) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xNum = rowNum;
        this.yNum = rowNum;
        this.xSize = blockSize;
        this.ySize = blockSize;
        this.imagePath=imagePath;
    }

    public int getXOffset() {
        return xOffset;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public int getXNum() {
        return xNum;
    }

    public void setXNum(int xNum) {
        this.xNum = xNum;
    }

    public int getYNum() {
        return yNum;
    }

    public void setYNum(int yNum) {
        this.yNum = yNum;
    }

    public int getXSize() {
        return xSize;
    }

    public void setXSize(int xSize) {
        this.xSize = xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ImageProfile{" +
                "xOffset=" + xOffset +
                ", yOffset=" + yOffset +
                ", xNum=" + xNum +
                ", yNum=" + yNum +
                ", xSize=" + xSize +
                ", ySize=" + ySize +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
