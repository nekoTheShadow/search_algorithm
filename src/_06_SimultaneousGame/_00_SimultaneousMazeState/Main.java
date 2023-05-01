package _06_SimultaneousGame._00_SimultaneousMazeState;

import java.util.Random;

public class Main {
    private static Random mtForAction = new Random(0);
    private static String[] dstr = {"RIGHT", "LEFT", "DOWN", "UP"};
    
    public static int randomAction(SimultaneousMazeState state, int playerId) {
        var legalActions = state.legalActions(playerId);
        return legalActions.get(mtForAction.nextInt(legalActions.size()));
    }
    
    public static void playGame(StringAIPair[] ais, long seed) {
        var state = new SimultaneousMazeState(seed);
        System.out.println(state);
        while (!state.isDone()) {
            int[] actions = new int[] {ais[0].second.apply(state, 0), ais[1].second.apply(state, 1)};
            System.out.println("actions " + dstr[actions[0]] + " " + dstr[actions[1]]);
            state.advance(actions[0], actions[1]);
            System.out.println(state);
        }
    }
    
    public static void main(String[] args) {
        StringAIPair[] ais = {
            new StringAIPair("randomAction", (state, playerId) -> randomAction(state, playerId)),	
            new StringAIPair("randomAction", (state, playerId) -> randomAction(state, playerId))
        };
        playGame(ais, 0);
    }
}
