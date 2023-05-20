package _08_Actual._01_MCTS;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ConnectFourState {
    public static int H = 6;
    public static int W = 7;
    
    private static int[] dx = {1, -1};
    private static int[] dyRightUp = {1, -1};
    private static int[] dyLefttUp = {-1, 1};

    private boolean isFirst;
    private int[][] myBoard;
    private int[][] enemyBoard;
    private WinningStatus winningStatus;
    
    public ConnectFourState() {
        this.isFirst = true;
        this.myBoard = new int[H][W];
        this.enemyBoard = new int[H][W];
        this.winningStatus = WinningStatus.NONE;
    }
    
    
    public ConnectFourState(ConnectFourState other) {
        this.isFirst = other.isFirst;
        this.myBoard = new int[H][W];
        this.enemyBoard = new int[H][W];
        this.winningStatus = other.winningStatus;
        
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                this.myBoard[i][j] = other.myBoard[i][j];
                this.enemyBoard[i][j] = other.enemyBoard[i][j];
            }
        }
    }
    
    public boolean isDone() {
        return this.winningStatus != WinningStatus.NONE;
    }
    
    public WinningStatus getWinningStatus() {
        return this.winningStatus;
    }
    
    public int[] legalActions() {
        List<Integer> actions = new ArrayList<>();
        for (int x = 0; x < W; x++) {
            for (int y = H-1; y >= 0; y--) {
                if (myBoard[y][x]==0 && enemyBoard[y][x]==0) {
                    actions.add(x);
                    break;
                }
            }
        }
        return actions.stream().mapToInt(Integer::intValue).toArray();
    }
    
    public void advance(int action) {
        Pair coordinate = null;
        for (int y = 0; y < H; y++) {
            if (this.myBoard[y][action]==0 && this.enemyBoard[y][action]==0) {
                this.myBoard[y][action] = 1;
                coordinate = Pair.of(y, action);
                break;
            }
        }
        
        // 横方向の連結判定
        {
            Queue<Pair> que = new ArrayDeque<>();
            que.add(coordinate);
            boolean[][] check = new boolean[H][W];
            int count = 0;
            while (!que.isEmpty()) {
                Pair tmpCod = que.remove();
                count++;
                if (count >= 4) {
                    this.winningStatus = WinningStatus.LOSE;
                    break;
                }
                check[tmpCod.first][tmpCod.second] = true;
                for (action = 0; action < 2; action++) {
                    int ty = tmpCod.first;
                    int tx = tmpCod.second+dx[action];
                    if (ty >= 0 && ty < H && tx >= 0 && tx < W && myBoard[ty][tx] == 1 && !check[ty][tx]) {
                        que.add(Pair.of(ty, tx));
                    }
                }
            }
        }
        
        // "／"方向の連結判定
        if (!isDone()) { 
            Queue<Pair> que = new ArrayDeque<>();
            que.add(coordinate);
            boolean[][] check = new boolean[H][W];
            int count = 0;
            while (!que.isEmpty()) {
                Pair tmpCod = que.remove();
                ++count;
                if (count >= 4){
                    this.winningStatus = WinningStatus.LOSE;
                    break;
                }
                check[tmpCod.first][tmpCod.second] = true;

                for (action = 0; action < 2; action++) {
                    int ty = tmpCod.first + dyRightUp[action];
                    int tx = tmpCod.second + dx[action];

                    if (ty >= 0 && ty < H && tx >= 0 && tx < W && myBoard[ty][tx] == 1 && !check[ty][tx]){
                        que.add(Pair.of(ty, tx));
                    }
                }
            }
        }
        
        // "\"方向の連結判定
        if (!isDone()) {
            Queue<Pair> que = new ArrayDeque<>();
            que.add(coordinate);
            boolean[][] check = new boolean[H][W];
            int count = 0;
            while (!que.isEmpty()){
                Pair tmpCod = que.remove();
                ++count;
                if (count >= 4) {
                    this.winningStatus = WinningStatus.LOSE;
                    break;
                }
                check[tmpCod.first][tmpCod.second] = true;

                for (action = 0; action < 2; action++) {
                    int ty = tmpCod.first + dyLefttUp[action];
                    int tx = tmpCod.second + dx[action];

                    if (ty >= 0 && ty < H && tx >= 0 && tx < W && myBoard[ty][tx] == 1 && !check[ty][tx]) {
                        que.add(Pair.of(ty, tx));
                    }
                }
            }
        }
        // 縦方向の連結判定
        if (!isDone()) { 
            int ty = coordinate.first;
            int tx = coordinate.second;
            boolean is_win = true;
            for (int i = 0; i < 4; i++) {
                boolean is_mine = (ty >= 0 && ty < H && tx >= 0 && tx < W && myBoard[ty][tx] == 1);
                if (!is_mine) {
                    is_win = false;
                    break;
                }
                --ty;
            }
            if (is_win) {
                this.winningStatus = WinningStatus.LOSE; // 自分の駒が揃ったら相手視点負け
            }
        }

        var tmp = this.enemyBoard;
        this.enemyBoard = this.myBoard;
        this.myBoard = tmp;
        
        this.isFirst = !isFirst;
        if (this.winningStatus == WinningStatus.NONE && legalActions().length == 0) {
            this.winningStatus = WinningStatus.DRAW;
        }
    }
    
    
    @Override
    public String toString() {
        StringBuilder ss = new StringBuilder();
        ss.append("is_first:\t").append(isFirst).append(System.lineSeparator());
        for (int y = H-1; y >= 0; y--) {
            for (int x = 0; x< W; x++) {
                char c = '.';
                if (myBoard[y][x]==1) {
                    c = isFirst ? 'x' : 'o';
                } else if (enemyBoard[y][x] ==1) {
                    c = isFirst ? 'o' : 'x';
                }
                ss.append(c);
            }
            ss.append(System.lineSeparator());
        }
        return ss.toString();
    }
    
    public double getFirstPlayerScoreForWinRate(){
        switch (this.getWinningStatus()) {
        case WIN:
            if (this.isFirst){
                return 1.;
            } else {
                return 0.;
            }
        case LOSE:
            if (this.isFirst){
                return 0.;
            } else {
                return 1.;
            }
        default:
            return 0.5;
        }
    }
}
