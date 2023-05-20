package _08_Actual._01_MCTS;

import java.util.Random;


public class Main {
    public static void main(String[] args) {
        StringAIPair[] ais = {
            new StringAIPair("mctsActionWithTimeThreshold 1ms", state -> mctsActionWithTimeThreshold(state, 1)),	
            new StringAIPair("randomAction", state ->randomAction(state))
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
    
    private static Random mtForAction = new Random(0);

    public static int randomAction(ConnectFourState state) {
        int[] legalActions = state.legalActions();
        return legalActions[mtForAction.nextInt(legalActions.length)];
    }

    public static int mctsActionWithTimeThreshold(ConnectFourState state, long timeThreshold) {
        Node rootNode = new Node(state);
        rootNode.expand();
        TimeKeeper timeKeeper = new TimeKeeper(timeThreshold);
        for (int cnt = 0;; cnt++) {
            if (timeKeeper.isTimeOver()) {
                break;
            }
            rootNode.evaluate();
        }
        
        int[] legalActions = state.legalActions();
        int bestActionSearchedNumber = -1;
        int bestActionIndex = -1;
        for (int i = 0; i < legalActions.length; i++) {
            int n = (int)rootNode.childNodes.get(i).n;
            if (n > bestActionSearchedNumber) {
                bestActionIndex = i;
                bestActionSearchedNumber = n;
            }
        }
        return legalActions[bestActionIndex];
    }

}
