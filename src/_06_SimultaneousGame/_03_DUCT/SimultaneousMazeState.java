package _06_SimultaneousGame._03_DUCT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimultaneousMazeState {
    public static int H = 5;
    public static int W = 5;
    public static int END_TURN = 20;
    
    private static int[] dx = {1, -1, 0, 0};
    private static int[] dy = {0, 0, 1, -1};
    
    private int[][] points;
    private int turn;
    private Character[] characters;
    
    public SimultaneousMazeState(SimultaneousMazeState other) {
        this.points = other.getPoints();
        this.turn = other.getTurn();
        this.characters = other.getCharacters();
    }
    
    public SimultaneousMazeState(long seed) {
        this.points = new int[H][W];
        this.turn = 0;
        this.characters = new Character[] {new Character(H/2, (W/2)-1), new Character(H/2, (W/2)+1)};
    
        Random mtForConstruct = new Random(seed);
        for (int y=0; y<H; y++) {
            for (int x=0; x<W/2+1; x++) {
                int ty = y;
                int tx = x;
                int point = mtForConstruct.nextInt(10);
                if (characters[0].y==y && characters[0].x==x) {
                    continue;
                }
                if (characters[1].y==y && characters[1].x==x) {
                    continue;
                }
                this.points[ty][tx] = point;
                tx = W - 1 - x;
                this.points[ty][tx] = point;
            }
        }
    }
    
    public void advance(int action0, int action1) {
        {
            var character = this.characters[0];
            var action = action0;
            character.x += dx[action];
            character.y += dy[action];
            var point = this.points[character.y][character.x];
            if (point>0) {
                character.gameScore += point;
            }
        }
        {
            var character = this.characters[1];
            var action = action1;
            character.x += dx[action];
            character.y += dy[action];
            var point = this.points[character.y][character.x];
            if (point>0) {
                character.gameScore += point;
            }
        }
        
        for (var character : this.characters) {
            this.points[character.y][character.x] = 0;
        }
        this.turn++;
    }
    
    public List<Integer> legalActions(int playerId) {
        List<Integer> actions = new ArrayList<>();
        var character = this.characters[playerId];
        for (int action = 0; action < 4; action++) {
            int ty = character.y + dy[action];
            int tx = character.x + dx[action];
            if (ty>=0 && ty<H && tx>=0 && tx<W) {
                actions.add(action);
            }
        }
        return actions;
    }
    
    public boolean isDone() {
        return this.turn == END_TURN;
    }
    
    @Override
    public String toString() {
        StringBuilder ss = new StringBuilder();
        ss.append("turn:\t" + this.turn + System.lineSeparator());
        ss.append("score(0):\t" + this.characters[0].gameScore + System.lineSeparator());
        ss.append("score(1):\t" + this.characters[1].gameScore + System.lineSeparator());
        
        for (int h=0; h<H; h++) {
            for (int w=0; w<W; w++) {
                boolean isWritten = false;
                if (this.characters[0].y==h && this.characters[0].x==w) {
                    ss.append("A");
                    isWritten = true;
                }
                if (this.characters[1].y==h && this.characters[1].x==w) {
                    ss.append("B");
                    isWritten = true;
                }
                if (!isWritten) {
                    if (this.points[h][w]>0) {
                        ss.append(this.points[h][w]);
                    } else {
                        ss.append(".");
                    }
                }
            }
            ss.append(System.lineSeparator());
        }
        
        return ss.toString();
    }
    
    public WinningStatus getWinningStatus() {
        if (isDone()) {
            if (characters[0].gameScore>characters[1].gameScore) {
                return WinningStatus.FRIST;
            } else if (characters[0].gameScore<characters[1].gameScore) {
                return WinningStatus.SECOND;
            } else {
                return WinningStatus.DRAW;
            }
        } else {
            return WinningStatus.NONE;
        }
    }
    
    public double getFirstPlayerScoreForWinRate() {
        switch (this.getWinningStatus()) {
        case FRIST:
            return 1;
        case SECOND:
            return 0;
        default:
            return 0.5;
        }
    }
    
    public int[][] getPoints() {
        int[][] points = new int[H][W];
        for (int h = 0; h < H; h++) {
            for (int w = 0; w < W; w++) {
                points[h][w] = this.points[h][w];
            }
        }
        return points;
    }
    
    public int getTurn() {
        return this.turn;
    }
    
    public Character[] getCharacters() {
        return new Character[]{new Character(this.characters[0]), new Character(this.characters[1])};
    }
}
