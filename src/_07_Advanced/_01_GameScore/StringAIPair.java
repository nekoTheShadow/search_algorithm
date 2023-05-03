package _07_Advanced._01_GameScore;

import java.util.function.Function;

public class StringAIPair {
    public String first;
    public Function<WallMazeState, Integer> second;
    
    public StringAIPair(String first, Function<WallMazeState, Integer> second) {
        this.first = first;
        this.second = second;
    }
}
