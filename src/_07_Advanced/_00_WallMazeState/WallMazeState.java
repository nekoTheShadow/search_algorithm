package _07_Advanced._00_WallMazeState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WallMazeState {
    public static int H = 5;
    public static int W = 5;
    public static int END_TURN = 4;
    
    private static int[] dx = {1, -1, 0,  0};
    private static int[] dy = {0,  0, 1, -1};
    
    private int[][] points;
    private int turn;
    private int[][] walls;
    
    public Coord character;
    public int gameScore;
    public long evaluatedScore;
    public int firstAction;
    
    public WallMazeState(long seed) {
        Random mtForConstruct = new Random(0);
        
        this.points = new int[H][W];
        this.turn = 0;
        this.walls = new int[H][W];
        this.character = new Coord(mtForConstruct.nextInt(H), mtForConstruct.nextInt(W));
        this.gameScore= 0;
        this.evaluatedScore = 0;
        this.firstAction = -1;
        
        for (int y = 1; y < H; y+= 2) {
            for (int x = 1; x < W; x += 2) {
                int ty = y;
                int tx = x;
                if (ty==character.y && tx==character.x) {
                    continue;
                }
                this.walls[ty][tx]=1;
                int directionSize = 3;
                if (y==1) {
                    directionSize = 4;
                }
                int direction = mtForConstruct.nextInt(directionSize);
                ty += dy[direction];
                tx += dx[direction];
                if (ty==character.y && tx==character.x) {
                    continue;
                }
                this.walls[ty][tx] = 1;
            }
        }
        
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (y==character.y && x==character.x) {
                    continue;
                }
                this.points[y][x] = mtForConstruct.nextInt(10);
            }
        }
    }
    
    public boolean isDone() {
        return this.turn == END_TURN;
    }
    
    public void evaluatedScore() {
        this.evaluatedScore = this.gameScore;
    }
    
    public void advance(int action) {
        this.character.x += dx[action];
        this.character.y += dy[action];
        int point = this.points[this.character.y][this.character.x];
        if (point > 0) {
            this.gameScore += point;
            this.points[this.character.y][this.character.x] = 0;
        }
        this.turn++;
    }
    
    public int[] legalActions() {
        List<Integer> actions = new ArrayList<>();
        for (int action = 0; action < 4; action++) {
            int ty = this.character.y + dy[action];
            int tx = this.character.x + dx[action];
            if (0<=ty && ty<W && 0<=tx && tx<W && this.walls[ty][tx]==0) {
                actions.add(action);
            }
        }
        return actions.stream().mapToInt(Integer::intValue).toArray();
    }
    
    @Override
    public String toString() {
        StringBuilder ss = new StringBuilder();
        ss.append("turn:\t").append(this.turn).append(System.lineSeparator());
        ss.append("score:\t").append(this.gameScore).append(System.lineSeparator());
        for (int h = 0; h < H; h++) {
            for (int w = 0; w < W; w++) {
                if (this.walls[h][w]==1) {
                    ss.append("#");
                } else if (this.character.y==h && this.character.x==w) {
                    ss.append("@");
                } else if (this.points[h][w]>0) {
                    ss.append(points[h][w]);
                } else {
                    ss.append(".");
                }
            }
            ss.append(System.lineSeparator());
        }
        return ss.toString();
    }
}
