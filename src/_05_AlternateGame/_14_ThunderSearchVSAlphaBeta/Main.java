package _05_AlternateGame._14_ThunderSearchVSAlphaBeta;

import java.util.List;

import _05_AlternateGame._14_ThunderSearchVSAlphaBeta.iterativeDeepeningAction.IterativeDeepeningAction;
import _05_AlternateGame._14_ThunderSearchVSAlphaBeta.thunderSearchAction.ThunderSearchAction;


public class Main {

    public static void main(String[] args) {
        List<StringAIPair> ais = List.of(
            new StringAIPair("thunderSearchActionWithTimeThreshold 1ms", state -> ThunderSearchAction.thunderSearchActionWithTimeThreshold(state, 1)),
            new StringAIPair("iterativeDeepeningAction 1ms", state -> IterativeDeepeningAction.iterativeDeepeningAction(state,1))
        );
        testFirstPlayerWinRate(ais, 100);
    }
    
    public static void testFirstPlayerWinRate(List<StringAIPair> ais, int gameNumber) {
        double firstPlayerWinRate = 0;
        for (int i = 0; i < gameNumber; i++) {
            State baseState = new State(i);
            for (int j = 0; j < 2; j++) {
                State state = new State(baseState);
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

}
