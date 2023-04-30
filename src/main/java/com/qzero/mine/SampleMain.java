package com.qzero.mine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

public class SampleMain {

    public static int counter=0;
    public static File lastFile=null;

    public static void main(String[] args) throws Exception{
        ImageProfile profile=ImageUtils.PROFILE_24X24_BY_ROBOT;
        String imageName="game.png";
        //Do segment
        BufferedImage game= ImageIO.read(new File(imageName));
//        ImageUtils.segmentImage(game,profile);

        //List file
//        Scanner scanner=new Scanner(System.in);
        File[] files=new File("segment/").listFiles();


        JFrame frame = new JFrame("demo");
        JPanel panel = new JPanel();
        JLabel label = new JLabel();

        label.setIcon(new ImageIcon(files[0].getAbsolutePath()));

        JTextField textField=new JTextField(20);
        textField.addActionListener(e -> {
            String input=textField.getText();
            System.out.println(input);
            textField.setText("");

            if(counter==files.length-1)
                return;

            if(input.equals("d") && lastFile!=null){
                lastFile.delete();
                return;
            }

            File file=files[counter];
//            int choice=Integer.parseInt(input);

            String targetDir="";
            switch (input){
                case "f":
                    targetDir="flag";
                    break;
                case "c":
                    targetDir="cover";
                    break;
                case "b":
                    targetDir="blank";
                    break;
                default:
                    targetDir=input;
                    break;
            }

            new File(profile.getImagePath()+"/"+targetDir+"/").mkdirs();
            lastFile=new File(profile.getImagePath()+"/"+targetDir+"/"+System.currentTimeMillis()+".png");
            file.renameTo(lastFile);

            counter++;

            label.setIcon(new ImageIcon(files[counter].getAbsolutePath()));
            frame.setVisible(true);
        });

        panel.add(label);
        panel.add(textField);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

//        for(File file:files){
//            label.setIcon(new ImageIcon(file.getAbsolutePath()));
//            frame.setVisible(true);
//
//            int choice=scanner.nextInt();
//
//            if(choice==-100 && lastFile!=null){
//                lastFile.delete();
//                continue;
//            }
//
//            String targetDir="";
//            switch (choice){
//                case -2:
//                    targetDir="flag";
//                    break;
//                case -1:
//                    targetDir="cover";
//                    break;
//                case 0:
//                    targetDir="blank";
//                    break;
//                default:
//                    targetDir=String.valueOf(choice);
//                    break;
//            }
//
////            frame.dispose();
//
//            new File(profile.getImagePath()+"/"+targetDir+"/").mkdirs();
//            lastFile=new File(profile.getImagePath()+"/"+targetDir+"/"+System.currentTimeMillis()+".png");
//            boolean b=file.renameTo(lastFile);
//            //System.out.println(b);
//
//
//        }
    }

}
