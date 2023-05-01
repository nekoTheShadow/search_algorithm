package _06_SimultaneousGame._01_PrimitiveMontecarlo;

import java.util.Random;

public class Main {
    private static Random mtForAction = new Random(0);
    private static String[] dstr = {"RIGHT", "LEFT", "DOWN", "UP"};
    private static long INF = 1000000000;
    
    public static int randomAction(SimultaneousMazeState state, int playerId) {
        var legalActions = state.legalActions(playerId);
        return legalActions.get(mtForAction.nextInt(legalActions.size()));
    }
    
    public static double playout(SimultaneousMazeState state) {
        switch (state.getWinningStatus()){
        case FRIST:
            return 1;
        case SECOND:
            return 0;
        case DRAW:
            return 0.5;
        default:
            state.advance(randomAction(state, 0), randomAction(state, 1));
            return playout(state);
        }
    }
    
    public static int primitiveMontecarloAction(SimultaneousMazeState state, int playerId, int playoutNumber) {
        var myLegalActions = state.legalActions(playerId);
        var oppLegalActions = state.legalActions((playerId+1)%2);
        double bestValue = -INF;
        int bestActioinIndex = -1;
        for (int i = 0; i < myLegalActions.size(); i++) {
            double value = 0;
            for (int j = 0; j < playoutNumber; j++) {
                var nextState = new SimultaneousMazeState(state);
                if (playerId==0) {
                    nextState.advance(myLegalActions.get(i), oppLegalActions.get(mtForAction.nextInt(oppLegalActions.size())));
                } else {
                    nextState.advance(oppLegalActions.get(mtForAction.nextInt(oppLegalActions.size())), myLegalActions.get(i));
                }
                
                double player0WinRate = playout(nextState);
                double winRate = playerId==0 ? player0WinRate : 1-player0WinRate;
                value += winRate;
            }
            if (value>bestValue) {
                bestActioinIndex = i;
                bestValue = value;
            }
        }
        
        return myLegalActions.get(bestActioinIndex);
    }
    
    
    public static void playGame(StringAIPair[] ais, long seed) {
        var state = new SimultaneousMazeState(seed);
        System.out.println(state);
        while (!state.isDone()) {
            int[] actions = new int[] {ais[0].second.apply(state, 0), ais[1].second.apply(state, 1)};
            System.out.println("actions " + dstr[actions[0]] + " " + dstr[actions[1]]);
            state.advance(actions[0], actions[1]);
            System.out.println(state);
        }
    }
    
    public static void main(String[] args) {
        StringAIPair[] ais = {
            new StringAIPair("primitiveMontecarloAction", (state, playerId) -> primitiveMontecarloAction(state, playerId, 1000)),	
            new StringAIPair("randomAction", (state, playerId) -> randomAction(state, playerId))
        };
//        playGame(ais, 500);
        testFirstPlayerWinRate(ais, 500);
    }
    
    public static void testFirstPlayerWinRate(StringAIPair[] ais, int gameNumber) {
        var mtForConstuct = new Random(0);
        double firstPlayerWinRate = 0;
        for (int i = 0; i < gameNumber; i++) {
            var state = new SimultaneousMazeState(mtForConstuct.nextLong());
            var firstAi = ais[0];
            var secondAi = ais[1];
            while (true) {
                state.advance(firstAi.second.apply(state, 0), secondAi.second.apply(state, 1));
                if (state.isDone()) {
                    break;
                }
            }
            double winRatePoint = state.getFirstPlayerScoreForWinRate();
            if (winRatePoint>=0) {
                System.out.println(state);
            }
            firstPlayerWinRate += winRatePoint;
            System.out.println("i " + i + " w " + firstPlayerWinRate/(i+1));
        }
        
        firstPlayerWinRate /= (double)gameNumber;
        System.out.println("Winning rate of " + ais[0].first + " to " + ais[1].first + ":\t" + firstPlayerWinRate);
    }
    
}
