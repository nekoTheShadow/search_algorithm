package _07_Advanced._01_GameScore;

import java.util.PriorityQueue;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int beamWidth = 100;
        int beamDepth = WallMazeState.END_TURN;
        var ai = new StringAIPair("beamSearchAction", state -> beamSearchAction(state, beamWidth, beamDepth));
        testAiScore(ai, 100);
    }
    
    public static void testAiScore(StringAIPair ai, int gameNumber) {
        Random mtForConstruct = new Random(0);
        double scoreMean = 0;
        for (int i = 0; i < gameNumber; i++) {
            var state = new WallMazeState(mtForConstruct.nextLong());
            while (!state.isDone()) {
                state.advance(ai.second.apply(state));
            }
            double score = state.gameScore;
            scoreMean += score;
        }
        scoreMean/=gameNumber;
        System.out.println("Score of " + ai.first + ":\t" + scoreMean);
    }
    
    public static int beamSearchAction(WallMazeState state, int beamWidth, int beamDepth) {
        PriorityQueue<WallMazeState> nowBeam = new PriorityQueue<>();
        nowBeam.add(state);
        
        WallMazeState bestState = null;
        for (int t = 0; t < beamDepth; t++) {
            PriorityQueue<WallMazeState> nextBeam = new PriorityQueue<>();
            for (int i = 0; i < beamWidth; i++) {
                if (nowBeam.isEmpty()) {
                    break;
                }
                WallMazeState nowState = nowBeam.poll();
                var legalActions = nowState.legalActions();
                for (int action : legalActions) {
                    WallMazeState nextState = new WallMazeState(nowState);
                    nextState.advance(action);
                    nextState.evaluatedScore();
                    if (t==0) {
                        nextState.firstAction = action;
                    }
                    nextBeam.add(nextState);
                }
            }
            
            
            nowBeam = nextBeam;
            bestState = nowBeam.peek();
            if (bestState.isDone()) {
                break;
            }
        }
        
        return bestState.firstAction;
    }
}
