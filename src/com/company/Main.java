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
        QiPan qiPan=new Wuziqi(10,10);
        qiPan.addPlayer(player);
        qiPan.addPlayer(new IPlayer() {
            @Override
            public void init(QiPan qiPan, int playerid) {
                System.out.println("初始化玩家2");
            }

            @Override
            public void action() {
                System.out.println("执行动作2");
            }

            @Override
            public String getName() {
                return "测试玩家2";
            }
        });
        qiPan.addView(new IView() {
            QiPan qiPan=null;
            int now=0;
            @Override
            public void init(QiPan qiPan) {
                this.qiPan=qiPan;
                System.out.println("初始化");
            }

            @Override
            public void gameStart() {
                System.out.println("游戏开始");
            }

            @Override
            public void stepStart() {
                now++;
                System.out.printf("第%d回合开始\n",now);
            }

            @Override
            public void stepEnd() {
                System.out.printf("第%d回合结束\n\n\n",now);
            }

            @Override
            public void actionStart(int playerid) {
                System.out.printf("玩家 %s 执棋\n",qiPan.players.get(playerid).getName());
            }

            @Override
            public void actionEnd(int playerid) {
//                System.out.printf("%s动作结束\n",qiPan.players.get(playerid).getName());
            }

            @Override
            public void gameOver(int[] winners) {
                System.out.println("游戏结束 以下玩家取胜:\n");
                for(int i:winners){
                    System.out.println(qiPan.players.get(i).getName());
                }
            }
        });
        qiPan.start(1000);
    }
}
