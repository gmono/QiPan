package com.company;

/**
 * 观察者接口
 * 观察者在每此玩家放子和回合结束以及游戏开始结束时收到通知
 */
public interface IView {
    /**
     * 初始化观察者
     * @param qiPan 目标棋盘
     */
    void init(QiPan qiPan);

    /**
     * 游戏开始
     */
    void gameStart();

    /**
     * 回合开始
     */
    void stepStart();

    /**
     * 回合结束
     */
    void stepEnd();

    /**
     * 某玩家开始执行动作
     * @param playerid 玩家id
     */
    void actionStart(int playerid);

    /**
     * 某玩家执行动作结束
     * @param playerid 玩家id
     */
    void actionEnd(int playerid);

    /**
     * 游戏结束
     * @param winners 胜利者列表
     */
    void gameOver(int[] winners);
}
