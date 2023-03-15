package _04_HeuristicGame._00_AutoMoveMazeState;

import java.util.Random;

public class AutoMoveMazeState {
    public static Random mtForAction = new Random(0);
    
    public static AutoMoveMazeState randomAction(AutoMoveMazeState state) {
        AutoMoveMazeState nowState = new AutoMoveMazeState(state);
        for (int characterId = 0; characterId < CHARACTER_N; characterId++) {
            int y = mtForAction.nextInt(H);
            int x = mtForAction.nextInt(W);
            nowState.setCharacter(characterId, y, x);
        }
        return nowState;
    }
    
    public static void playGame(StringAIPair ai, long seed) {
        AutoMoveMazeState state = new AutoMoveMazeState(seed);
        state = ai.second.apply(state);
        System.out.println(state);
        long score = state.getScore(true);
        System.out.printf("Score of %s : %d%n", ai.first, score);
    }
    
    public static void main(String[] args) {
        StringAIPair ai = new StringAIPair("randomAciton", state -> randomAction(state));
        playGame(ai, 0);
    }
    
    public static int H = 5;
    public static int W = 5;
    public static int END_TURN = 5;
    public static int CHARACTER_N = 3;
            
    private int[][] points;
    private int turn;
    private Coord[] characters;
    private int gameScore;
    private long evaluatedScore;
    
    public AutoMoveMazeState(long seed) {
        this.points = new int[H][W];
        this.turn = 0;
        this.characters = new Coord[CHARACTER_N];
        this.gameScore = 0;
        this.evaluatedScore = 0;
        
        for (int i = 0; i < CHARACTER_N; i++) {
            this.characters[i] = new Coord(-1, -1);
        }
        
        Random random = new Random(seed);
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                points[y][x] = random.nextInt(9)+1;
            }
        }
    }
    
    public AutoMoveMazeState(AutoMoveMazeState other) {
        this.points = new int[H][W];
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                this.points[y][x] = other.points[y][x];
            }
        }
        this.turn = other.turn;
        this.characters = new Coord[CHARACTER_N];
        for (int i = 0; i < CHARACTER_N; i++) {
            this.characters[i] = new Coord(other.characters[i].x, other.characters[i].y);
        }
        this.gameScore = other.gameScore;
        this.evaluatedScore = other.evaluatedScore;
    }
    
    public void setCharacter(int charcterId, int y, int x) {
        this.characters[charcterId].x = x;
        this.characters[charcterId].y = y;
    }
    
    public boolean isDone() {
        return this.turn == END_TURN;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("turn :\t%d%n".formatted(this.turn));
        sb.append("score:\t%d%n".formatted(this.gameScore));
        for (int h = 0; h < H; h++) {
            for (int w = 0; w < W; w++) {
                boolean isWritten = false;
                for (Coord character : this.characters) {
                    if (character.y == h && character.x == w) {
                        sb.append("@");
                        isWritten = true;
                        break;
                    }
                }
                
                if (!isWritten) {
                    if (this.points[h][w] > 0) {
                        sb.append(this.points[h][w]);
                    } else {
                        sb.append(".");
                    }
                }
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
    
    public long getScore(boolean isPrint) {
        AutoMoveMazeState tmpState = new AutoMoveMazeState(this);
        for (Coord character : this.characters) {
            tmpState.points[character.y][character.x] = 0;
        }
        while (!tmpState.isDone()) {
            tmpState.advance();
            if (isPrint) {
                System.out.println(tmpState);
            }
        }
        return tmpState.gameScore;
    }
    
    private static int[] dx = new int[] {1,-1,0, 0};
    private static int[] dy = new int[] {0, 0,1,-1};
    
    public void movePlayer(int characterId) {
        Coord character = this.characters[characterId];
        int bestPoint = Integer.MIN_VALUE;
        int bestActionIndex = 0;
        for (int action = 0; action < 4; action++) {
            int ty = character.y + dy[action];
            int tx = character.x + dx[action];
            if (0<=ty && ty<H && 0<=tx && tx<W) {
                int point = this.points[ty][tx];
                if (point > bestPoint) {
                    bestPoint = point;
                    bestActionIndex = action;
                }
            }
        }
        
        character.y += dy[bestActionIndex];
        character.x += dx[bestActionIndex];
    }
    
    public void advance() {
        for (int charcterId = 0; charcterId < CHARACTER_N; charcterId++) {
            movePlayer(charcterId);
        }
        for (Coord character : this.characters) {
            this.gameScore += this.points[character.y][character.x];
            this.points[character.y][character.x] = 0;
        }
        this.turn++;
    }
}
