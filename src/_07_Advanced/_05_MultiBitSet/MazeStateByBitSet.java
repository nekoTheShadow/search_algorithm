package _07_Advanced._05_MultiBitSet;

import java.util.ArrayList;
import java.util.List;

public class MazeStateByBitSet  implements Comparable<MazeStateByBitSet>  {
    private int[][] points;
    private Mat wholePointMat;
    private int turn;
    private Mat walls;
    private Coord character;
    
    public int gameScore;
    public long evaluatedScore;
    public int firstAction;
    public long hash;
    
    public MazeStateByBitSet(WallMazeState state) {
        this.points = new int[WallMazeState.H][WallMazeState.W];
        this.wholePointMat = new Mat();
        this.turn = state.turn;
        this.walls = new Mat();
        this.character = new Coord(state.character.y, state.character.x);
        this.gameScore = state.gameScore;
        this.evaluatedScore = 0;
        this.firstAction = -1;
        
        for (int y = 0; y < WallMazeState.H; y++) {
            for (int x = 0; x < WallMazeState.W; x++) {
                if (state.walls[y][x]>0) {
                    this.walls.set(y, x);
                }
                if (state.points[y][x]>0) {
                    this.points[y][x] = state.points[y][x];
                    this.wholePointMat.set(y, x);
                }
            }
        }
        
        initHash();
    }
    
    public MazeStateByBitSet(MazeStateByBitSet other) {
        this.points = new int[WallMazeState.H][WallMazeState.W];
        this.wholePointMat = new Mat(other.wholePointMat);
        this.turn = other.turn;
        this.walls = new Mat(other.walls);
        this.character = new Coord(other.character.y, other.character.x);
        this.gameScore = other.gameScore;
        this.evaluatedScore = other.evaluatedScore;
        this.firstAction = other.firstAction;
        this.hash = other.hash;
        
        for (int y = 0; y < WallMazeState.H; y++) {
            for (int x = 0; x < WallMazeState.W; x++) {
                this.points[y][x] = other.points[y][x];
            }
        }
    }
    
    public void evaluatedScore() {
        this.evaluatedScore = this.gameScore*WallMazeState.H*WallMazeState.W-getDistanceToNearestPoint();
    }
    
    public boolean isDone() {
        return this.turn == WallMazeState.END_TURN;
    }
    
    public void advance(int action) {
        hash ^= ZobristHash.character[character.y][character.x];
        this.character.x += WallMazeState.dx[action];
        this.character.y += WallMazeState.dy[action];
        int point = this.points[this.character.y][this.character.x];
        this.hash ^= ZobristHash.character[character.y][character.x];
        if (point > 0) {
            this.hash ^= ZobristHash.points[this.character.y][this.character.x][point];
            this.gameScore += point;
            wholePointMat.del(this.character.y, this.character.x);
            this.points[this.character.y][this.character.x] = 0;
        }
        this.turn++;
    }
    
    public int[] legalActions() {
        List<Integer> actions = new ArrayList<>();
        for (int action = 0; action < 4; action++) {
            int ty = this.character.y + WallMazeState.dy[action];
            int tx = this.character.x + WallMazeState.dx[action];
            if (0<=ty && ty<WallMazeState.H && 0<=tx && tx<WallMazeState.W && !this.walls.get(ty, tx)) {
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
        for (int h = 0; h < WallMazeState.H; h++) {
            for (int w = 0; w < WallMazeState.W; w++) {
                if (this.walls.get(h, w)) {
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
    
    private int getDistanceToNearestPoint() {
        var now = new Mat();
        now.set(this.character.y, this.character.x);
        for (int depth=0; ;depth++) {
            if (now.isAnyEqual(this.wholePointMat)) {
                return depth;
            }
            
            var next = new Mat(now);
            next.expand();
            next.andeqNot(this.walls);
            if (next.isEqual(now)) {
                break;
            }
            now = next;
        }
        return WallMazeState.H+WallMazeState.W;
    }
    
    private void initHash() {
        this.hash = 0L;
        this.hash ^= ZobristHash.character[this.character.y][this.character.x];
        for (int y = 0; y < WallMazeState.H; y++) {
            for (int x = 0; x < WallMazeState.W; x++) {
                int point = this.points[y][x];
                if (point>0) {
                    this.hash ^= ZobristHash.points[y][x][point];
                }
            }
        }
    }

    @Override
    public int compareTo(MazeStateByBitSet o) {
        return Long.compare(o.evaluatedScore, this.evaluatedScore);
    }
}
