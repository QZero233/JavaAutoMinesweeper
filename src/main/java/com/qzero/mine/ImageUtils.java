package com.qzero.mine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageUtils {

    public static final ImageProfile PROFILE_9X9=new ImageProfile(745,259,9,119,"img_samples/9x9/");
    public static final ImageProfile PROFILE_16X16=new ImageProfile(674,189,16,76,"img_samples/16x16/");
    public static final ImageProfile PROFILE_16X16_BY_ROBOT=new ImageProfile(448,125,16,51,"img_samples/16x16_robot/");

    public static final ImageProfile PROFILE_24X24_BY_ROBOT=new ImageProfile(424,102,24,36,"img_samples/24x24_robot/");

    public static final ImageProfile PROFILE_16X30_BY_ROBOT=new ImageProfile(96,126,30,16,50,51,"16x30_robot");

    private static Map<ImageProfile.ImageType, List<Matrix>> cacheMap=new HashMap<>();

    public static void loadSamples(ImageProfile profile) throws Exception{
        File[] dirs=new File(profile.getImagePath()).listFiles();
        if(dirs.length==0){
            return;
        }

        for(File dir:dirs){
            File[] files=dir.listFiles();

            List<Matrix> bufferedImageList=new ArrayList<>();
            for(File file:files){
                BufferedImage image=ImageIO.read(file);

                Matrix matrix=new Matrix(image.getHeight(),image.getWidth());
                for(int x=0;x<image.getWidth();x++){
                    for(int y=0;y<image.getHeight();y++){
                        matrix.set(x,y,getGrayAvg(image.getRGB(x,y)));
                    }
                }

                bufferedImageList.add(matrix);
            }

            ImageProfile.ImageType type=null;
            switch (dir.getName()){
                case "1":
                    type=ImageProfile.ImageType.ONE;
                    break;
                case "2":
                    type=ImageProfile.ImageType.TWO;
                    break;
                case "3":
                    type=ImageProfile.ImageType.THREE;
                    break;
                case "4":
                    type=ImageProfile.ImageType.FOUR;
                    break;
                case "5":
                    type=ImageProfile.ImageType.FIVE;
                    break;
                case "cover":
                    type=ImageProfile.ImageType.COVERED;
                    break;
                case "flag":
                    type=ImageProfile.ImageType.FLAGGED;
                    break;
                case "blank":
                    type=ImageProfile.ImageType.BLANK;
                    break;
            }

            cacheMap.put(type,bufferedImageList);
        }
    }

    public static void segmentImage(BufferedImage game,ImageProfile profile) throws Exception{
        int xSize= profile.getXSize();
        int ySize= profile.getYSize();
        for(int x=0;x<profile.getXNum();x++){
            for(int y=0;y<profile.getYNum();y++){
                BufferedImage newImage=new BufferedImage(xSize,ySize,BufferedImage.TYPE_INT_ARGB);

                for(int i=0;i<xSize;i++){
                    for(int j=0;j<ySize;j++){
                        int color=game.getRGB(profile.getXOffset()+x*xSize+i,profile.getYOffset()+y*ySize+j);
                        newImage.setRGB(i,j,color);
                    }
                }
                ImageIO.write(newImage,"png",new File("segment/"+x+"-"+y+".png"));
            }
        }
    }

    public static void scaleOriginSamples(int newSize) throws Exception{
        File[] typeDirs=new File("image_samples_origin/").listFiles();
        for(File dir:typeDirs){
            if(!dir.isDirectory())
                continue;

            File[] files=dir.listFiles();
            for(File file:files){
                BufferedImage newImage=new BufferedImage(newSize,newSize,BufferedImage.TYPE_INT_RGB);
                Graphics graphics=newImage.getGraphics();

                Image src= ImageIO.read(file);

                graphics.drawImage(src,0,0,newSize,newSize,null);
                new File("image_samples/"+dir.getName()+"/").mkdirs();
                ImageIO.write(newImage,"png",new File("image_samples/"+dir.getName()+"/"+file.getName()));
            }
        }
    }

    public static int[][] getGameMatrix(BufferedImage game,ImageProfile profile) throws Exception{
        Matrix gameImage=new Matrix(game.getHeight(),game.getWidth());
        for(int x=0;x<game.getWidth();x++){
            for(int y=0;y<game.getHeight();y++){
                gameImage.set(x,y,getGrayAvg(game.getRGB(x,y)));
            }
        }

        int xNum= profile.getXNum();
        int yNum= profile.getYNum();
        int[][] result=new int[xNum][yNum];
        for(int x=0;x<xNum;x++){
            for(int y=0;y<yNum;y++){
                ImageProfile.ImageType type=getType(gameImage,profile,x,y);
                result[x][y]=type.value;
            }
        }

        return result;
    }

    public static ImageProfile.ImageType getType(Matrix game,ImageProfile profile,int x,int y) throws Exception{
        //Check size

        double bestLikelihood=1e10;
        ImageProfile.ImageType bestType =null;

        for(ImageProfile.ImageType type:cacheMap.keySet()){
            List<Matrix> imageList=cacheMap.get(type);
            for(Matrix image:imageList){
                double likelihood=getLikelihood(game,image,profile,x,y);
                if(likelihood < bestLikelihood){
                    bestLikelihood=likelihood;
                    bestType=type;
                }
            }
        }

        return bestType;
    }

    public static double getLikelihood(Matrix game,Matrix target,ImageProfile profile,int x,int y){
        return getLikelihood(game,target,x,y,profile.getXSize(),profile.getYSize()
                ,profile.getXOffset(),profile.getYOffset());
    }

    private static int getGrayAvg(int color){
        int red=(color & 0xff0000) >> 16;
        int green=(color & 0xff00) >> 8;
        int blue=color & 0x0000ff;
        return Math.round((red * 0.299f + green * 0.587f + blue * 0.114f));
    }

    public static double getLikelihood(Matrix game,Matrix target,int x,int y,int xSize,int ySize,int xOffset,int yOffset){
        double currentSum=0;

        //Only compare central parts 1/4 to 3/4
        for(int k=target.getCol()/8;k<target.getCol()*7/8;k++){
            for(int l=target.getRow()/8;l<target.getRow()*7/8;l++){
                //Origin x: x*blockSize + k
                //Origin y: y*blockSize + l
                //Target x: k
                //Target y: l

                int srcColor=game.get(x*xSize + k + xOffset,y*ySize + l + yOffset);
                int targetColor=target.get(k,l);

                int abs=Math.abs(srcColor-targetColor);
                currentSum+=abs;
            }
        }

        double averageSum=currentSum/(target.getCol()*target.getRow());

        return averageSum;
    }

    public static Coordinate getCoordinate(ImageProfile profile,int x,int y){
        return new Coordinate(x*profile.getXSize()+profile.getXOffset()+profile.getXSize()/2,
                y*profile.getYSize()+profile.getYOffset()+profile.getYSize()/2);
    }

}
