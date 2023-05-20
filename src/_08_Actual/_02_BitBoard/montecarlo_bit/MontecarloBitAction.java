package _08_Actual._02_BitBoard.montecarlo_bit;

import java.util.Random;

import _08_Actual._02_BitBoard.TimeKeeper;
import _08_Actual._02_BitBoard.montecarlo.ConnectFourState;


public class MontecarloBitAction {
    private static Random mtForAction = new Random(0);

    public static int randomAction(ConnectFourStateByBitSet state) {
        int[] legalActions = state.legalActions();
        return legalActions[mtForAction.nextInt(legalActions.length)];
    }

    public static int mctsActionBitWithTimeThreshold(ConnectFourState state, long timeThreshold) {
        Node rootNode = new Node(new ConnectFourStateByBitSet(state));
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
