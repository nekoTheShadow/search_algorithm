package _06_SimultaneousGame._02_MCTSSimulation;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        StringAIPair[] ais = {
            new StringAIPair("mctsAction", (state, playerId) -> Action.mctsAction(state, playerId, 1000)),	
            new StringAIPair("primitiveMontecarloAction", (state, playerId) -> Action.primitiveMontecarloAction(state, playerId, 100))
        };
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
