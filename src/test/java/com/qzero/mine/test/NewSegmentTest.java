package com.qzero.mine.test;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewSegmentTest {

    public boolean isBlack(int color){
        int red=(color & 0xff0000) >> 16;
        int green=(color & 0xff00) >> 8;
        int blue=color & 0x0000ff;


        return ((red+green+blue)/3) < 30;
    }

    public void saveSegment(BufferedImage image,int beginX,int beginY,int endX,int endY,String fileName) throws Exception{
        BufferedImage newImage=new BufferedImage(endX-beginX+1,endY-beginY+1,BufferedImage.TYPE_INT_ARGB);
        for(int x=beginX;x<endX;x++){
            for(int y=beginY;y<endY;y++){
                newImage.setRGB(x-beginX,y-beginY,image.getRGB(x,y));
            }
        }

        ImageIO.write(newImage,"png",new File(fileName));
    }

    @Test
    public void testNewSegment() throws Exception{
        int xStart=84;
        int yStart=113;

        int xEnd=1621;
        int yEnd=946;

        List<Integer> xDivides=new ArrayList<>();
        List<Integer> yDivides=new ArrayList<>();

        BufferedImage image= ImageIO.read(new File("test_ss.png"));

        int x=xStart;
        while (x<xEnd){
            //Detect continuous black lines whose length > 50
            boolean found=false;

            boolean continuousBlack=false;
            int beginY=0;

            for(int y=yStart;y<yEnd;y++){
                if(isBlack(image.getRGB(x,y))){
                    if(!continuousBlack){
                        continuousBlack=true;
                        beginY=y;
                    }
                }else{
                    if(continuousBlack){
                        continuousBlack=false;
                        int length=y-beginY;
                        if(length>100){
                            found=true;
                            xDivides.add(x);
                            System.out.println("Found x "+x+", length "+length);
                            break;
                        }
                    }
                }
            }

            if(found)
                x+=5;
            else
                x++;
        }

        int y=yStart;
        while (y<yEnd){
            //Detect continuous black lines whose length > 50
            boolean found=false;

            boolean continuousBlack=false;
            int beginX=0;

            for(x=xStart;x<xEnd;x++){
                if(isBlack(image.getRGB(x,y))){
                    if(!continuousBlack){
                        continuousBlack=true;
                        beginX=x;
                    }
                }else{
                    if(continuousBlack){
                        continuousBlack=false;
                        int length=x-beginX;
                        if(length>100){
                            found=true;
                            yDivides.add(y);
                            System.out.println("Found y "+y+", length "+length);
                            break;
                        }
                    }
                }
            }

            if(found)
                y+=5;
            else
                y++;
        }

        System.out.println(xDivides);
        System.out.println(xDivides.size());

        System.out.println(yDivides);
        System.out.println(yDivides.size());

        for(x=0;x<xDivides.size()-1;x++){
            for(y=0;y<yDivides.size()-1;y++){
                saveSegment(image,xDivides.get(x),yDivides.get(y),xDivides.get(x+1),yDivides.get(y+1),
                        "segment_new/"+x+"_"+y+".png");
            }
        }
    }

}
