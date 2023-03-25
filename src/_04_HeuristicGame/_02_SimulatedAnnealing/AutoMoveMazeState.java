package _04_HeuristicGame._02_SimulatedAnnealing;

import java.util.List;
import java.util.Random;

public class AutoMoveMazeState {
    public static Random mtForAction = new Random(0);
    public static long INF = 1000000000;
    
    public static AutoMoveMazeState simulatedAnnealing(AutoMoveMazeState state, int number, double startTemp, double endTemp) {
        AutoMoveMazeState nowState = new AutoMoveMazeState(state);
        nowState.init();
        long bestScore = nowState.getScore(false);
        long nowScore = bestScore;
        AutoMoveMazeState bestState = new AutoMoveMazeState(nowState);
        
        for (int i = 0; i < number; i++) {
            AutoMoveMazeState nextState = new AutoMoveMazeState(nowState);
            nextState.transition();
            long nextScore = nextState.getScore(false);
            
            double temp = startTemp+(endTemp-startTemp)*(i/number);
            double probability = Math.exp((nextScore-nowScore)/temp);
            boolean isForceNext = probability>(mtForAction.nextLong(INF))/(double)INF;
            if (nextScore>nowScore || isForceNext) {
                nowScore = nextScore;
                nowState = nextState;
            }
            if (nextScore>bestScore) {
                bestScore = nextScore;
                bestState = nextState;
            }
        }
        return bestState;
    }
    
    public static void testAiScore(StringAIPair ai, int gameNumber) {
        double scoreMean = 0;
        for (int i = 0; i < gameNumber; i++) {
            AutoMoveMazeState state = new AutoMoveMazeState(mtForAction.nextLong());
            state = ai.second.apply(state);
            long score = state.getScore(false);
            scoreMean += score;
        }
        scoreMean /= (double)gameNumber;
        System.out.printf("Score of %s:\t%f%n", ai.first, scoreMean);
    }
    
    public static AutoMoveMazeState hillClimb(AutoMoveMazeState state, int number) {
        AutoMoveMazeState nowState = new AutoMoveMazeState(state);
        nowState.init();
        long bestScore = nowState.getScore(false);
        for (int i = 0; i < number; i++) {
            AutoMoveMazeState nextState = new AutoMoveMazeState(nowState);
            nextState.transition();
            long nextScore = nextState.getScore(false);
            if (nextScore > bestScore) {
                bestScore = nextScore;
                nowState = nextState;
            }
        }
        return nowState;
    }
    
    public static void main(String[] args) {
        int simulateNumber = 10000;
        List<StringAIPair> ais = List.of(
            new StringAIPair("hillClimb", state -> hillClimb(state, simulateNumber)),
            new StringAIPair("simulatedAnnealing", state -> simulatedAnnealing(state, simulateNumber, 500, 10))
        );
        
        int gameNumber = 1000;
        for (StringAIPair ai : ais) {
            testAiScore(ai, gameNumber);
        }
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
    
    public void init() {
        for (Coord character : this.characters) {
            character.y = mtForAction.nextInt(H);
            character.x = mtForAction.nextInt(W);
        }
    }
    
    public void transition() {
        Coord character = this.characters[mtForAction.nextInt(CHARACTER_N)];
        character.y = mtForAction.nextInt(H);
        character.x = mtForAction.nextInt(W);
    }
}
