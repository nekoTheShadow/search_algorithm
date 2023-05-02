package _06_SimultaneousGame._03_DUCT.montecarlo;

import java.util.Random;

import _06_SimultaneousGame._03_DUCT.SimultaneousMazeState;

public class DUCTAction {
    private static Random mtForAction = new Random(0);
    public static long INF = 1000000000;

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
    
    public static int ductAction(SimultaneousMazeState state, int playerId, int playoutNumber) {
        Node rootNode = new Node(state);
        rootNode.expand();
        for (int i = 0; i < playoutNumber; i++) {
            rootNode.evaluate();
        }
        var legalActions = state.legalActions(playerId);
        int iSize = rootNode.childNodeses.size();
        int jSize = rootNode.childNodeses.get(0).size();
        
        if (playerId==0) {
            double bestActionSearchNumber = -1;
            int bestActionIndex = -1;
            for (int i = 0; i < iSize; i++) {
                double n = 0;
                for (int j = 0; j < jSize; j++) {
                    n += rootNode.childNodeses.get(i).get(j).n;
                }
                if (n>bestActionSearchNumber) {
                    bestActionIndex=i;
                    bestActionSearchNumber=n;
                }
            }
            return legalActions.get(bestActionIndex);
        } else {
            double bestActionSearchedNumber = -1;
            int bestJ = -1;
            for (int j = 0; j < jSize; j++) {
                double n = 0;
                for (int i = 0; i < iSize; i++) {
                    n += rootNode.childNodeses.get(i).get(j).n;
                }
                if (n>bestActionSearchedNumber) {
                    bestJ = j;
                    bestActionSearchedNumber = n;
                }
            }
            return legalActions.get(bestJ);
        }
    }
}
