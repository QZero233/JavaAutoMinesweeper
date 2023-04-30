package com.qzero.mine.test;

import com.qzero.mine.ImageProfile;
import com.qzero.mine.ImageUtils;
import com.qzero.mine.MineSweepUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageUtilsTest {

    @Test
    public void testGetGameMatrix() throws Exception{
        ImageUtils.loadSamples(ImageUtils.PROFILE_24X24_BY_ROBOT);
        BufferedImage game= ImageIO.read(new File("test_ss.png"));
        int[][] gameMatrix= ImageUtils.getGameMatrix(game,ImageUtils.PROFILE_24X24_BY_ROBOT);

        for(int y=0;y<gameMatrix.length;y++){
            for(int x=0;x<gameMatrix.length;x++){
                System.out.print(gameMatrix[x][y]+"\t");
            }
            System.out.println();
        }


        System.out.println(MineSweepUtils.getSolution(gameMatrix,24,24));
    }

//    @Test
//    public void testScale() throws Exception{
//        ImageUtils.scaleOriginSamples(ImageUtils.PROFILE_16X16_BY_ROBOT.getBlockSize());
//    }

    @Test
    public void testSegment() throws Exception{
        BufferedImage game= ImageIO.read(new File("test_ss.png"));
        ImageUtils.segmentImage(game,ImageUtils.PROFILE_16X30_BY_ROBOT);
    }

}
