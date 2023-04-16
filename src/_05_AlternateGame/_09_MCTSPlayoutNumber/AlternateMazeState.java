package _05_AlternateGame._09_MCTSPlayoutNumber;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlternateMazeState {
    private static int INF = 1000000000;
    private static Random mtForAction = new Random(0);
        
    public static int mctsAction(AlternateMazeState state, int playoutNumber) {
        Node rootNode = new Node(state);
        rootNode.expand();
        for (int i = 0; i < playoutNumber; i++) {
            rootNode.evaluate();
        }
        
        List<Integer> legalActions = state.legalActions();
        int bestActionSearchedNumber = -1;
        int bestActionIndex = -1;
        for (int i = 0; i < legalActions.size(); i++) {
            int n = (int)rootNode.childNodes.get(i).n;
            if (n > bestActionSearchedNumber) {
                bestActionIndex = i;
                bestActionSearchedNumber = n;
            }
        }
        return legalActions.get(bestActionIndex);
    }
    
    public static int randomAction(AlternateMazeState state) {
        List<Integer> legalActions = state.legalActions();
        return legalActions.get(mtForAction.nextInt(legalActions.size()));
    }
    
    public static int alphaBetaScore(AlternateMazeState state, int alpha, int beta, int depth, TimeKeeper timeKeeper) {
        if (timeKeeper.isTimeOver()) {
            return 0;
        }
        
        if (state.isDone() || depth == 0) {
            return state.getScore();
        }
        
        List<Integer> legalActions = state.legalActions();
        if (legalActions.isEmpty()) {
            return state.getScore();
        }
        
        for (int action : legalActions) {
            AlternateMazeState nextState = new AlternateMazeState(state);
            nextState.advance(action);
            
            int score = -alphaBetaScore(nextState, -beta, -alpha, depth-1, timeKeeper);
            if (timeKeeper.isTimeOver()) {
                return 0;
            }
            if (score>alpha) {
                alpha = score;
            }
            if (alpha>=beta) {
                return alpha;
            }

        }
        return alpha;
    }
    
    public static int alphaBetaActionWithTimeThreshold(AlternateMazeState state, int depth, TimeKeeper timeKeeper) {
        int bestAction = -1;
        int alpha = -INF;
        int beta = INF;
        for (int action : state.legalActions()) {
            AlternateMazeState nextState = new AlternateMazeState(state);
            nextState.advance(action);
            int score = -alphaBetaScore(nextState, -beta, -alpha, depth, timeKeeper);
            if (timeKeeper.isTimeOver()) {
                return 0;
            }
            if (score > alpha) {
                bestAction = action;
                alpha = score;
            }
        }
        return bestAction;
    }
    
    public static int iterativeDeepeningAction(AlternateMazeState state, long timeThreshold) {
        TimeKeeper timeKeeper = new TimeKeeper(timeThreshold);
        int bestAction = -1;
        for (int depth=1; ;depth++) {
            int action = alphaBetaActionWithTimeThreshold(state, depth, timeKeeper);
            if (timeKeeper.isTimeOver()) {
                break;
            } else { 
                bestAction = action;
            }
        }
        return bestAction;
    }

    public static int miniMaxScore(AlternateMazeState state, int depth) {
        if (state.isDone() || depth == 0) {
            return state.getScore();
        }
        
        List<Integer> legalActions = state.legalActions();
        if (legalActions.isEmpty()) {
            return state.getScore();
        }
        
        int bestScore = -INF;
        for (int action : legalActions) {
            AlternateMazeState nextState = new AlternateMazeState(state);
            nextState.advance(action);
            
            int score = -miniMaxScore(nextState, depth-1);
            if (score>bestScore) {
                bestScore = score;
            }
        }
        return bestScore;
    }
    
    public static int miniMaxAction(AlternateMazeState state, int depth) {
        int bestAction = -1;
        int bestScore = Integer.MIN_VALUE;
        for (int action : state.legalActions()) {
            AlternateMazeState nextState = new AlternateMazeState(state);
            nextState.advance(action);
            int score = -miniMaxScore(nextState, depth);
            if (score > bestScore) {
                bestAction = action;
                bestScore = score;
            }
        }
        return bestAction;
    }
    
    public static void testFirstPlayerWinRate(List<StringAIPair> ais, int gameNumber) {
        double firstPlayerWinRate = 0;
        for (int i = 0; i < gameNumber; i++) {
            AlternateMazeState baseState = new AlternateMazeState(i);
            for (int j = 0; j < 2; j++) {
                AlternateMazeState state = new AlternateMazeState(baseState);
                StringAIPair firstAi = ais.get(j);
                StringAIPair secondAi = ais.get((j+1)%2);
                while (true) {
                    state.advance(firstAi.second.apply(state));
                    if (state.isDone()) {
                        break;
                    }
                    state.advance(secondAi.second.apply(state));
                    if (state.isDone()) {
                        break;
                    }
                }
                
                double winRatePoint = state.getFristPlayerScoreForWinRate();
                if (j==1) {
                    winRatePoint = 1-winRatePoint;
                }
                if (winRatePoint>=0) {
                    state.toString();
                }
                firstPlayerWinRate += winRatePoint;
            }
            
            System.out.printf("i %d w %f%n", i, firstPlayerWinRate/((i+1)*2));
        }
        
        firstPlayerWinRate /= (double)(gameNumber*2);
        System.out.printf("Winning rate of %s to %s:\t%f", ais.get(0).first, ais.get(1).first, firstPlayerWinRate);
    }
    
    public static List<AlternateMazeState> getSampleStates(int gameNumber) {
        Random mtForConstruct = new Random(0);
        List<AlternateMazeState> states = new ArrayList<>();
        for (int i = 0; i < gameNumber; i++) {
            AlternateMazeState state = new AlternateMazeState(mtForConstruct.nextLong());
            int turn = mtForConstruct.nextInt(END_TURN);
            for (int t = 0; t < turn; t++) {
                state.advance(randomAction(state));
            }
            states.add(state);
        }
        return states;
    }
    
    public static void calculateExecutionSpeed(StringAIPair ai, List<AlternateMazeState> states) {
        LocalDateTime startTime = LocalDateTime.now();
        for (AlternateMazeState state : states) {
            ai.second.apply(state);
        }
        
        long time = ChronoUnit.MILLIS.between(startTime, LocalDateTime.now());
        System.out.printf("%s take %d ms to process %d nodes%n", ai.first, time, states.size());
    }
    
    
    public static double playout(AlternateMazeState state) {
        switch(state.getWinningStatus()) {
        case WIN:
            return 1.0;
        case LOSE:
            return 0.0;
        case DRAW:
            return 0.5;
        default:
            state.advance(randomAction(state));
            return 1.0 - playout(state);
        }
    }
    
    public static int primitiveMontecarloAction(AlternateMazeState state, int playoutNumber) {
        List<Integer> legalActions = state.legalActions();
        double[]values = new double[legalActions.size()];
        double[]cnts = new double[legalActions.size()];
        for (int cnt = 0; cnt < playoutNumber; cnt++) {
            int index = cnt % legalActions.size();
            AlternateMazeState nextState = new AlternateMazeState(state);
            nextState.advance(legalActions.get(index));
            values[index] += 1.0 - playout(nextState);
            cnts[index]++;
        }
        
        int bestActionIndex = -1;
        double bestScore = -INF;
        for (int index = 0; index < legalActions.size(); index++) {
            double valueMean = values[index] / cnts[index];
            if (valueMean > bestScore) {
                bestScore = valueMean;
                bestActionIndex = index;
            }
        }
        
        return legalActions.get(bestActionIndex);
    }
    
    public static void main(String[] args) {
        List<StringAIPair> ais = List.of(
            new StringAIPair("mctsAction 3000", state -> mctsAction(state, 3000)),
            new StringAIPair("mctsAction 30", state -> mctsAction(state, 30))
        );
        testFirstPlayerWinRate(ais, 100);
    }
    
    public static int H = 3;
    public static int W = 3;
    public static int END_TURN = 4;
    
    private static int[] dx = {1, -1, 0, 0};
    private static int[] dy = {0, 0, 1, -1};
    
    private int[][] points;
    private int turn;
    private Character[] characters;
    
    public AlternateMazeState(long seed) {
        this.points = new int[H][W];
        this.turn = 0;
        this.characters = new Character[] {
            new Character(H/2, (W/2)-1),
            new Character(H/2, (W/2)+1)
        };
        
        Random mtForConstruct = new Random(seed);
        for (int y=0; y<H; y++) {
            for (int x=0; x<W; x++) {
                int point = mtForConstruct.nextInt(10);
                if (characters[0].y==y && characters[0].x==x) {
                    continue;
                }
                if (characters[1].y==y && characters[1].x==x) {
                    continue;
                }
                this.points[y][x] = point;
            }
        }
    }
    
    public AlternateMazeState(AlternateMazeState other) {
        this.points = new int[H][W];
        for (int y=0; y<H; y++) {
            for (int x=0; x<W; x++) {
                this.points[y][x] = other.points[y][x];
            }
        }
        
        this.turn = other.turn;
        this.characters = new Character[] {
            new Character(other.characters[0]), 
            new Character(other.characters[1])
        };
    }
    
    public boolean isDone() {
        return this.turn == END_TURN;
    }
    
    public void advance(int action) {
        Character character = this.characters[0];
        character.x += dx[action];
        character.y += dy[action];
        int point = this.points[character.y][character.x];
        if (point>0) {
            character.gameScore += point;
            this.points[character.y][character.x] = 0;
        }
        this.turn++;
        
        Character tmp = this.characters[0];
        this.characters[0] = this.characters[1];
        this.characters[1] = tmp;
    }
    
    public List<Integer> legalActions() {
        List<Integer> actions = new ArrayList<>();
        Character character = this.characters[0];
        for (int action=0; action<4; action++) {
            int ty = character.y + dy[action];
            int tx = character.x + dx[action];
            if (0<=ty && ty<H && 0<=tx && tx<W) {
                actions.add(action);
            }
        }
        return actions;
    }
    
    public WinningStatus getWinningStatus() {
        if (isDone()) {
            if (characters[0].gameScore > characters[1].gameScore) {
                return WinningStatus.WIN;
            } else if (characters[0].gameScore < characters[1].gameScore) {
                return WinningStatus.LOSE;
            } else {
                return WinningStatus.DRAW;
            }
        } else {
            return WinningStatus.NONE;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder ss = new StringBuilder();
        ss.append("turn:\t%d%n".formatted(this.turn));
        for (int playerId=0; playerId<this.characters.length; playerId++) {
            int actualPlayerId = playerId;
            if (this.turn%2==1) {
                actualPlayerId=(playerId+1)%2;
            }
            
            Character character = this.characters[actualPlayerId];
            ss.append("score(%d):\t%d\t y:%d\t x:%d%n".formatted(actualPlayerId, character.gameScore, character.y, character.x));
        }
        
        for (int h=0; h<H; h++) {
            for (int w=0; w<W; w++) {
                boolean isWritten = false;
                for (int playerId=0; playerId<this.characters.length; playerId++) {
                    int actualPlayerId = playerId;
                    if (this.turn%2==1) {
                        actualPlayerId = (playerId+1)%2;
                    }
                    
                    Character character = this.characters[playerId];
                    if (character.y==h && character.x==w) {
                        if (actualPlayerId==0) {
                            ss.append("A");
                        } else {
                            ss.append("B");
                        }
                        isWritten = true;
                    }
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
    
    public int getScore() {
        return characters[0].gameScore - characters[1].gameScore;
    }
    
    public boolean isFirstPlayer() {
        return this.turn%2==0;
    }
    
    public double getFristPlayerScoreForWinRate() {
        switch(this.getWinningStatus()) {
        case WIN:
            if (this.isFirstPlayer()) {
                return 1.0;
            } else {
                return 0.0;
            }
        case LOSE:
            if (this.isFirstPlayer()) {
                return 0.0;
            } else {
                return 1.0;
            }
        default:
            return 0.5;
        }
    }
}
