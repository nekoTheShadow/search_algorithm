package _07_Advanced._05_MultiBitSet;

import java.util.function.Function;

public class StringAIPair {
    public String first;
    public Function<MazeStateByBitSet, Integer> second;
    
    public StringAIPair(String first, Function<MazeStateByBitSet, Integer> second) {
        this.first = first;
        this.second = second;
    }
}
