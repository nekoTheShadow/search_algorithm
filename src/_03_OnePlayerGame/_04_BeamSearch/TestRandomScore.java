package _03_OnePlayerGame._04_BeamSearch;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class TestRandomScore {
    public static void main(String[] args) {
        testAiScore(100);
    }
    
    public static void testAiScore(int gameNumber) {
        Random random = ThreadLocalRandom.current();
        double scoreMean = 0;
        for (int i = 0; i < gameNumber; i++) {
            MazeState state = new MazeState(random.nextLong());
            while (!state.isDone()) {
                state.advance(MazeState.beamSearchAction(state, 2, MazeState.END_TURN));
            }
            int score = state.gameScore;
            scoreMean += score;
        }
        scoreMean /= gameNumber;
        System.out.println("Score:\t"+scoreMean);
    }
}
