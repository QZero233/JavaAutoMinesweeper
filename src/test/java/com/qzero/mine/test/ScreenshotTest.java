package com.qzero.mine.test;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenshotTest {

    public static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

    @Test
    public void testGetScreenshot() throws Exception{

        Thread.sleep(2000);

        BufferedImage screenshot = (new Robot()).createScreenCapture(new
                Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));

        ImageIO.write(screenshot,"png",new File("test_ss.png"));
    }

}
