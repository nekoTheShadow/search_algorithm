package _03_OnePlayerGame._02_TestRandomScore;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import _03_OnePlayerGame._01_Greedy.MazeState;

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
                state.advance(MazeState.randomAction(state));
            }
            int score = state.gameScore;
            scoreMean += score;
        }
        scoreMean /= gameNumber;
        System.out.println("Score:\t"+scoreMean);
    }
}
