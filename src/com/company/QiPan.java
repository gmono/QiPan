package com.company;

import javafx.scene.media.VideoTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * 棋盘容器 主要用于存储和管理规则
 */
public class QiPan {
    static final int Empty=-1;
    int[][] matrix=null;
    static public void initmatrix(int[][] matrix,int rows,int cols,int value){
        for(int i=0;i<rows;++i){
            for(int j=0;j<cols;++j){
                matrix[i][j]=value;
            }
        }
    }
    public QiPan(int rows,int cols){
        matrix=new int[rows][cols];
        initmatrix(matrix,rows,cols,Empty);
    }
    ///棋盘相关部分
    /**
     * 设置棋盘状态
     * @param mat 新棋盘矩阵
     */
    public void setQiPan(int[][] mat){
        if(mat.length==matrix.length&&mat[0].length==matrix[0].length){
            this.matrix=mat.clone();
        }
    }

    /**
     * 得到棋盘矩阵
     * @return 棋盘矩阵（不可修改）
     */
    public final int[][] getQiPan(){
        return this.matrix;
    }

    /**
     * 放子
     * @param r 行
     * @param c 列
     * @param playerid 玩家id
     * @return 落子是否合法 如果合法返回true 落子成功 否则落子失败
     */
    public boolean put(int r,int c,int playerid){
        if(canPut(r,c,playerid)){
            matrix[r][c]=playerid;
            return true;
        }
        return false;
    }

    /**
     * 检查某个位置是否能由某个玩家落子
     * @param r 行
     * @param c 列
     * @param playerid 玩家id
     * @return 是否可以
     */
    public boolean canPut(int r,int c,int playerid){
        if(matrix[r][c]==Empty) return true;
        return false;
    }


    ///玩家相关部分
    List<Integer> winners=new ArrayList<>();
    List<IPlayer> players=new ArrayList<>();
    /**
     * 添加玩家
     * @param player 玩家对象
     * @return 玩家ID号
     */
    public int addPlayer(IPlayer player){
        players.add(player);
        return players.size()-1;
    }
    /**
     * 初始化所有玩家
     */
    protected void initPlayers(){
        for(int i=0;i<players.size();++i){
            players.get(i).init(this,i);
        }
    }


    ///回合相关部分
    int now_playerid=0;
    /**
     * 通知下一个玩家放子 如果所有玩家都放完 就返回false 否则返回true
     * @return 是否成功，如果所有玩家都放完了子就返回false 此时需要执行step函数开始下一回合
     */
    public boolean next(){
        //这里处理 没有玩家直接就是false的情况
        if(now_playerid==players.size()) return false;
        //通知下一个玩家放子
        IPlayer player=players.get(now_playerid);
        player.action();
        now_playerid++;
        //这里返回下一次有没有玩家可供调动
        if(now_playerid==players.size()) return false;
        return true;
    }

    /**
     * 执行下一回合 如果此回合执行后有玩家取胜 则返回true 否则返回false
     * @return 是否有玩家取胜
     */
    public boolean step(){
        now_playerid=0;
        scanWinners();
        if(this.winners.size()!=0) return true;
        else return false;
    }

    /**
     * 获取取胜的玩家列表
     * @return 取胜的玩家列表 如果没有玩家取胜 返回null
     */
    public int[] getWinners(){
        if(winners.size()==0) return null;
        int[] ret=new int[winners.size()];
        return ret;
    }

    /**
     * 扫描所有胜利者 并将其加入winner列表
     */
    protected void scanWinners(){
        return;
    }


    ///游戏控制

    /**
     * 开始游戏
     */
    public void start(int timeout) throws InterruptedException {
        this.initPlayers();
        int[] winner=null;
        for(int i=0;!this.step();++i){
            System.out.printf("当前第%d回合\n",i);
            do{
                System.out.printf("玩家 %s 执棋\n",players.get(now_playerid).getName());
            }while (this.next());
            if(timeout!=0) Thread.sleep(timeout);
        }
        //结束
        winner=this.getWinners();
        System.out.printf("游戏结束！以下玩家取胜:\n");
        for(IPlayer player:players){
            System.out.println(player.getName());
        }
    }
    public void start() throws InterruptedException {
        start(0);
    }
}
