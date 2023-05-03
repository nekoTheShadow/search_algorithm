package _07_Advanced._03_ZobristHash;

import java.util.function.Function;

public class StringAIPair {
    public String first;
    public Function<WallMazeState, Integer> second;
    
    public StringAIPair(String first, Function<WallMazeState, Integer> second) {
        this.first = first;
        this.second = second;
    }
}
