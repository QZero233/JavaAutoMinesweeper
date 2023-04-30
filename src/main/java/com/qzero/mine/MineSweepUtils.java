package com.qzero.mine;

import java.util.ArrayList;
import java.util.List;

public class MineSweepUtils {

    public static List<Action> getSolution(int[][] game,int xNum,int yNum){

        List<Action> result=new ArrayList<>();

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
        for(int i=0;i<xNum;i++){
            for(int j=0;j<yNum;j++){

                int current=game[i][j];
                if(current<=0)
                    continue;

                int covered=0;
                int flagged=0;
                int uncovered=0;
                for(int k=0;k<8;k++){
                    int nX=i+directions[k][0];
                    int nY=j+directions[k][1];

                    if(nX < 0 || nX >=xNum || nY < 0 || nY >=yNum)
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

                        if(nX < 0 || nX >=xNum || nY < 0 || nY >=yNum)
                            continue;

                        if(game[nX][nY]==-1 && !result.contains(new Action(nX,nY, Action.Type.OPEN))){
                            result.add(new Action(nX,nY, Action.Type.OPEN));
//                            System.out.println("Open "+nX+","+nY);
                        }
                    }

                }else if(flagged==current-1 && flagged+covered==current){
                    //Can mark a bomb

                    for(int k=0;k<8;k++){
                        int nX=i+directions[k][0];
                        int nY=j+directions[k][1];

                        if(nX < 0 || nX >=xNum || nY < 0 || nY >=yNum)
                            continue;

                        if(game[nX][nY]==-1 && !result.contains(new Action(nX,nY, Action.Type.MARK))){
                            result.add(new Action(nX,nY, Action.Type.MARK));
//                            System.out.println("Mark "+nX+","+nY);
                        }
                    }
                }

            }
        }

        return result;
    }



}
