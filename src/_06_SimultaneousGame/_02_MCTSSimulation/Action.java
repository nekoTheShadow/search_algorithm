package _06_SimultaneousGame._02_MCTSSimulation;

import java.util.Random;

public class Action {
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
