package com.company;

import javafx.scene.media.VideoTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * 棋盘容器 主要用于存储和管理规则
 */
public abstract class QiPan {
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
    public abstract boolean canPut(int r,int c,int playerid);


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
        //动作执行信号
        actionStart();
        IPlayer player=players.get(now_playerid);
        player.action();
        //动作结束信号
        actionEnd();
        now_playerid++;
        //这里返回下一次有没有玩家可供调动
        if(now_playerid==players.size()){
            //回合结束 发送回合结束信号
            stepEnd();
            return false;
        }
        return true;
    }

    /**
     * 执行下一回合 如果此回合执行后有玩家取胜 则返回true 否则返回false
     * @return 是否有玩家取胜
     */
    public boolean step(){
        now_playerid=0;
        //执行规则 扫描是否有胜利者
        scanWinners();
        //新回合开始信号
        stepStart();
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
     * 强制设置胜利者
     * @param winners 胜利者
     */
    public void setWinners(int[] winners){
        //转换
        List<Integer> ws=new ArrayList<>();
        for(int i:winners){
            ws.add(i);
        }
        this.winners=ws;
    }
    /**
     * 扫描所有胜利者 并将其加入winner列表
     * 在此处添加取胜规则
     */
    protected abstract void scanWinners();

    ///游戏控制

    /**
     * 开始游戏
     */
    public void start(int timeout) throws InterruptedException {
        this.initPlayers();
        int[] winner=null;
        //游戏开始信号
        gameStart();
        while(!this.step()){
            while(this.next());
            if(timeout!=0) Thread.sleep(timeout);
        }
        //游戏结束信号
        gameOver();
    }
    public void start() throws InterruptedException {
        start(0);
    }
    ///观察者相关
    List<IView> views=new ArrayList<>();
    public void addView(IView view){
        views.add(view);
        view.init(this);;
    }
    //信号组
    protected void gameStart(){views.forEach(IView::gameStart);}
    protected void stepStart(){views.forEach(IView::stepStart);}
    protected void stepEnd(){views.forEach(IView::stepEnd);}
    protected void actionStart(){views.forEach((IView v)->v.actionStart(now_playerid));}
    protected void actionEnd(){views.forEach((IView v)->v.actionEnd(now_playerid));}
    protected void gameOver(){views.forEach((IView v)->v.gameOver(this.getWinners()));}
}
