package com.company;

public interface IPlayer {
    /**
     * 初始化玩家对象
     * @param qiPan 目标棋盘
     * @param playerid 玩家id
     */
    void init(QiPan qiPan,int playerid);

    /**
     * 通知玩家执行动作
     */
    void action();

    /**
     * 取玩家名
     */
    String getName();
}
