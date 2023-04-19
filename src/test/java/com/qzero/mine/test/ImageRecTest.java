package com.qzero.mine.test;

import com.qzero.mine.ImageUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageRecTest {

    @Test
    public void testGetLikelihood() throws Exception{
        BufferedImage image = ImageIO.read(new File("game9.png"));

        File[] files=new File("s2b/").listFiles();
        String types[]=new String[files.length];
        for(int i=0;i<files.length;i++){
            types[i]=files[i].getName().replace(".png","");
        }

//        String types[]={"1","2","3","4","5","cover","flag","blank","cover_light","cover_dark","blank2","blank3","blank4"};

        //Load images
        BufferedImage images[]=new BufferedImage[types.length];
        for(int i=0;i<types.length;i++){
            BufferedImage image1=ImageIO.read(new File("s2b/"+types[i]+".png"));
            images[i]=image1;
        }

        int xOffset=745;
        int yOffset=259;
        int blockSize=119;

//        int xOffset=674;
//        int yOffset=189;
//        int blockSize=76;

        int[][] game=new int[9][9];

        for(int x=0;x<9;x++) {
            for (int y = 0; y < 9; y++) {
                String best=null;
                double bestLikelihood=1e14;
                for(int i=0;i<types.length;i++){
                    double likelihood= ImageUtils.getLikelihood(image,images[i],x,y,blockSize,xOffset,yOffset);
                    if(likelihood<bestLikelihood){
                        bestLikelihood=likelihood;
                        best=types[i];
                    }
                }

                System.out.println(x+","+y+" : "+best);

                //Export image
                BufferedImage newImage=new BufferedImage(blockSize,blockSize,BufferedImage.TYPE_INT_ARGB);

                for(int i=0;i<blockSize;i++){
                    for(int j=0;j<blockSize;j++){
                        int color=image.getRGB(xOffset+x*blockSize+i,yOffset+y*blockSize+j);
                        newImage.setRGB(i,j,color);
                    }
                }
                ImageIO.write(newImage,"png",new File("samples2/"+x+"-"+y+"-"+best+".png"));

                if(best.startsWith("flag")){
                    game[x][y]=-2;
                }else if(best.startsWith("cover")){
                    game[x][y]=-1;
                }else if(best.startsWith("blank")){
                    game[x][y]=0;
                }else{
                    game[x][y]=Integer.parseInt(best);
                }

            }
        }

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print(game[j][i]+" ");
            }
            System.out.println();
        }

        //Do algo
        int[][] directions={
                {1,1},
                {1,-1},
                {-1,1},
                {-1,-1},
                {1,0},
                {-1,0},
                {0,1},
                {0,-1}
        };
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){

                int current=game[i][j];
                if(current<=0)
                    continue;

                int covered=0;
                int flagged=0;
                int uncovered=0;
                for(int k=0;k<8;k++){
                    int nX=i+directions[k][0];
                    int nY=j+directions[k][1];

                    if(nX < 0 || nX >=9 || nY < 0 || nY >=9)
                        continue;

                    if(game[nX][nY]==-1)
                        covered++;
                    else if(game[nX][nY]==-2)
                        flagged++;
                    else
                        uncovered++;
                }

                if(flagged==current && current!=0){
                    //Can open every neighbour

                    for(int k=0;k<8;k++){
                        int nX=i+directions[k][0];
                        int nY=j+directions[k][1];

                        if(nX < 0 || nX >=9 || nY < 0 || nY >=9)
                            continue;

                        if(game[nX][nY]==-1){
                            System.out.println("Open "+nX+","+nY);
                        }
                    }

                }else if(flagged==current-1 && flagged+covered==current){
                    //Can mark a bomb

                    for(int k=0;k<8;k++){
                        int nX=i+directions[k][0];
                        int nY=j+directions[k][1];

                        if(nX < 0 || nX >=9 || nY < 0 || nY >=9)
                            continue;

                        if(game[nX][nY]==-1){
                            System.out.println("Mark "+nX+","+nY);
                        }
                    }
                }

            }
        }
    }

    @Test
    public void checkCoverAndBlank(){
        File[] files=new File("samples2/").listFiles();
        for(File file:files){
            assert file.getName().contains("cover") || file.getName().contains("blank");
        }
    }

    private int avg(int red, int green, int blue) {
//        int avg = Math.round((red * 0.299f + green * 0.587f + blue * 0.114f));
//        avg &= 0x0000ff;
//        return (avg << 16) + (avg << 8) + (avg);
        int regAvg=(red+green+blue)/3;
        int th=170;
        if(red > th || green > th || blue > th){
            return 0xffffff;
        }else{
            return 0;
        }
    }

    @Test
    public void testScale() throws Exception{
//        int newSize=76;
        int newSize=119;

        File[] files=new File("image_samples_small/").listFiles();
        for(File file:files){
            BufferedImage newImage=new BufferedImage(newSize,newSize,BufferedImage.TYPE_INT_RGB);
            Graphics graphics=newImage.getGraphics();

            Image src=ImageIO.read(new File("image_samples_small/"+file.getName()));

            graphics.drawImage(src,0,0,newSize,newSize,null);
            ImageIO.write(newImage,"png",new File("s2b/"+file.getName()));
        }


    }

    @Test
    public void testExportGame() throws Exception{
        BufferedImage image = ImageIO.read(new File("game8.png"));

//        BufferedImage target=ImageIO.read(new File("image_samples/2.png"));

//        int xOffset=745;
//        int yOffset=259;
//        int blockSize=119;

        int xOffset=674;
        int yOffset=189;
        int blockSize=76;

        for(int x=0;x<16;x++){
            for(int y=0;y<16;y++){
                System.out.println(x+","+y);
                BufferedImage newImage=new BufferedImage(blockSize,blockSize,BufferedImage.TYPE_INT_ARGB);

                for(int i=0;i<blockSize;i++){
                    for(int j=0;j<blockSize;j++){
                        int color=image.getRGB(xOffset+x*blockSize+i,yOffset+y*blockSize+j);
//                        int grayColor = avg((color & 0xff0000) >> 16, (color & 0xff00) >> 8, color & 0x0000ff);
                        newImage.setRGB(i,j,color);
                    }
                }
                ImageIO.write(newImage,"png",new File("samples/"+x+"-"+y+".png"));

//                if(x!=1 || y!=2)
//                    continue;

                //Do image match
                //Like convolution
//                double min=1e12;
//                for(int i=0;i<blockSize-target.getWidth();i++){
//                    for(int j=0;j<blockSize-target.getHeight();j++){
//                        //Calculate absolute delta
//                        long currentSum=0;
//
//                        for(int k=0;k<target.getWidth();k++){
//                            for(int l=0;l<target.getHeight();l++){
//                                //Origin x: x*blockSize + i + k
//                                //Origin y: y*blockSize + j + l
//                                //Target x: k
//                                //Target y: l
//
//                                int abs=Math.abs(target.getRGB(k,l)-image.getRGB(x*blockSize + i + k,y*blockSize + j + l));
//                                currentSum+=abs;
//                            }
//                        }
//
//                        double averageSum=currentSum/target.getWidth()*target.getHeight();
//
//                        System.out.println("Average sum: "+averageSum);
//                        if(averageSum<min){
//                            min=averageSum;
//                        }
//                    }
//                }
//
//                System.out.println("Min: "+min);
            }
        }
    }

}
