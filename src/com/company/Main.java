package com.company;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        IPlayer player=new IPlayer() {
            @Override
            public void init(QiPan qiPan, int playerid) {
                System.out.println("初始化玩家");
            }

            @Override
            public void action() {
                System.out.println("执行动作");
            }

            @Override
            public String getName() {
                return "测试玩家";
            }
        };
        QiPan qiPan=new QiPan(10,10);
        qiPan.addPlayer(player);
        qiPan.start(1000);
    }
}
