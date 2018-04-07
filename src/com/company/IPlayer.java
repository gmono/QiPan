package com.company;

/**
 * 玩家接口 玩家用于执行棋盘动作并查看棋盘
 */
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
     * @return 玩家名称
     */
    String getName();
}
