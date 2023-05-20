package _08_Actual._02_BitBoard;

import _08_Actual._02_BitBoard.montecarlo.ConnectFourState;
import _08_Actual._02_BitBoard.montecarlo.MontecarloAction;
import _08_Actual._02_BitBoard.montecarlo_bit.MontecarloBitAction;


public class Main {
    public static void main(String[] args) {
        StringAIPair[] ais = {
            new StringAIPair("mctsActionBitWithTimeThreshold 1ms", state -> MontecarloBitAction.mctsActionBitWithTimeThreshold(state, 1)),	
            new StringAIPair("mctsActionWithTimeThreshold 1ms", state -> MontecarloAction.mctsActionWithTimeThreshold(state, 1))
        };
        testFirstPlayerWinRate(ais, 100);
    }
    
    public static void testFirstPlayerWinRate(StringAIPair[] ais, int gameNumber) {
        double firstPlayerWinRate = 0;
        for (int i = 0; i < gameNumber; i++) {
            ConnectFourState baseState = new ConnectFourState();
            for (int j = 0; j < 2; j++) {
                ConnectFourState state = new ConnectFourState(baseState);
                StringAIPair firstAi = ais[j];
                StringAIPair secondAi = ais[(j+1)%2];
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
                double winRatePoint = state.getFirstPlayerScoreForWinRate();
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
        System.out.printf("Winning rate of %s to %s:\t%f", ais[0].first, ais[1].first, firstPlayerWinRate);
    }


}
