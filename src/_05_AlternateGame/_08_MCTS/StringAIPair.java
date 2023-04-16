package _05_AlternateGame._08_MCTS;

import java.util.function.Function;

public class StringAIPair {
    public String first;
    public Function<AlternateMazeState, Integer> second;
    
    public StringAIPair(String first, Function<AlternateMazeState, Integer> second) {
        this.first = first;
        this.second = second;
    }
}
