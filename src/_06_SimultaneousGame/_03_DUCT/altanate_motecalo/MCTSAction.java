package _06_SimultaneousGame._03_DUCT.altanate_motecalo;

import java.util.Random;

import _06_SimultaneousGame._03_DUCT.AlternateMazeState;
import _06_SimultaneousGame._03_DUCT.SimultaneousMazeState;


public class MCTSAction {
    private static Random mtForAction = new Random(0);
    public static long INF = 1000000000;


    public static int mctsAction(SimultaneousMazeState baseState, int playerId, int playoutNumber) {
        var state = new AlternateMazeState(baseState, playerId);
        Node rootNode = new Node(state);
        rootNode.expand();
        for (int i = 0; i < playoutNumber; i++) {
            rootNode.evaluate();
        }
        var legalActions = state.legalActions();
        
        double bestActionSearchedNumber = -1;
        int bestActionIndex = -1;
        for (int i = 0; i < legalActions.size(); i++) {
            double n = rootNode.childNodes.get(i).n;
            if (n>bestActionSearchedNumber) {
                bestActionIndex=i;
                bestActionSearchedNumber=n;
            }
        }
        return legalActions.get(bestActionIndex);
    }

    
    
    public static int randomAction(AlternateMazeState state) {
        var legalActions = state.legalActions();
        return legalActions.get(mtForAction.nextInt(legalActions.size()));
    }
    
    public static double playout(AlternateMazeState state) {
        switch (state.getWinningStatus()){
        case FRIST:
            return 1;
        case SECOND:
            return 0;
        case DRAW:
            return 0.5;
        default:
            state.advance(randomAction(state));
            return 1-playout(state);
        }
    }
}
