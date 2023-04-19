package com.qzero.mine;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class MineMain {

    public static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

    public static boolean stop=true;

    public static void main(String[] args) throws Exception{
        //-1 : covered
        //-2 : flag
        //0 : nothing

//        Thread.sleep(4000);
//        Robot robot=new Robot();
//        robot.mousePress(InputEvent.BUTTON3_MASK);
//        robot.mouseRelease(InputEvent.BUTTON3_MASK);

        ImageUtils.loadSamples(ImageUtils.PROFILE_16X16_BY_ROBOT);

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//
//                while (true){
//                    try {
//                        if(stop){
//                            Thread.sleep(1000);
//                            continue;
//                        }
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//                    ImageProfile profile=ImageUtils.PROFILE_16X16_BY_ROBOT;
//                    try {
//                        Robot robot=new Robot();
//                        //Get screenshot
//                        BufferedImage screenshot = robot.createScreenCapture(new
//                                Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
////                        ImageIO.write(screenshot,"png",new File("game.png"));
//
////                    BufferedImage image=ImageIO.read(new File("game.png"));
//
//                        //Get solution
//                        int[][] game=ImageUtils.getGameMatrix(screenshot,profile);
//                        List<Action> actions=MineSweepUtils.getSolution(game, profile.getRowNum());
//
//                        System.out.println(actions);
//
//                        for(Action action:actions){
//
//                            Coordinate coordinate=ImageUtils.getCoordinate(profile,action.getX(),action.getY());
//                            robot.mouseMove(coordinate.getX(),coordinate.getY());
//
//                            if(action.getType()== Action.Type.OPEN){
//                                robot.mousePress(InputEvent.BUTTON1_MASK);
//                                robot.mouseRelease(InputEvent.BUTTON1_MASK);
//                            }else{
//                                robot.mousePress(InputEvent.BUTTON3_MASK);
//                                robot.mouseRelease(InputEvent.BUTTON3_MASK);
//                            }
//
//                            Thread.sleep(100);
//                        }
//
//                        robot.mouseMove(0,0);
//
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }.start();


//        final HotKeyListener listener = new HotKeyListener(){
//            @Override
//            public void onHotKey(HotKey arg0){
//                System.out.println("Hotkey "+arg0);
//                stop=!stop;
//            }
//        };

        final HotKeyListener listener = new HotKeyListener(){
            @Override
            public void onHotKey(HotKey arg0){
                System.out.println("Hotkey "+arg0);

                long start=System.currentTimeMillis();

                ImageProfile profile=ImageUtils.PROFILE_16X16_BY_ROBOT;
                try {
                    Robot robot=new Robot();
                    //Get screenshot
                    BufferedImage screenshot = robot.createScreenCapture(new
                            Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
                    ImageIO.write(screenshot,"png",new File("game.png"));

//                    BufferedImage image=ImageIO.read(new File("game.png"));

                    //Get solution
                    int[][] game=ImageUtils.getGameMatrix(screenshot,profile);
                    List<Action> actions=MineSweepUtils.getSolution(game, profile.getRowNum());

                    System.out.println(actions);
                    System.out.println(System.currentTimeMillis()-start);

                    for(Action action:actions){
                        Coordinate coordinate=ImageUtils.getCoordinate(profile,action.getX(),action.getY());
                        robot.mouseMove(coordinate.getX(),coordinate.getY());

                        if(action.getType()== Action.Type.OPEN){
                            robot.mousePress(InputEvent.BUTTON1_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_MASK);
                        }else{
                            robot.mousePress(InputEvent.BUTTON3_MASK);
                            robot.mouseRelease(InputEvent.BUTTON3_MASK);
                        }

                        Thread.sleep(100);
                    }

                    robot.mouseMove(0,0);


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        Provider provider = Provider.getCurrentProvider(true);
        provider.reset();
        provider.register(KeyStroke.getKeyStroke("ctrl LEFT"), listener);
    }

}
