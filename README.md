# 通用棋类游戏
初期以五子棋和围棋为蓝本设计，后期加入棋子的区分
## 架构
1. 抽象棋盘，其中胜利规则和放子规则尚未明确
2. IPlayer 玩家接口,接受通知，调用棋盘给的接口来放子等
3. IView 观察者接口，可用于监视棋盘，显示棋盘，计算得分，设定规则，例如在某个情况下改变棋盘数据等
