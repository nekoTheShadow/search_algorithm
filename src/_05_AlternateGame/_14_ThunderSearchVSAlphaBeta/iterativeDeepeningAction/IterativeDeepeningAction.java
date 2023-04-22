package _05_AlternateGame._14_ThunderSearchVSAlphaBeta.iterativeDeepeningAction;


import java.util.List;

import _05_AlternateGame._14_ThunderSearchVSAlphaBeta.State;
import _05_AlternateGame._14_ThunderSearchVSAlphaBeta.TimeKeeper;

public class IterativeDeepeningAction {
    private static int INF = 1000000000;
    
    public static int iterativeDeepeningAction(State state, long timeThreshold) {
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
    
    public static int alphaBetaActionWithTimeThreshold(State state, int depth, TimeKeeper timeKeeper) {
        int bestAction = -1;
        int alpha = -INF;
        int beta = INF;
        for (int action : state.legalActions()) {
            State nextState = new State(state);
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
    
    public static int alphaBetaScore(State state, int alpha, int beta, int depth, TimeKeeper timeKeeper) {
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
            State nextState = new State(state);
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
}
