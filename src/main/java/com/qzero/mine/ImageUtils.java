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

    public static final ImageProfile PROFILE_9X9=new ImageProfile(745,259,9,119);
    public static final ImageProfile PROFILE_16X16=new ImageProfile(674,189,16,76);
    public static final ImageProfile PROFILE_16X16_BY_ROBOT=new ImageProfile(448,125,16,51);

    public static final ImageProfile PROFILE_24X24_BY_ROBOT=new ImageProfile(423,101,24,38);

    private static Map<ImageProfile.ImageType, List<BufferedImage>> cacheMap=new HashMap<>();

    public static void loadSamples(ImageProfile profile) throws Exception{
        scaleOriginSamples(profile.getBlockSize());

        File[] dirs=new File("image_samples/").listFiles();
        if(dirs.length==0){
            return;
        }

        for(File dir:dirs){
            File[] files=dir.listFiles();

            List<BufferedImage> bufferedImageList=new ArrayList<>();
            for(File file:files){
                BufferedImage image=ImageIO.read(file);
                bufferedImageList.add(image);
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
        int blockSize=profile.getBlockSize();
        for(int x=0;x<profile.getRowNum();x++){
            for(int y=0;y<profile.getRowNum();y++){
                BufferedImage newImage=new BufferedImage(blockSize,blockSize,BufferedImage.TYPE_INT_ARGB);

                for(int i=0;i<blockSize;i++){
                    for(int j=0;j<blockSize;j++){
                        int color=game.getRGB(profile.getxOffset()+x*blockSize+i,profile.getyOffset()+y*blockSize+j);
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
        int rowNum= profile.getRowNum();
        int[][] result=new int[rowNum][rowNum];
        for(int x=0;x<rowNum;x++){
            for(int y=0;y<rowNum;y++){
                ImageProfile.ImageType type=getType(game,profile,x,y);
                result[x][y]=type.value;
            }
        }

        return result;
    }

    public static ImageProfile.ImageType getType(BufferedImage game,ImageProfile profile,int x,int y) throws Exception{
        //Check size

        double bestLikelihood=1e10;
        ImageProfile.ImageType bestType =null;

        for(ImageProfile.ImageType type:cacheMap.keySet()){
            List<BufferedImage> imageList=cacheMap.get(type);
            for(BufferedImage image:imageList){
                double likelihood=getLikelihood(game,image,profile,x,y);
                if(likelihood < bestLikelihood){
                    bestLikelihood=likelihood;
                    bestType=type;
                }
            }
        }

        return bestType;
    }

    public static double getLikelihood(BufferedImage game,BufferedImage target,ImageProfile profile,int x,int y){
        return getLikelihood(game,target,x,y,profile.getBlockSize(),profile.getxOffset(),profile.getyOffset());
    }

    private static int getGrayAvg(int color){
        int red=(color & 0xff0000) >> 16;
        int green=(color & 0xff00) >> 8;
        int blue=color & 0x0000ff;
        return Math.round((red * 0.299f + green * 0.587f + blue * 0.114f));
    }

    public static double getLikelihood(BufferedImage game,BufferedImage target,int x,int y,int blockSize,int xOffset,int yOffset){
        double currentSum=0;

        //Only compare central parts 1/4 to 3/4
        for(int k=target.getWidth()/4;k<target.getWidth()*3/4;k++){
            for(int l=target.getHeight()/4;l<target.getHeight()*3/4;l++){
                //Origin x: x*blockSize + k
                //Origin y: y*blockSize + l
                //Target x: k
                //Target y: l

                int srcColor=game.getRGB(x*blockSize + k + xOffset,y*blockSize + l + yOffset);
                int targetColor=target.getRGB(k,l);

                //Get gray average
                srcColor=getGrayAvg(srcColor);
                targetColor=getGrayAvg(targetColor);


                int abs=Math.abs(srcColor-targetColor);
                currentSum+=abs;
            }
        }

        double averageSum=currentSum/(target.getWidth()*target.getHeight());

        return averageSum;
    }

    public static Coordinate getCoordinate(ImageProfile profile,int x,int y){
        int epsilon=profile.getBlockSize()/2;
        return new Coordinate(x*profile.getBlockSize()+profile.getxOffset()+epsilon,y*profile.getBlockSize()+profile.getyOffset()+epsilon);
    }

}
