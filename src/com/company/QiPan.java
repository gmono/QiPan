package com.company;

import javafx.scene.media.VideoTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * 棋盘容器 主要用于存储和管理规则
 */
public abstract class QiPan {
    static final int Empty=-1;
    //玩家矩阵 每个值表示此位置上的子的所属玩家id
    protected int[][] p_matrix=null;
    //棋子矩阵 每个值表示此位置上的子的类型id
    protected int[][] l_matrix=null;
    static protected void initmatrix(int[][] matrix,int value){
        for(int i=0;i<matrix.length;++i){
            for(int j=0;j<matrix[0].length;++j){
                matrix[i][j]=value;
            }
        }
    }
    public QiPan(int rows,int cols){
        p_matrix=new int[rows][cols];
        l_matrix=new int[rows][cols];
        initmatrix(p_matrix,Empty);
        initmatrix(l_matrix,Empty);
    }
    ///棋盘相关部分
    /**
     * 设置棋盘状态
     * @param player_mat 新棋盘矩阵
     */
    public void setQiPan(int[][] player_mat,int[][] qizi_mat){
        if(player_mat.length==p_matrix.length&&player_mat[0].length==p_matrix[0].length){
            if(qizi_mat.length==l_matrix.length&&qizi_mat[0].length==l_matrix[0].length) {
                this.p_matrix = player_mat.clone();
                this.l_matrix=qizi_mat.clone();
            }
        }
    }

    /**
     * 得到棋盘矩阵（用户）
     * @return 棋盘矩阵（不可修改） 表示每个位置上的棋子所属用户id
     */
    public final int[][] getPlayer_QiPan(){
        return this.p_matrix;
    }

    /**
     * 得到棋盘矩阵（棋子类型）
     * @return 棋盘矩阵（不可修改） 表示每个位置上的棋子类型
     */
    public final int[][] getQizi_QiPan(){
        return this.l_matrix;
    }

    /**
     * 放子
     * @param r 行
     * @param c 列
     * @param playerid 玩家id
     * @param qid 棋子类型id
     * @return 落子是否合法 如果合法返回true 落子成功 否则落子失败
     */
    public boolean put(int r,int c,int playerid,int qid){
        if(canPut(r,c,playerid,qid)){
            p_matrix[r][c]=playerid;
            l_matrix[r][c]=qid;
            return true;
        }
        return false;
    }

    /**
     * 获取指定位置的棋子信息
     * @param r 行
     * @param c 列
     * @return 数组 {所属用户id,棋子类型}
     */
    public int[] get(int r,int c){
        return new int[]{p_matrix[r][c],l_matrix[r][c]};
    }

    /**
     * 检查某个位置是否能由某个玩家落子
     * @param r 行
     * @param c 列
     * @param playerid 玩家id
     * @param qid 棋子类型id
     * @return 是否可以
     */
    public abstract boolean canPut(int r,int c,int playerid,int qid);


    ///玩家相关部分
    protected List<Integer> winners=new ArrayList<>();
    protected List<IPlayer> players=new ArrayList<>();
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
     * @param timeout 每回合停顿时间
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

    /**
     * 开始游戏 停顿时间为0
     */
    public void start() throws InterruptedException {
        start(0);
    }
    ///观察者相关
    List<IView> views=new ArrayList<>();

    /**
     * 添加观察者
     * @param view 观察者对象
     */
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
