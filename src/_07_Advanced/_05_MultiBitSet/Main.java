package _07_Advanced._05_MultiBitSet;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        int beamWidth = 100;
        int beamDepth = WallMazeState.END_TURN;
        var ai = new StringAIPair("beamSearchAction", state -> beamSearchAction(state, beamWidth, beamDepth));
        testAiScore(ai, 100);
        testAiSpeed(ai, 100, 10);
    }
    
    public static void testAiSpeed(StringAIPair ai, int gameNumber, int perGameNumber) {
        Random mtForConstruct = new Random(0);
        long diffSum = 0;
        for (int i = 0; i < gameNumber; i++) {
            var state = new WallMazeState(mtForConstruct.nextLong());
            var stateBit = new MazeStateByBitSet(state);
            var startTime = LocalDateTime.now();
            for (int j = 0; j < perGameNumber; j++) {
                ai.second.apply(stateBit);
            }
            long diff = ChronoUnit.MILLIS.between(startTime, LocalDateTime.now());
            diffSum += diff;
        }
        
        double timeMean = ((double)diffSum)/((double)gameNumber);
        System.out.println("Time of " + ai.first + ":\t" + timeMean + "ms");
    }
    
    public static void testAiScore(StringAIPair ai, int gameNumber) {
        Random mtForConstruct = new Random(0);
        double scoreMean = 0;
        for (int i = 0; i < gameNumber; i++) {
            var state = new WallMazeState(mtForConstruct.nextLong());
            var stateBit = new MazeStateByBitSet(state);
            while (!stateBit.isDone()) {
                stateBit.advance(ai.second.apply(stateBit));
            }
            double score = stateBit.gameScore;
            scoreMean += score;
        }
        scoreMean/=gameNumber;
        System.out.println("Score of " + ai.first + ":\t" + scoreMean);
    }
    
    public static int beamSearchAction(MazeStateByBitSet state, int beamWidth, int beamDepth) {
        
        PriorityQueue<MazeStateByBitSet> nowBeam = new PriorityQueue<>();
        nowBeam.add(state);
        MazeStateByBitSet bestState = null;
        Set<Long> hashCheck = new HashSet<>();
        
        for (int t = 0; t < beamDepth; t++) {
            PriorityQueue<MazeStateByBitSet> nextBeam = new PriorityQueue<>();
            for (int i = 0; i < beamWidth; i++) {
                if (nowBeam.isEmpty()) {
                    break;
                }
                MazeStateByBitSet nowState = nowBeam.poll();
                var legalActions = nowState.legalActions();
                for (int action : legalActions) {
                    MazeStateByBitSet nextState = new MazeStateByBitSet(nowState);
                    nextState.advance(action);
                    if (t>=1 && hashCheck.contains(nextState.hash)) {
                        continue;
                    }
                    hashCheck.add(nextState.hash);
                    
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
