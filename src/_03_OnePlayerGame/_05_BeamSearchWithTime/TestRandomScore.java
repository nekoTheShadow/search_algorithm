package _03_OnePlayerGame._05_BeamSearchWithTime;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class TestRandomScore {
    public static void main(String[] args) {
        testAiScore(100);
    }
    
    // 本書では1msでテストしているが、Javaだと1msではテストができない 
    // (BestStateまで探索できず、NLPが発生する)
    // 少なくとも10ms程度は必要。
    public static void testAiScore(int gameNumber) {
        Random random = ThreadLocalRandom.current();
        double scoreMean = 0;
        for (int i = 0; i < gameNumber; i++) {
            MazeState state = new MazeState(random.nextLong());
            while (!state.isDone()) {
                state.advance(MazeState.beamSearchActionWithTimeThreshold(state, 5, 50));
            }
            int score = state.gameScore;
            scoreMean += score;
        }
        scoreMean /= gameNumber;
        System.out.println("Score:\t"+scoreMean);
    }
}
