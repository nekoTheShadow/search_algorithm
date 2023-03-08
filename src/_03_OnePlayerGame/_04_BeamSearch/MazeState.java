package _03_OnePlayerGame._04_BeamSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MazeState implements Comparable<MazeState> {
    public static void main(String[] args) {
        playGame(121321);
    }
    
    public static void playGame(long seed) {
        MazeState state = new MazeState(seed);
        System.out.println(state);
        while (!state.isDone()) {
            state.advance(greedyAction(state));
            System.out.println(state);
        }
        
    }
    
    public static int randomAction(MazeState state) {
        Random random = ThreadLocalRandom.current();
        List<Integer> actions = state.legalActions();
        return actions.get(random.nextInt(actions.size()));
    }
    
    public static int greedyAction(MazeState state) {
        List<Integer> legalActions = state.legalActions();
        int bestScore = -INF;
        int bestAction = -1;
        for (int action : legalActions) {
            MazeState nowState = new MazeState(state);
            nowState.advance(action);
            nowState.evaluatedScore();
            if (nowState.evaluatedScore > bestScore) {
                bestScore = nowState.evaluatedScore;
                bestAction = action;
            }
        }
        return bestAction;
    }
    
    public static int beamSearchAction(MazeState state, int beamWidth, int beamDepth) {
        PriorityQueue<MazeState> nowBeam = new PriorityQueue<>();
        MazeState bestState = null;
        
        nowBeam.add(state);
        for (int t = 0; t < beamDepth; t++) {
            PriorityQueue<MazeState> nextBeam = new PriorityQueue<>();
            for (int i = 0; i < beamWidth; i++) {
                if (nowBeam.isEmpty()) {
                    break;
                }
                
                MazeState nowState = nowBeam.remove();
                List<Integer> legalActions = nowState.legalActions();
                for (int action : legalActions) {
                    MazeState nextState = new MazeState(nowState);
                    nextState.advance(action);
                    nextState.evaluatedScore();
                    if (t == 0) {
                        nextState.firstAction = action;
                    }
                    nextBeam.add(nextState);
                }
            }
            
            nowBeam = nextBeam;
            bestState = nowBeam.peek();
            if (bestState.isDone()) {
                break;
            }
        }
        return bestState.firstAction;
    }
    
    private static int H = 3;
    private static int W = 4;
    public static int END_TURN = 4;
    private static int[] dx = new int[] {1,-1,0, 0};
    private static int[] dy = new int[] {0, 0,1,-1};
    private static int INF = Integer.MAX_VALUE;
    
    private int[][] points;
    private int turn;
    public Coord character;
    public int gameScore;
    public int evaluatedScore;
    public int firstAction;
    
    public MazeState(long seed) {
        this.turn = 0;
        this.gameScore = 0;
        this.firstAction = -1;
        
        Random random = new Random(seed);
        this.character = new Coord(random.nextInt(W), random.nextInt(H));
        this.points = new int[H][W];
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (y == this.character.y && x == this.character.x) {
                    this.points[y][x] = 0;;
                } else {
                    this.points[y][x] = random.nextInt(10);
                }
            }
        }
    }
    
    public MazeState(MazeState other) {
        this.points = new int[H][W];
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                this.points[y][x] = other.points[y][x];
            }
        }
        this.turn = other.turn;
        this.character = new Coord(other.character.x, other.character.y);
        this.gameScore = other.gameScore;
        this.evaluatedScore = other.evaluatedScore;
        this.firstAction = other.firstAction;
    }
    
    public boolean isDone() {
        return this.turn == END_TURN;
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
    
    public List<Integer> legalActions() {
        List<Integer> actions = new ArrayList<>();
        for (int action = 0; action < 4; action++) {
            int ty = this.character.y + dy[action];
            int tx = this.character.x + dx[action];
            if (0 <= ty && ty < H && 0 <= tx && tx < W) {
                actions.add(action);
            }
        }
        return actions;
    }
    
    public void evaluatedScore() {
        this.evaluatedScore = this.gameScore;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("turn :\t%d%n".formatted(this.turn));
        sb.append("score:\t%d%n".formatted(this.gameScore));
        for (int h = 0; h < H; h++) {
            for (int w = 0; w < W; w++) {
                if (this.character.y == h && this.character.x == w) {
                    sb.append("@");
                } else if (this.points[h][w] > 0) {
                    sb.append(this.points[h][w]);
                } else {
                    sb.append(".");
                }
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public int compareTo(MazeState other) {
        return Integer.compare(other.evaluatedScore, this.evaluatedScore);
    }
}
