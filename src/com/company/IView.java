package com.company;

/**
 * 观察者接口
 * 观察者在每此玩家放子和回合结束以及游戏开始结束时收到通知
 */
public interface IView {
    void init(QiPan qiPan);
    void gameStart();
    void stepStart();
    void stepEnd();
    void actionStart(int playerid);
    void actionEnd(int playerid);
    void gameOver(int[] winners);
}
