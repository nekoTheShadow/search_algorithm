package _04_HeuristicGame._00_AutoMoveMazeState;

import java.util.function.Function;

public class StringAIPair {
    public String first;
    public Function<AutoMoveMazeState, AutoMoveMazeState> second;
    
    public StringAIPair(String first, Function<AutoMoveMazeState, AutoMoveMazeState> second) {
        this.first = first;
        this.second = second;
    }
}
