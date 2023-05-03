package _07_Advanced._00_WallMazeState;

import java.util.Random;

public class Main {
    private static Random mtForAction = new Random(0);
    
    public static void main(String[] args) {
        playGame(2);
    }
    
    public static void playGame(long seed) {
        var state = new WallMazeState(seed);
        System.out.println(state);
        while (!state.isDone()) {
            state.advance(randomAction(state));
            System.out.println(state);
        }
    }
    
    public static int randomAction(WallMazeState state) {
        var legalActions = state.legalActions();
        return legalActions[mtForAction.nextInt(legalActions.length)];
    }
}
